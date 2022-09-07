package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.lmc.autotest.dao.dal.auto.tb_report_base_dal;
import com.lmc.autotest.dao.model.auto.tb_report_model;
import lombok.val;

public class tb_report_dal extends tb_report_base_dal {
    public tb_report_model getByTaskIdWithLock(DbConn conn, Object taskid) {
        val par = new Object[]{taskid};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_report s where s.taskid=? FOR UPDATE");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }
}
