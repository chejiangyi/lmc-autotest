package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.dao.dal.auto.tb_sample_example_base_dal;
import com.lmc.autotest.dao.model.auto.tb_log_model;
import com.lmc.autotest.dao.model.auto.tb_report_url_example_model;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class tb_sample_dal extends tb_sample_example_base_dal {
    public tb_sample_example_model get(DbConn conn, String table, Long id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from "+table+" s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }
    public List<tb_sample_example_model> searchPage(DbConn db, String table2, String sql2, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_sample_example_model>();
        String tableSql = "select * from {table} where {sql}".replace("{table}",table2).replace("{sql}",sql2);
        StringBuilder sb = new StringBuilder(" from ("+tableSql+") t ");
        String sql = "select * "+sb.toString() +" order by t.id desc "+String.format(" limit %s,%s",(pageindex-1)*pagesize,pagesize);
        //sql 安全检查
        AutoTestTool.checkSampleSelectSql(sql);
        String countSql = "select count(0) "+sb.toString();
        val ds = db.executeList(sql, par.toArray());
        if (ds != null && ds.size() > 0)
        {
            for (Map<String,Object> dr : ds)
            {
                rs.add(createModel(dr));
            }
        }
        if(totalSize!=null) {
            totalSize.setData(ConvertUtils.convert(db.executeScalar(countSql, par.toArray()), int.class));
        }
        return rs;
    }

    public List<String> tables(DbConn db){
        val par = new ArrayList<>();
        val rs = new ArrayList<String>();
        String sql = "SHOW TABLES like 'auto_tb_sample_%'";
        val ds = db.executeList(sql, par.toArray());
        if (ds != null && ds.size() > 0)
        {
            for (Map<String,Object> dr : ds)
            {
                rs.add(ConvertUtils.convert(dr.values().stream().collect(Collectors.toList()).get(0),String.class));
            }
        }
        return rs;
    }

    public String copyNewTable(DbConn conn, String name){
        conn.executeSql("CREATE TABLE "+name+" LIKE tb_sample_example",new Object[]{});
        return name;
    }

    public boolean tableIsExist(DbConn db, String name){
        String sql = "SHOW TABLES like ? ";
        val ds = db.executeList(sql, new Object[]{name});
        if (ds != null && ds.size() > 0)
        {
           return true;
        }
        return false;
    }

    public boolean batch(DbConn conn,String table, List<tb_sample_example_model> models){
        val pars = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("insert into "+table+"(url,app_name,header,body,create_time,fromip,traceid,trace_top,method,operator_type) values");
        for(val model: models){
            val par = new Object[]{
                    /***/
                    model.url,
                    /***/
                    model.app_name,
                    /***/
                    model.header,
                    /***/
                    model.body,
                    /***/
                    model.create_time,
                    /***/
                    model.fromip,
                    /***/
                    model.traceid,
                    /**是否是顶部trace*/
                    model.trace_top,
                    /***/
                    model.method,
                    /**枚举:未知,操作,仅查询*/
                    model.operator_type
            };
            for(val o:par) {
                pars.add(o);
            }
            sql.append("(?,?,?,?,?,?,?,?,?,?),");
        }
        int rev = conn.executeSql(StringUtils.trimRight(sql.toString(),',')+";", pars.toArray());
        return rev == 1;
    }

    public boolean clear(DbConn conn,String table) {
        val par = new Object[]{};
        String Sql = "truncate table "+table;
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }
}
