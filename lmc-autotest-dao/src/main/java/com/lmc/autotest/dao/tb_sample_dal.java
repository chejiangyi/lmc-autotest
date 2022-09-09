package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_sample_example_base_dal;
import com.lmc.autotest.dao.model.auto.tb_log_model;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import com.xxl.job.core.util.DateUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_sample_dal extends tb_sample_example_base_dal {
    public List<tb_sample_example_model> searchPage(DbConn db, String table2, String sql2, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_sample_example_model>();
        String tableSql = "select * from {table} where {sql}".replace("{table}",table2).replace("{sql}",sql2);
        StringBuilder sb = new StringBuilder(" from ("+tableSql+") t ");
        String sql = "select * "+sb.toString() +" order by t.id desc "+String.format(" limit %s,%s",(pageindex-1)*pagesize,pagesize);
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

    public List<String> tables(DbConn db){
        val par = new ArrayList<>();
        val rs = new ArrayList<String>();
        String sql = "select TABLE_NAME from information_schema.tables where table_schema like concat('%', 'tb_sample_', '%');";
        val ds = db.executeList(sql, par.toArray());
        if (ds != null && ds.size() > 0)
        {
            for (Map<String,Object> dr : ds)
            {
                rs.add(ConvertUtils.convert(dr.get("TABLE_NAME"),String.class));
            }
        }
        return rs;
    }
}
