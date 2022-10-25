package com.lmc.autotest.task.base.provider;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.*;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_report_model;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import com.lmc.autotest.dao.tb_report_dal;
import com.lmc.autotest.dao.tb_report_node_dal;
import com.lmc.autotest.dao.tb_report_url_dal;
import com.lmc.autotest.service.LogTool;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.task.NodeManager;
import com.lmc.autotest.task.base.*;
import com.lmc.autotest.task.base.FileUtils;
import lombok.Getter;
import lombok.val;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoTestProvider {
    private tb_task_model task_model = null;
    private ReportProvider reportProvider = null;
    private NodeProvider nodeProvider=null;
    @Getter
    private Integer taskid=-1;
    @Getter
    private boolean isRun =false;
    //private String tempid= UUID.randomUUID().toString().replace("-","");
    private Date startTime = new Date();
    private String tranId = "";
    private Integer index=-1;
    private ScheduledExecutorService checkStopThreadPool = Executors.newScheduledThreadPool(1);
    private Integer userid=0;
    public AutoTestProvider(Integer taskid,String tranId,NodeProvider nodeProvider,Integer index,Integer userid){
        this.taskid=taskid;
        this.tranId = tranId;
        this.nodeProvider = nodeProvider;
        this.index=index;
        this.userid=userid;
    }

    public AutoTestProvider init(){
        try {
            DbHelper.get(Config.mysqlDataSource(), (c) -> {
                task_model = new tb_task_dal().get(c, this.taskid);
                return true;
            });
            if (task_model == null)
                throw new BsfException(taskid + "任务不存在");

            this.reportProvider = new ReportProvider();
            this.reportProvider.init(task_model,this.tranId,this.userid);
            return this;
        } catch (Exception e) {
            LogTool.error(this.getClass(), taskid,Config.appName(), "初始化自动化测试失败", e);
            throw e;
        }
    }

    public void run(){
        ThreadUtils.system().submit("自动化压测运行任务",()->{
            isRun=true;
            try {
                disposeFile();
                if(!this.checkRunning()){ return;}
                hearBeat();
                if(!this.checkRunning()){ return;}
                checkStop();
                if(!this.checkRunning()){ return;}
                createSampleFile();
                if(!this.checkRunning()){ return;}
                filterError();
                if(!this.checkRunning()){ return;}
                autoTest();
            }catch (Exception e){
                close("启动执行异常");
                LogTool.error(AutoTestProvider.class,taskid,Config.appName(),task_model.task+"-自动化压测运行任务异常",e);
                //throw e;
            }
        });
    }

    public void close(String reason){
        isRun = false;
        try {
            if(checkStopThreadPool!=null){
                checkStopThreadPool.shutdown();
                checkStopThreadPool=null;
            }
            String reason2 = Config.nodeName() + ":" + StringUtils.nullToEmpty(reason);
            DbHelper.call(Config.mysqlDataSource(), (c) -> {
                val task = new tb_task_dal().getWithLock(c, taskid);
                new tb_task_dal().closeHeartBeat(c, taskid);
                new tb_task_dal().appendResult(c, taskid,  "\r\n"+ reason2);
            });
            if(NodeManager.Default.getAutoTestProvider()==this&&NodeManager.Default.getAutoTestProvider().tranId.equals(this.tranId)) {
                NodeManager.Default.close(taskid, reason, true);
            }
            LogTool.info(this.getClass(),taskid, Config.appName(), StringUtils.nullToEmpty(task_model.task) + "-压测任务已关闭");
        } catch (Exception e) {
            LogTool.error(this.getClass(), taskid,Config.appName(), "关闭原因更新数据库出错", e);
        }
        System.gc();//建议进行内存回收!
        disposeFile();
    }

    private void autoTest(){
        LogTool.info(this.getClass(),taskid,Config.appName(),StringUtils.nullToEmpty(task_model.task)+"-压测任务准备启动,线程逐步开启中(缓慢开启,若线程多,等待时间较长)...");
        for(int i=0;i<task_model.run_threads_count;i++){
            val index = i;
            if(task_model.sleep_time_every_thread<=0) {
                //简单的自动均衡算法
                int waitTime = new Random(UUID.randomUUID().toString().hashCode()).nextInt(Config.maxSleepPerTheadOpen());
                ThreadUtils.sleep(waitTime < 200 ? 200 : waitTime);//给程序缓冲时间,缓慢启动,不得小于200
            }else{
                //超过1分钟不合理
                if(task_model.sleep_time_every_thread>60*1000){
                    ThreadUtils.sleep(60*1000);
                }else {
                    ThreadUtils.sleep(task_model.sleep_time_every_thread);
                }
            }
            ThreadPoolUtils.System.submit("压测线程"+index,()->{
                while (!ThreadUtils.system().isShutdown()&&isRun){
                    try {
                        SampleUtils.readline(FileUtils.getSampleFile(task_model.id,tranId),(line)->{
                            try {
                                if (!isRun||StringUtils.isEmpty(line))
                                    return;
                                tb_sample_example_model j = JsonUtils.deserialize(line, tb_sample_example_model.class);
                                if (j == null || StringUtils.isEmpty(j.url))
                                    return;
                                //前
                                if (!StringUtils.isEmpty(task_model.http_begin_script)) {
                                    val sMap= this.initMap();
                                    sMap.put("request", j);
                                    val r1 = DynamicScript.run("执行前脚本", task_model.http_begin_script, sMap);
                                    if (r1 != null && (!((r1 instanceof Boolean) && (boolean) r1 != true))) {
                                        return;
                                    }
                                }
                                val r = HttpUtils.request(j,false,task_model.use_http_keepalive);
                                //后
                                if (!StringUtils.isEmpty(task_model.http_end_script)) {
                                    val sMap2= this.initMap();
                                    sMap2.put("request", j);
                                    sMap2.put("response", r);
                                    val r2 = DynamicScript.run("执行后脚本", task_model.http_end_script, sMap2);
                                    if (r2 != null && (!((r2 instanceof Boolean) && (boolean) r2 != true))) {
                                        return;
                                    }
                                }
                                //结束
                                this.reportProvider.updateReport(r);
                            }catch (Exception e){
                                LogTool.error(AutoTestProvider.class,taskid,Config.appName(),"处理样本文件单行出错:"+e.getMessage(),e);
                            }
                        });
                    }catch (Exception e){
                        LogTool.error(AutoTestProvider.class,taskid,Config.appName(),"压测线程"+index+"异常",e);
                    }
                }
            });
        }
        LogTool.info(this.getClass(),taskid,Config.appName(),StringUtils.nullToEmpty(task_model.task)+"-压测任务线程已全部启动,压测中...");
    }

    private void hearBeat(){
        ThreadUtils.system().submit("心跳更新任务",()->{
            while (!ThreadUtils.system().isShutdown()&&isRun){
                try {
                    DbHelper.call(Config.mysqlDataSource(),(c)-> {
                        new tb_task_dal().runHeartBeat(c, taskid);
                    });
                }catch (Exception e){
                    LogTool.error(AutoTestProvider.class,taskid,Config.appName(),"心跳更新任务错误",e);
                }
                ThreadUtils.sleep(Config.heartbeat()*1000);
            }

        });
        LogTool.info(this.getClass(),taskid,Config.appName(),StringUtils.nullToEmpty(task_model.task)+"-任务心跳检测已开启");
    }

    private void checkStop(){
        try {
            AutoTestTool.clockCorrection();
            LogTool.info(AutoTestProvider.class,taskid,Config.appName(),"任务周期上报起始时间:"+DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss:SSS"));
        }catch (Exception e){
            LogTool.error(AutoTestProvider.class,taskid,Config.appName(),"检查任务终止时,时钟对齐出错",e);
        }
        checkStopThreadPool.scheduleAtFixedRate(()->{
            if(!ThreadUtils.system().isShutdown()&&isRun) {
                //LogTool.info(this.getClass(), taskid, Config.appName(), "任务信息上报");
                try {
                    val nodeReport = reportProvider.heartBeatReport();
                    if (!StringUtils.isEmpty(task_model.check_stop_script)) {
                        val sMap = this.initMap();
                        sMap.put("nodeReport", nodeReport == null ? null : nodeReport.toModel());
                        sMap.put("runtime", (new Date().getTime() - startTime.getTime()) / 1000);
                        val r2 = DynamicScript.run("任务终止判断脚本", task_model.check_stop_script, sMap);
                        if (r2 != null && (r2 instanceof Boolean) && (boolean) r2 == false) {
                            LogTool.info(this.getClass(), taskid, Config.appName(), StringUtils.nullToEmpty(task_model.task) + "-压测任务命中任务终止判断脚本规则");
                            close("命中任务终止判断脚本规则");
                        }
                    }
                } catch (Exception e) {
                    LogTool.error(AutoTestProvider.class, taskid, Config.appName(), "检查任务终止错误", e);
                }
            }
        },0,Config.heartbeat(), TimeUnit.SECONDS);
        LogTool.info(this.getClass(),taskid,Config.appName(),StringUtils.nullToEmpty(task_model.task)+"-任务终止检测已开启");
    }

    //生成采样表文件
    private void createSampleFile(){
       val map = this.initMap();
        DynamicScript.run("采样表文件生成",task_model.filter_script,map);
        if(!this.checkRunning()){ return;}
        LogTool.info(this.getClass(),taskid,Config.appName(),StringUtils.nullToEmpty(task_model.task)+"-采样文件生成完成");
    }
    //过滤错误样本
    private void filterError(){
        int fileLines = FileUtils.fileCount(FileUtils.getSampleFile(task_model.id,tranId));
        Ref<Integer> errorLines=new Ref<Integer>(0);
        if(!StringUtils.isEmpty(task_model.first_filter_error_script)) {
            //生成临时文件
            String filename = FileUtils.getSampleFile(task_model.id,tranId);
            String temp = filename + ".temp";
            try {
                val file = new File(filename);
                if (!file.exists()) {
                    throw new BsfException("文件不存在:" + filename);
                }
                file.renameTo(new File(temp));
                //重新生成文件
                SampleUtils.reCreate(filename);
                if(fileLines>500) {
                    LogTool.info(this.getClass(), taskid, Config.appName(), StringUtils.nullToEmpty(task_model.task) + "样本"+fileLines+"条,过滤错误样本耗时较长,请耐心等待....");
                }
                Ref<Long> dealCount = new Ref<>(0L);
                SampleUtils.readline(temp, (line) -> {
                    if (isRun) {
                        tb_sample_example_model j = JsonUtils.deserialize(line, tb_sample_example_model.class);
                        val r = HttpUtils.request(j,false,task_model.use_http_keepalive);
                        val sMap2= this.initMap();
                        sMap2.put("request", j);
                        sMap2.put("response", r);
                        val r2 = DynamicScript.run("第一次过滤错误样本",task_model.first_filter_error_script,sMap2);
                        if (r2!=null&&(r2 instanceof Boolean)&&(boolean)r2==false) {
                            errorLines.setData(errorLines.getData() + 1);
                        }else{
                            SampleUtils.writeline(filename, line);
                        }
                        dealCount.setData(dealCount.getData()+1);
                        if(dealCount.getData()%500==0) {
                            LogTool.info(this.getClass(), taskid, Config.appName(), StringUtils.nullToEmpty(task_model.task) + "-过滤错误样本中,已处理"+dealCount.getData()+"条!");
                        }
                    }
                });
                if(!this.checkRunning()){ return;}
                LogTool.info(this.getClass(),taskid,Config.appName(),StringUtils.nullToEmpty(task_model.task)+"-过滤错误样本完成");
            }finally {
                FileUtils.delete(temp);
            }

        }
        DbHelper.call(Config.mysqlDataSource(),(c)-> {
            new tb_report_dal().updateFilterTableInfo(c,reportProvider.report_model.id,fileLines,errorLines.getData());
        });
    }

    public boolean checkRunning(){
        if(!isRun){
            this.disposeFile();
            return false;
        }
        return true;
    }

    public void disposeFile(){
        try {
            FileUtils.delete(FileUtils.getSampleFile(task_model.id, tranId) + ".temp");
            FileUtils.delete(FileUtils.getSampleFile(task_model.id, tranId));
            FileUtils.clearExpireFile(new File("").getAbsolutePath());
        } catch (Exception e) {
            LogTool.error(this.getClass(), taskid,Config.appName(), "关闭删除文件出错", e);
        }
    }

    private LinkedHashMap initMap(){
        val map = new LinkedHashMap();
        map.put("task",task_model);
        map.put("node",this.nodeProvider.getNode_model());
        map.put("report",this.reportProvider.getReport_model());
        map.put("autotest",this);
        map.put("tranId",tranId);
        map.put("index",index);
        return map;
    }

}
