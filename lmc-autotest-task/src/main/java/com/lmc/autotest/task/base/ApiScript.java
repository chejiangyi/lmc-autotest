package com.lmc.autotest.task.base;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.*;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.service.LogTool;
import com.lmc.autotest.task.base.provider.AutoTestProvider;
import com.xxl.job.core.util.DateUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.time.DateUtils;
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
        LogTool.info(ApiScript.class,getTaskId(),Config.nodeName(),"动态脚本普通日志:"+scriptInfo+"=>"+JsonUtils.serialize(info));
        //DebugUtil.webDebug(info);
    }
    //错误日志
    public void error(Object info){
        LogTool.error(ApiScript.class,getTaskId(),Config.nodeName(),"动态脚本错误日志:"+scriptInfo+"=>"+JsonUtils.serialize(info),null);
        //DebugUtil.webDebug(info);
    }
    //调试日志,仅在节点日志中查看到,不展示到web站点
    public void debug(Object info){
        LogUtils.info(ApiScript.class,Config.nodeName(),"动态脚本【调试】日志:"+scriptInfo+"=>"+JsonUtils.serialize(info));
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
    //写样本文件
    public void writeSample(Object sample){
        val tranId = ps.get("tranId");
        SampleUtils.writeline(FileUtils.getSampleFile(getTaskId(),tranId.toString()), JsonUtils.serialize(sample));
    }


    //当前时间格式化
    public String nowFormat(String format){
        return DateUtil.format(new Date(),format);
    }

    //执行sql
    public Object querySql(String sql,Object...ps){
        try{
        val r = DbHelper.get(ContextUtils.getBean(DataSource.class,false),(c)->{
//            c.getConn()
            return c.executeList(sql,ps);
        });
        return  r;
        }catch (Exception e){
            val msg = "动态sql出错:"+sql+",参数:"+JsonUtils.serialize(ps);
            LogTool.error(ApiScript.class,getTaskId(),"bsf",msg,e);
            throw new BsfException(msg);
        }
    }
    //执行sql
    public void streamSql(String sql,Object[] ps,ScriptObjectMirror objectMirror){
        val task = this.ps.get("task");
        try{
            val autoTest = (AutoTestProvider)this.ps.get("autotest");
             DbHelper.call(ContextUtils.getBean(DataSource.class,false),(c)->{
                 Ref<Long> size=new Ref(0L);
                 c.executeStream(sql,ps, Config.streamSize(),(map)->{
                     if(autoTest.isRun()) {
                         if (objectMirror != null) {
                             objectMirror.call(objectMirror, map);
                         }
                         size.setData(size.getData() + 1L);
                         if (size.getData() % Config.streamSize() == 0) {
                             LogTool.info(this.getClass(),getTaskId(), Config.appName(), "streamSql处理数据中:" + size.getData());
                         }
                     }
                 });
                 LogTool.info(this.getClass(),getTaskId(),Config.appName(),"streamSql共处理数据:"+size.getData());
            });
        }catch (Exception e){
            val msg = "动态sql出错:"+sql+",参数:"+JsonUtils.serialize(ps);
            LogTool.error(ApiScript.class,getTaskId(),"bsf",msg,e);
            throw new BsfException(msg);
        }
    }
    private Integer getTaskId(){
        val task = this.ps.get("task");
        if(task!=null&& task instanceof tb_task_model){
            return ((tb_task_model)task).id;
        }
        return 0;
    }
    //执行sql2
    public void streamSql2(String sql,Object[] ps,ScriptObjectMirror objectMirror){

        try{
            val autoTest = (AutoTestProvider)this.ps.get("autotest");
            DbHelper.call(ContextUtils.getBean(DataSource.class,false),(c)->{
                Long id=0L;Long size=0L;
                while (autoTest.isRun()) {
                    String sql2 = "select * from ({table})a where id>{id} order by id asc limit {size} "
                            .replace("{table}", sql).replace("{id}", id+"").replace("{size}", Config.streamSize()+"");
                    val list = c.executeList(sql2, ps);
                    if(list.size()==0)
                        break;
                    if(!autoTest.checkRunning()){return;}
                    for(val map : list) {
                        id = Math.max(ConvertUtils.convert(map.get("id"),long.class),id);
                        if(objectMirror!=null) {
                            objectMirror.call(objectMirror, map);
                        }
                    }
                    if(!autoTest.checkRunning()){return;}
                    size+=list.size();
                    LogTool.info(this.getClass(),getTaskId(),Config.appName(),"streamSql2处理数据中:"+size);
                }
                LogTool.info(this.getClass(),getTaskId(),Config.appName(),"streamSql2共处理数据:"+size);
            });
        }catch (Exception e){
            val msg = "动态sql出错:"+sql+",参数:"+JsonUtils.serialize(ps);
            LogTool.error(ApiScript.class,getTaskId(),"bsf",msg,e);
            throw new BsfException(msg);
        }
    }


}
