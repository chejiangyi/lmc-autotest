package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.lmc.autotest.dao.dal.auto.tb_user_base_dal;
import com.lmc.autotest.dao.model.auto.tb_user_model;
import lombok.val;

public class tb_user_dal extends tb_user_base_dal {
    public tb_user_model login(DbConn conn,String name, String pwd){
        val par = new Object[]{name,pwd};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_user s where s.name=? and s.pwd=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

}
