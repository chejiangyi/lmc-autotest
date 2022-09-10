package com.lmc.autotest.dao.dal.auto;


import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.db.DbConn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.*;

import java.util.Date;

import com.lmc.autotest.dao.model.auto.*;

/**
 * tb_sample_example 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-09 15:46:21
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_sample_example_base_dal {

    public boolean add(DbConn conn, tb_sample_example_model model) {
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
                model.traceId,
                /***/
                model.method
        };
        int rev = conn.executeSql("insert into tb_sample_example(url,app_name,header,body,create_time,fromip,traceId,method)" +
                "values(?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_sample_example_model model) {
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
                model.traceId,
                /***/
                model.method,
                model.id
        };
        int rev = conn.executeSql("update tb_sample_example set url=?,app_name=?,header=?,body=?,create_time=?,fromip=?,traceId=?,method=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Long id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_sample_example where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_sample_example_model get(DbConn conn, Long id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_sample_example s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_sample_example_model> list(DbConn conn) {
        val rs = new ArrayList<tb_sample_example_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_sample_example s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_sample_example_model createModel(Map<String, Object> dr) {
        val o = new tb_sample_example_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Long.class);
        }
        /***/
        if (dr.containsKey("url")) {
            o.url = ConvertUtils.convert(dr.get("url"), String.class);
        }
        /***/
        if (dr.containsKey("app_name")) {
            o.app_name = ConvertUtils.convert(dr.get("app_name"), String.class);
        }
        /***/
        if (dr.containsKey("header")) {
            o.header = ConvertUtils.convert(dr.get("header"), String.class);
        }
        /***/
        if (dr.containsKey("body")) {
            o.body = ConvertUtils.convert(dr.get("body"), String.class);
        }
        /***/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        /***/
        if (dr.containsKey("fromip")) {
            o.fromip = ConvertUtils.convert(dr.get("fromip"), String.class);
        }
        /***/
        if (dr.containsKey("traceId")) {
            o.traceId = ConvertUtils.convert(dr.get("traceId"), String.class);
        }
        /***/
        if (dr.containsKey("method")) {
            o.method = ConvertUtils.convert(dr.get("method"), String.class);
        }
        return o;
    }
}


