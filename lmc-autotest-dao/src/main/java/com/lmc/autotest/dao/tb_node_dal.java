package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.db.DbHelper;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.dal.auto.tb_node_base_dal;
import com.lmc.autotest.dao.model.auto.tb_node_model;
import lombok.val;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class tb_node_dal extends tb_node_base_dal {
    public tb_node_model get(DbConn conn, String node) {
        val par = new Object[]{node};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_node s where s.node=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public List<tb_node_model> getOnlineNodes(DbConn conn){
        List<tb_node_model> nodes = new ArrayList<>();
        for(val o:this.list(conn)){
            if(AutoTestTool.isOnLine(o.heatbeat_time)){
                nodes.add(o);
            }
        }
        return nodes;
    }

    public List<tb_node_model> getOnlineNotUsedNodes(DbConn conn){
        List<tb_node_model> nodes = new ArrayList<>();
        for(val o:this.list(conn)){
            if(AutoTestTool.isOnLine(o.heatbeat_time)&&o.used==false){
                nodes.add(o);
            }
        }
        return nodes;
    }

    public List<tb_node_model> getClearNodes(DbConn conn){
        List<tb_node_model> nodes = new ArrayList<>();
        for(val o:this.list(conn)){
            //超过10个心跳周期,节点被清理
            if((new Date().getTime() - o.heatbeat_time.getTime())>10*Config.heartbeat()*1000){
                nodes.add(o);
            }
        }
        return nodes;
    }

    public boolean updateUsed(DbConn conn,int id,boolean used){
        val par = new Object[]{
                /**节点状态*/
                used,
                id
        };
        int rev = conn.executeSql("update tb_node set used=? where id=?", par);
        return rev == 1;
    }

}
