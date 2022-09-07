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
 * tb_log 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-07 20:31:50
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_log_base_dal {

    public boolean add(DbConn conn, tb_log_model model) {
        val par = new Object[]{
                /***/
                model.node,
                /***/
                model.type,
                /***/
                model.create_time,
                /***/
                model.message
        };
        int rev = conn.executeSql("insert into tb_log(node,type,create_time,message)" +
                "values(?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_log_model model) {
        val par = new Object[]{
                /***/
                model.node,
                /***/
                model.type,
                /***/
                model.create_time,
                /***/
                model.message,
                model.id
        };
        int rev = conn.executeSql("update tb_log set node=?,type=?,create_time=?,message=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_log where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_log_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_log s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_log_model> list(DbConn conn) {
        val rs = new ArrayList<tb_log_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_log s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_log_model createModel(Map<String, Object> dr) {
        val o = new tb_log_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /***/
        if (dr.containsKey("node")) {
            o.node = ConvertUtils.convert(dr.get("node"), String.class);
        }
        /***/
        if (dr.containsKey("type")) {
            o.type = ConvertUtils.convert(dr.get("type"), String.class);
        }
        /***/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        /***/
        if (dr.containsKey("message")) {
            o.message = ConvertUtils.convert(dr.get("message"), String.class);
        }
        return o;
    }
}


