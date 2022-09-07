package com.lmc.autotest.task.base;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.*;
import com.lmc.autotest.service.LogTool;
import com.xxl.job.core.util.DateUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.val;
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
        LogTool.info(ApiScript.class,"bsf","动态脚本普通日志:"+scriptInfo+"=>"+JsonUtils.serialize(info));
        //DebugUtil.webDebug(info);
    }
    //调试日志
    public void error(Object info){
        LogTool.error(ApiScript.class,"bsf","动态脚本调试日志:"+scriptInfo+"=>"+JsonUtils.serialize(info),null);
        //DebugUtil.webDebug(info);
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
    public void writeSample(String filename,Object sample){
        SampleUtils.writeline(filename,JsonUtils.serialize(sample));
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
            LogUtils.error(ApiScript.class,"bsf",msg,e);
            throw new BsfException(msg);
        }
    }
    //执行sql
    public void streamSql(String sql,Object[] ps,ScriptObjectMirror objectMirror){
        try{
             DbHelper.call(ContextUtils.getBean(DataSource.class,false),(c)->{
                 c.executeStream(sql,ps,1000,(map)->{
                     objectMirror.call(objectMirror,map);
                 });
            });
        }catch (Exception e){
            val msg = "动态sql出错:"+sql+",参数:"+JsonUtils.serialize(ps);
            LogUtils.error(ApiScript.class,"bsf",msg,e);
            throw new BsfException(msg);
        }
    }

}
