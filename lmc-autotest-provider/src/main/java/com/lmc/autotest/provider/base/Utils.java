package com.lmc.autotest.provider.base;

import com.free.bsf.core.db.DbHelper;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.dao.tb_task_dal;
import lombok.val;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.quartz.CronExpression;

public class Utils {
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
}
