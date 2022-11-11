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
 * tb_config 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-11-11 15:44:11
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_config_base_dal {

    public boolean add(DbConn conn, tb_config_model model) {
        val par = new Object[]{
                /***/
                model.dic_key,
                /***/
                model.dic_value
        };
        int rev = conn.executeSql("insert into tb_config(dic_key,dic_value)" +
                "values(?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_config_model model) {
        val par = new Object[]{
                /***/
                model.dic_key,
                /***/
                model.dic_value,
                model.dic_key
        };
        int rev = conn.executeSql("update tb_config set dic_key=?,dic_value=? where dic_key=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, String dic_key) {
        val par = new Object[]{dic_key};
        String Sql = "delete from tb_config where dic_key=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_config_model get(DbConn conn, String dic_key) {
        val par = new Object[]{dic_key};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_config s where s.dic_key=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_config_model> list(DbConn conn) {
        val rs = new ArrayList<tb_config_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_config s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_config_model createModel(Map<String, Object> dr) {
        val o = new tb_config_model();
        /***/
        if (dr.containsKey("dic_key")) {
            o.dic_key = ConvertUtils.convert(dr.get("dic_key"), String.class);
        }
        /***/
        if (dr.containsKey("dic_value")) {
            o.dic_value = ConvertUtils.convert(dr.get("dic_value"), String.class);
        }
        return o;
    }
}


