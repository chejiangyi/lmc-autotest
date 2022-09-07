package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.lmc.autotest.dao.dal.auto.tb_task_base_dal;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import lombok.val;

public class tb_task_dal extends tb_task_base_dal {
    public tb_task_model getWaitingTask(DbConn conn) {
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_task s where use_state='使用' and next_time<now() order by next_time asc limit 1");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public void runHeartBeat(DbConn conn, int id) {
        val par = new Object[]{
                id
        };
        int rev = conn.executeSql("update tb_task set run_heart_time=now() where id=?", par);
    }
}
