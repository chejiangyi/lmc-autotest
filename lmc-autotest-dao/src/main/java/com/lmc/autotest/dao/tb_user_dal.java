package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_user_base_dal;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.model.auto.tb_user_model;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_user_dal extends tb_user_base_dal {
    public List<tb_user_model> searchPage(DbConn db, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_user_model>();

        StringBuilder sb = new StringBuilder(" from tb_user where 1=1 ");

        String sql = "select * "+sb.toString() +" order by id desc "+String.format(" limit %s,%s",(pageindex-1)*pagesize,pagesize);
        String countSql = "select count(0) "+sb.toString();
        val ds = db.executeList(sql, par.toArray());
        if (ds != null && ds.size() > 0)
        {
            for (Map<String,Object> dr : ds)
            {
                rs.add(createModel(dr));
            }
        }
        totalSize.setData(ConvertUtils.convert(db.executeScalar(countSql,par.toArray()),int.class));
        return rs;
    }
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
