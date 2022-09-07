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
 * tb_user 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-07 20:31:51
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_user_base_dal {

    public boolean add(DbConn conn, tb_user_model model) {
        val par = new Object[]{
                /***/
                model.name,
                /***/
                model.pwd,
                /***/
                model.create_time
        };
        int rev = conn.executeSql("insert into tb_user(name,pwd,create_time)" +
                "values(?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_user_model model) {
        val par = new Object[]{
                /***/
                model.name,
                /***/
                model.pwd,
                /***/
                model.create_time,
                model.id
        };
        int rev = conn.executeSql("update tb_user set name=?,pwd=?,create_time=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_user where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_user_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_user s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_user_model> list(DbConn conn) {
        val rs = new ArrayList<tb_user_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_user s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_user_model createModel(Map<String, Object> dr) {
        val o = new tb_user_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /***/
        if (dr.containsKey("name")) {
            o.name = ConvertUtils.convert(dr.get("name"), String.class);
        }
        /***/
        if (dr.containsKey("pwd")) {
            o.pwd = ConvertUtils.convert(dr.get("pwd"), String.class);
        }
        /***/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        return o;
    }
}


