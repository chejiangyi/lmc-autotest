package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_job_base_dal;
import com.lmc.autotest.dao.model.auto.tb_job_model;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_job_dal extends tb_job_base_dal {
    public List<tb_job_model> searchPage(DbConn db, String title, String create_user, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_job_model>();

        StringBuilder sb = new StringBuilder(" from tb_job where 1=1 ");
        if(!StringUtils.isEmpty(title)){
            par.add(title);
            sb.append(" and title like concat('%', ?, '%')");
        }
        if(!StringUtils.isEmpty(create_user)){
            par.add(create_user);
            sb.append(" and create_user like concat('%', ?, '%')");
        }

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
}
