package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.lmc.autotest.dao.dal.auto.tb_node_base_dal;
import com.lmc.autotest.dao.model.auto.tb_node_model;
import lombok.val;

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

}
