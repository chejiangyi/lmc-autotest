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
 * tb_report 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-11-15 18:14:44
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_report_base_dal {

    public boolean add(DbConn conn, tb_report_model model) {
        val par = new Object[]{
                /***/
                model.report_name,
                /**事务id*/
                model.tran_id,
                /***/
                model.task_id,
                /***/
                model.nodes,
                /**节点配置信息*/
                model.nodes_info,
                /***/
                model.filter_table,
                /***/
                model.filter_store,
                /**开始时间*/
                model.begin_time,
                /**结束时间*/
                model.end_time,
                /***/
                model.create_time,
                /**report_node 表*/
                model.report_node_table,
                /**report_url表*/
                model.report_url_table,
                /***/
                model.task_name,
                /***/
                model.filter_table_lines,
                /***/
                model.filter_table_error_lines,
                /**用户*/
                model.create_user,
                /**用户id*/
                model.create_user_id,
                /**备注信息*/
                model.remark
        };
        int rev = conn.executeSql("insert into tb_report(report_name,tran_id,task_id,nodes,nodes_info,filter_table,filter_store,begin_time,end_time,create_time,report_node_table,report_url_table,task_name,filter_table_lines,filter_table_error_lines,create_user,create_user_id,remark)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_report_model model) {
        val par = new Object[]{
                /***/
                model.report_name,
                /**事务id*/
                model.tran_id,
                /***/
                model.task_id,
                /***/
                model.nodes,
                /**节点配置信息*/
                model.nodes_info,
                /***/
                model.filter_table,
                /***/
                model.filter_store,
                /**开始时间*/
                model.begin_time,
                /**结束时间*/
                model.end_time,
                /***/
                model.create_time,
                /**report_node 表*/
                model.report_node_table,
                /**report_url表*/
                model.report_url_table,
                /***/
                model.task_name,
                /***/
                model.filter_table_lines,
                /***/
                model.filter_table_error_lines,
                /**用户*/
                model.create_user,
                /**用户id*/
                model.create_user_id,
                /**备注信息*/
                model.remark,
                model.id
        };
        int rev = conn.executeSql("update tb_report set report_name=?,tran_id=?,task_id=?,nodes=?,nodes_info=?,filter_table=?,filter_store=?,begin_time=?,end_time=?,create_time=?,report_node_table=?,report_url_table=?,task_name=?,filter_table_lines=?,filter_table_error_lines=?,create_user=?,create_user_id=?,remark=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_report where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_report_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_report s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_report_model> list(DbConn conn) {
        val rs = new ArrayList<tb_report_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_report s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_report_model createModel(Map<String, Object> dr) {
        val o = new tb_report_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /***/
        if (dr.containsKey("report_name")) {
            o.report_name = ConvertUtils.convert(dr.get("report_name"), String.class);
        }
        /**事务id*/
        if (dr.containsKey("tran_id")) {
            o.tran_id = ConvertUtils.convert(dr.get("tran_id"), String.class);
        }
        /***/
        if (dr.containsKey("task_id")) {
            o.task_id = ConvertUtils.convert(dr.get("task_id"), Integer.class);
        }
        /***/
        if (dr.containsKey("nodes")) {
            o.nodes = ConvertUtils.convert(dr.get("nodes"), String.class);
        }
        /**节点配置信息*/
        if (dr.containsKey("nodes_info")) {
            o.nodes_info = ConvertUtils.convert(dr.get("nodes_info"), String.class);
        }
        /***/
        if (dr.containsKey("filter_table")) {
            o.filter_table = ConvertUtils.convert(dr.get("filter_table"), String.class);
        }
        /***/
        if (dr.containsKey("filter_store")) {
            o.filter_store = ConvertUtils.convert(dr.get("filter_store"), String.class);
        }
        /**开始时间*/
        if (dr.containsKey("begin_time")) {
            o.begin_time = ConvertUtils.convert(dr.get("begin_time"), Date.class);
        }
        /**结束时间*/
        if (dr.containsKey("end_time")) {
            o.end_time = ConvertUtils.convert(dr.get("end_time"), Date.class);
        }
        /***/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        /**report_node 表*/
        if (dr.containsKey("report_node_table")) {
            o.report_node_table = ConvertUtils.convert(dr.get("report_node_table"), String.class);
        }
        /**report_url表*/
        if (dr.containsKey("report_url_table")) {
            o.report_url_table = ConvertUtils.convert(dr.get("report_url_table"), String.class);
        }
        /***/
        if (dr.containsKey("task_name")) {
            o.task_name = ConvertUtils.convert(dr.get("task_name"), String.class);
        }
        /***/
        if (dr.containsKey("filter_table_lines")) {
            o.filter_table_lines = ConvertUtils.convert(dr.get("filter_table_lines"), Integer.class);
        }
        /***/
        if (dr.containsKey("filter_table_error_lines")) {
            o.filter_table_error_lines = ConvertUtils.convert(dr.get("filter_table_error_lines"), Integer.class);
        }
        /**用户*/
        if (dr.containsKey("create_user")) {
            o.create_user = ConvertUtils.convert(dr.get("create_user"), String.class);
        }
        /**用户id*/
        if (dr.containsKey("create_user_id")) {
            o.create_user_id = ConvertUtils.convert(dr.get("create_user_id"), Integer.class);
        }
        /**备注信息*/
        if (dr.containsKey("remark")) {
            o.remark = ConvertUtils.convert(dr.get("remark"), String.class);
        }
        return o;
    }
}


