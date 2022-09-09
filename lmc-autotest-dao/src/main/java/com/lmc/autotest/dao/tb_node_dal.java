package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.db.DbHelper;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.dal.auto.tb_node_base_dal;
import com.lmc.autotest.dao.model.auto.tb_node_model;
import lombok.val;

import java.util.ArrayList;
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
            if(AutoTestTool.isOnline(o.heatbeat_time)){
                nodes.add(o);
            }
        }
        return nodes;
    }

}
