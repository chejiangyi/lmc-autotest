package com.lmc.autotest.provider.base.js;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Callable;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.*;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_job_model;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_job_dal;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.provider.base.QuartzJob;
import com.lmc.autotest.provider.base.QuartzManager;
import com.lmc.autotest.provider.base.Utils;
import com.lmc.autotest.service.LogTool;
import com.lmc.autotest.service.TaskService;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.val;
import lombok.var;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import javax.sql.DataSource;
import java.util.*;

public class ApiScript {
    protected String scriptInfo;
    //参数
    public LinkedHashMap ps = new LinkedHashMap();

    public ApiScript(String scriptInfo, LinkedHashMap form){
        this.scriptInfo=scriptInfo;
        this.ps = form;
    }

    //普通日志
    public void log(Object info){
        checkJobState();
        LogTool.info(ApiScript.class,0,Config.nodeName(),"计划任务脚本普通日志:"+scriptInfo+"=>"+JsonUtils.serialize(info));
        //DebugUtil.webDebug(info);
    }
    //错误日志
    public void error(Object info){
        checkJobState();
        LogTool.error(ApiScript.class,0,Config.nodeName(),"计划任务脚本错误日志:"+scriptInfo+"=>"+JsonUtils.serialize(info),null);
        //DebugUtil.webDebug(info);
    }
    //调试日志,仅在节点日志中查看到,不展示到web站点
    public void debug(Object info){
        checkJobState();
        LogUtils.info(ApiScript.class,Config.nodeName(),"计划任务脚本【调试】日志:"+scriptInfo+"=>"+JsonUtils.serialize(info));
    }
    //转json
    public String toJson(Object json){
        return  JsonUtils.serialize(toJavaObject(json));
    }
    //来自json
    public Object fromJson(String json){
        return  JsonUtils.deserialize(json,Object.class);
    }

    //转java类型
    public Object toJavaObject(Object obj) {
        if(obj == null ||!(obj instanceof ScriptObjectMirror)){
            return obj;
        }
        ScriptObjectMirror mirror = (ScriptObjectMirror) obj;
        if (mirror.isEmpty()) {
            return null;
        }
        if (mirror.isArray()) {
            List<Object> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : mirror.entrySet()) {
                Object result = entry.getValue();
                if (result instanceof ScriptObjectMirror) {
                    list.add(toJavaObject((ScriptObjectMirror) result));
                } else {
                    list.add(result);
                }
            }
            return list;
        }

        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : mirror.entrySet()) {
            Object result = entry.getValue();
            if (result instanceof ScriptObjectMirror) {
                map.put(entry.getKey(), toJavaObject((ScriptObjectMirror) result));
            } else {
                map.put(entry.getKey(), result);
            }
        }
        return map;
    }

    //线程睡眠
    public void sleep(Integer time){
        int timeSpan = 1000;
        val count = time/timeSpan;
        for(var i=0;i<count;i++){
            checkJobState();
            ThreadUtils.sleep(timeSpan);
        }
        ThreadUtils.sleep(time%timeSpan);
    }

    //当前时间格式化
    public String nowFormat(String format){
        return org.apache.http.client.utils.DateUtils.formatDate(new Date(),format);
    }

    //http请求
    public Object httpPost(String url,Object json){
        checkJobState();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setContentEncoding("utf-8").setText(toJson(json)).build());
        try {
            val r = HttpClientUtils.system().toString(HttpClientUtils.system().getClient().execute(httpPost));
            return r;
        }catch (Exception e){
            return null;
        }
    }

    //http请求 form请求支持
    public Object httpPostForm(String url,Object form){
        checkJobState();
        HttpClient.Params params = HttpClient.Params
                .custom()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .add(toJavaObject(form)).build();
        return HttpClientUtils.system().post(url, params);
    }
    //http请求 get
    public Object httpGet(String url){
        checkJobState();
        return HttpClientUtils.system().get(url);
    }

    //执行sql
    public Object querySql(String sql,Object[] ps){
        checkJobState();
        try{
        val r = DbHelper.get(ContextUtils.getBean(DataSource.class,false),(c)->{
            return c.executeList(sql,ps);
        });
        return  r;
        }catch (Exception e){
            val msg = "动态sql出错:"+sql+",参数:"+JsonUtils.serialize(ps);
            LogTool.error(ApiScript.class,getJobId(),"bsf",msg,e);
            throw new BsfException(msg);
        }
    }

    public boolean tryOpenTask(Integer taskid){
        try {
            openTask(taskid);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void openTask(Integer taskid){
       openTask2(taskid,null);
    }

    public boolean tryOpenTask2(Integer taskid,Object params){
        try {
            openTask2(taskid,params);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void openTask2(Integer taskid,Object params){
        checkJobState();
        DbHelper.call(ContextUtils.getBean(DataSource.class, false), (c) -> {
            val job = new tb_job_dal().get(c, getJobId());
            tb_task_model model = new tb_task_dal().get(c, taskid);
            if(model ==null){
                throw new BsfException("任务id不存在:"+taskid);
            }
            new TaskService().operatorTask(c, taskid, "运行", job.create_user_id,JsonUtils.deserialize(JsonUtils.serialize(params),new HashMap<String,Object>().getClass()));
        });
        new Utils().checkCondition(5000,()->{
            return DbHelper.get(ContextUtils.getBean(DataSource.class, false), (c) -> {
                tb_task_model model = new tb_task_dal().get(c, taskid);
                return new Utils().isOnline(model.run_heart_time);
            });
        });
    }

    public boolean isTaskRunning(Integer taskid){
        checkJobState();
        return DbHelper.get(ContextUtils.getBean(DataSource.class, false), (c) -> {
            val task = new tb_task_dal().get(c, taskid);
            if(task==null)
                return false;
            return new Utils().isOnline(task.run_heart_time);
        });
    }

    public boolean isTaskExist(Integer taskid){
        checkJobState();
        return DbHelper.get(ContextUtils.getBean(DataSource.class, false), (c) -> {
            val task = new tb_task_dal().get(c, taskid);
            if(task==null)
                return false;
            return true;
        });
    }

    public boolean tryCloseTask(Integer taskid){
        try {
            closeTask(taskid);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void closeTask(Integer taskid){
        checkJobState();
        DbHelper.call(ContextUtils.getBean(DataSource.class, false), (c) -> {
            val job = new tb_job_dal().get(c, getJobId());
            tb_task_model model = new tb_task_dal().get(c, taskid);
            new TaskService().operatorTask(c, taskid, "停止", job.create_user_id,null);
        });
        new Utils().checkCondition(5000,()->{
            return DbHelper.get(ContextUtils.getBean(DataSource.class, false), (c) -> {
                tb_task_model model = new tb_task_dal().get(c, taskid);
                return !new Utils().isOnline(model.run_heart_time);
            });
        });
    }

    private Integer getJobId(){
        val job = this.ps.get("job");
        if(job!=null&& job instanceof tb_job_model){
            return ((tb_job_model)job).id;
        }
        return 0;
    }

    private void checkJobState(){
        DbHelper.call(ContextUtils.getBean(DataSource.class, false), (c) -> {
            val job = new tb_job_dal().get(c, getJobId());
            if("停止".equals(job.state)){
                throw new BsfException("检测到定时计划状态已停止,强制停止当前运行时!");
            }
        });
    }



    public static void main(String[] args) {
        val api = new ApiScript("",new LinkedHashMap());
        for(int i=0;i<20;i++) {
            System.out.println(new Date());
            try {
                new Utils().checkCondition(5000, () -> {
                    return false;
                });
            }catch (Exception e){}
            System.out.println(new Date());
        }
//        String[] sql = new String[]{"select * from auto_tb_sample_a",
//                "update from auto_tb_sample_a set a=1",
//                "select * from b set a=1",
//                "insert b values(?,?,?,?)",
//                "select * from b where a=1",
//                "1=2"
//        };
//        for(val s:sql) {
//            try {
//                AutoTestTool.checkSampleSelectSql(s);
//            }catch (Exception e){
//                System.err.println(sql);
//                System.err.println(e);
//            }
//        }
    }

}
