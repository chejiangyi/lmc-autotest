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
 * tb_job 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-10-27 12:14:07
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_job_base_dal {

    public boolean add(DbConn conn, tb_job_model model) {
        val par = new Object[]{
                /**corn表达式*/
                model.corn,
                /**脚本*/
                model.jscript,
                /***/
                model.create_time,
                /***/
                model.create_user,
                /***/
                model.create_user_id,
                /**运行状态:启用,停用*/
                model.state,
                /***/
                model.title,
                /**描述*/
                model.remark
        };
        int rev = conn.executeSql("insert into tb_job(corn,jscript,create_time,create_user,create_user_id,state,title,remark)" +
                "values(?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_job_model model) {
        val par = new Object[]{
                /**corn表达式*/
                model.corn,
                /**脚本*/
                model.jscript,
                /***/
                model.create_time,
                /***/
                model.create_user,
                /***/
                model.create_user_id,
                /**运行状态:启用,停用*/
                model.state,
                /***/
                model.title,
                /**描述*/
                model.remark,
                model.id
        };
        int rev = conn.executeSql("update tb_job set corn=?,jscript=?,create_time=?,create_user=?,create_user_id=?,state=?,title=?,remark=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_job where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_job_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_job s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_job_model> list(DbConn conn) {
        val rs = new ArrayList<tb_job_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_job s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_job_model createModel(Map<String, Object> dr) {
        val o = new tb_job_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /**corn表达式*/
        if (dr.containsKey("corn")) {
            o.corn = ConvertUtils.convert(dr.get("corn"), String.class);
        }
        /**脚本*/
        if (dr.containsKey("jscript")) {
            o.jscript = ConvertUtils.convert(dr.get("jscript"), String.class);
        }
        /***/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        /***/
        if (dr.containsKey("create_user")) {
            o.create_user = ConvertUtils.convert(dr.get("create_user"), String.class);
        }
        /***/
        if (dr.containsKey("create_user_id")) {
            o.create_user_id = ConvertUtils.convert(dr.get("create_user_id"), Integer.class);
        }
        /**运行状态:启用,停用*/
        if (dr.containsKey("state")) {
            o.state = ConvertUtils.convert(dr.get("state"), String.class);
        }
        /***/
        if (dr.containsKey("title")) {
            o.title = ConvertUtils.convert(dr.get("title"), String.class);
        }
        /**描述*/
        if (dr.containsKey("remark")) {
            o.remark = ConvertUtils.convert(dr.get("remark"), String.class);
        }
        return o;
    }
}


