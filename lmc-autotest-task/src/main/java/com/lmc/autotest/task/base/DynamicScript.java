package com.lmc.autotest.task.base;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.TimeWatchUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.service.LogTool;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

import javax.script.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 动态脚本
 */
public class DynamicScript {
    static private Cache<String, CompiledScript> cache = CacheBuilder.newBuilder().softValues().maximumSize(1000).expireAfterAccess(1, TimeUnit.MINUTES).build();
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
            LogTool.error(DynamicScript.class, Config.appName(),"动态解析脚本出错,scriptInfo:"+scriptInfo,e);
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
        val o = run("111","return 1;",null);
        val form = (LinkedHashMap)JsonUtils.deserialize("{a:1,b:'c',jsondata:[{key:'bbbb',title:'文本2',value:'o',type:'aaaa'}]}", LinkedHashMap.class);
        TimeWatchUtils.print(true,"耗时",()->{
            for(int i=0;i<10000;i++){
                val form2 = run("测试性能","var i=0;api.setDesign('文本2','11');api.log(api.getDesign('文本2'));[1,2,3].map(function(x){return x * x;});",form);
            }
        });
    }
}
