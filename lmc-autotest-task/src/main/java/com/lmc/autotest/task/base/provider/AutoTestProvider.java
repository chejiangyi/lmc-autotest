package com.lmc.autotest.task.base.provider;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.*;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_report_model;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import com.lmc.autotest.dao.tb_report_dal;
import com.lmc.autotest.dao.tb_report_node_dal;
import com.lmc.autotest.dao.tb_report_url_dal;
import com.lmc.autotest.service.LogTool;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.task.AutoTestManager;
import com.lmc.autotest.task.base.DynamicScript;
import com.lmc.autotest.task.base.FileUtils;
import com.lmc.autotest.task.base.HttpUtils;
import com.lmc.autotest.task.base.SampleUtils;
import com.xxl.job.core.util.DateUtil;
import lombok.Getter;
import lombok.val;
import lombok.var;
import sun.swing.StringUIClientPropertyKey;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

public class AutoTestProvider {
    private tb_task_model task_model = null;
    private ReportProvider reportProvider = null;
    private Integer taskid=-1;
    @Getter
    private boolean isRun =false;
    private String tempid= UUID.randomUUID().toString().replace("-","");
    private Date startTime = new Date();
    public AutoTestProvider(Integer taskid){
        this.taskid=taskid;
    }

    public AutoTestProvider init(){
        DbHelper.get(Config.mysqlDataSource(), (c) -> {
            task_model = new tb_task_dal().get(c, this.taskid);
            return true;
        });
        if (task_model == null)
            throw new BsfException(taskid + "任务不存在");
        this.reportProvider = new ReportProvider();
        this.reportProvider.init(task_model);
        return this;
    }

    public void run(){
        ThreadUtils.system().submit("自动化压测运行任务",()->{
            isRun=true;
            try {
                hearBeat();
                createSampleFile();
                filterError();
                autoTest();
            }catch (Exception e){
                close("启动执行异常");
                LogTool.error(AutoTestProvider.class,Config.appName(),task_model.task+"-自动化压测运行任务异常",e);
                //throw e;
            }
        });
    }

    public void close(String reason){
        isRun=false;
        AutoTestManager.Default.close(taskid,reason);
        FileUtils.delete(getFileName(task_model.filter_table));
        FileUtils.delete(getFileName(task_model.filter_table)+".temp");
        FileUtils.clearExpireFile("/");
        try{
            String reason2=Config.nodeName()+":"+StringUtils.nullToEmpty(reason);
            DbHelper.transactionCall(Config.mysqlDataSource(), (c) -> {
                val task = new tb_task_dal().getWithLock(c,taskid);
                new tb_task_dal().addResult(c, taskid,task.exec_result+"\r\n"+reason2);
            });
        }
        catch (Exception e){
            LogUtils.error(this.getClass(),Config.appName(),"关闭原因更新数据库出错",e);
        }
    }

    private void autoTest(){
        for(int i=0;i<task_model.run_threads_count;i++){
            val index = i;
            ThreadUtils.system().submit("压测线程"+index,()->{
                while (!ThreadUtils.system().isShutdown()&&isRun){
                    try {
                        SampleUtils.readline(getFileName(task_model.filter_table),(line)->{
                            try {
                                if (isRun)
                                    return;
                                tb_sample_example_model j = JsonUtils.deserialize(line, tb_sample_example_model.class);
                                if (j == null || StringUtils.isEmpty(j.url))
                                    return;
                                //前
                                if (!StringUtils.isEmpty(task_model.http_begin_script)) {
                                    val sMap = new LinkedHashMap();
                                    sMap.put("request", j);
                                    val r1 = DynamicScript.run("执行前脚本", task_model.http_begin_script, sMap);
                                    if (r1 == null || !(r1 instanceof Boolean) || (boolean) r1 != true) {
                                        return;
                                    }
                                }
                                val r = HttpUtils.request(j);
                                //后
                                if (!StringUtils.isEmpty(task_model.http_end_script)) {
                                    val sMap2 = new LinkedHashMap();
                                    sMap2.put("request", j);
                                    sMap2.put("response", r);
                                    val r2 = DynamicScript.run("执行后脚本", task_model.http_end_script, sMap2);
                                    if (r2 == null || !(r2 instanceof Boolean) || (boolean) r2 != true) {
                                        return;
                                    }
                                }
                                //结束
                                this.reportProvider.updateReport(r);
                            }catch (Exception e){
                                LogTool.error(AutoTestProvider.class,Config.appName(),"处理样本文件单行出错:"+e.getMessage(),e);
                            }
                        });
                    }catch (Exception e){
                        LogTool.error(AutoTestProvider.class,Config.appName(),"压测线程"+index+"异常",e);
                    }
                }
            });
        }
    }

    private void hearBeat(){
        ThreadUtils.system().submit("心跳更新任务",()->{
            while (!ThreadUtils.system().isShutdown()&&isRun){
                try {
                    DbHelper.call(Config.mysqlDataSource(),(c)-> {
                        new tb_task_dal().runHeartBeat(c, taskid);
                    });
                    val nodeReport = reportProvider.heartBeatReport();
                    if(!StringUtils.isEmpty(task_model.check_stop_script)) {
                        val sMap = new LinkedHashMap();sMap.put("nodeReport",nodeReport);sMap.put("runtime",(startTime.getTime()-new Date().getTime())/1000);
                        val r2 = DynamicScript.run("执行后脚本", task_model.http_end_script, sMap);
                        if(r2!=null&&(r2 instanceof Boolean)&&(boolean)r2==true){
                            close("匹配执行后脚本规则");
                        }
                    }
                }catch (Exception e){
                    LogTool.error(AutoTestProvider.class,Config.appName(),"心跳更新任务",e);
                }
                ThreadUtils.sleep(Config.heartbeat());
            }

        });
    }

    //生成采样表文件
    private void createSampleFile(){
        val map = new LinkedHashMap();map.put("task",task_model);
        DynamicScript.run("采样表文件生成",task_model.filter_script,map);
    }
    //过滤错误日志
    private void filterError(){
        if(task_model.clear_data_first) {
            //生成临时文件
            String filename = getFileName(task_model.filter_table);
            String temp = filename + ".temp";
            val file = new File(filename);
            if (!file.exists()) {
                throw new BsfException("文件不存在:" + filename);
            }
            file.renameTo(new File(temp));
            //重新生成文件
            SampleUtils.readline(temp, (line) -> {
                if (isRun) {
                    tb_sample_example_model j = JsonUtils.deserialize(line, tb_sample_example_model.class);
                    val r = HttpUtils.request(j);
                    if (r.getCode() == 200) {
                        SampleUtils.writeline(filename, line);
                    }
                }
            });
        }
    }
    private String getFileName(String filename){
        return tempid+"-"+filename+".autotest";
    }
}
