package com.lmc.autotest.provider.base.js;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.util.ThreadUtils;
import com.free.bsf.core.util.TimeWatchUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.service.LogTool;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 动态脚本
 */
public class DynamicScript {
    static private Cache<String, CompiledScript> cache = CacheBuilder.newBuilder().softValues().maximumSize(10).expireAfterAccess(1, TimeUnit.MINUTES).build();
    public static Object run(String scriptInfo, String script, LinkedHashMap requestJson){
        val api =  new ApiScript( scriptInfo,requestJson);
        try {
            val compile= cache.get(DigestUtils.md5Hex(script.getBytes()), () -> {
                return compile(scriptInfo,script);
            });
            val bind = new SimpleBindings();
            bind.put("api",api);
            return api.toJavaObject(compile.eval(bind));
        }catch (Exception e){
            LogTool.error(DynamicScript.class,0, Config.appName(),"定时计划解析脚本出错,scriptInfo:"+scriptInfo,e);
           // DebugUtil.webDebug(ExceptionUtils.getDetailMessage(e));
            throw new BsfException(e);
        }
    }
    private static CompiledScript compile(String scriptInfo,String script){
        try {
            //ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            ScriptEngine engine =  new NashornScriptEngineFactory().getScriptEngine(new String[]{"--language=es6"});//es6 //"--global-per-engine" 性能调优
            val script2="function dynamicRunScript(){"+script+"}dynamicRunScript()";
            return ((Compilable) engine).compile(script2);
        }catch (Exception e){
            throw new BsfException("动态编译脚本出错:"+scriptInfo,e);
        }
    }


    public static void main(String[] args) {
        for(int i=0;i<10;i++) {
            TimeWatchUtils.print(true, "睡眠", () -> {
                ThreadUtils.sleep(2000);
            });
        }
    }
}
