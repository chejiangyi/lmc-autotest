package com.lmc.autotest.provider.base;

import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.dao.tb_task_dal;
import lombok.val;

import java.util.*;

import org.quartz.CronExpression;

public class Utils {
    public String printRunState(Date date){
        if(date==null){
            return "<b style='color:red'>停止</b>";
        }
        if(isOnline(date)){
            return "<b style='color:green'>运行</b>";
        }
        return "<b style='color:red'>停止</b>";
    }

    public String showRunState(Date date){
        if(date==null){
            return "停止";
        }
        if(isOnline(date)){
            return "运行";
        }
        return "停止";
    }

    public boolean isOnline(Date heartbeatDate){
        return AutoTestTool.isOnLine(heartbeatDate);
    }

    public String[] getOnlineNodes(){
        List<String> nodes = new ArrayList<>();
        DbHelper.call(Config.mysqlDataSource(), c->{
            val list = new tb_node_dal().getOnlineNodes(c);
            for(val o:list){
                if(isOnline(o.heatbeat_time)){
                    nodes.add(o.node);
                }
            }
        });

        return nodes.toArray(new String[]{});
    }

    public boolean isContainNode(String nodes,String node){
        if(nodes == null)
            return false;
        return Arrays.asList(nodes.split(",")).contains(node);
    }

    public double subTime(Date begin,Date end){
       return (double)(end.getTime()-begin.getTime())/1000/60;
    }

    public Map listToMap(List<Map<String,Object>> list){
        Map map = new HashMap();
        for(val o:list){
            for(val kv:o.entrySet()){
                map.put(kv.getKey(),kv.getValue());
            }
        }
        return map;
    }

    public String replaceChar(String str,String replace,String to){
        return StringUtils.trim(str.replace(replace,to),'\n');
    }

    public double errorPer(double throughput,double error){
        val count = throughput+error;
        if(count==0)
            return 0;
        else
            return error/count;
    }


}
