package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.lmc.autotest.dao.dal.auto.tb_report_node_example_base_dal;

public class tb_report_node_dal extends tb_report_node_example_base_dal {
    public String copyNewTable(DbConn conn, String name){
        conn.executeSql("CREATE TABLE tb_report_node_"+name+" LIKE tb_report_node_example",new Object[]{});
        return "tb_report_node_"+name;
    }
}
