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
 * tb_task 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-10-21 11:42:26
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_task_base_dal {

    public boolean add(DbConn conn, tb_task_model model) {
        val par = new Object[]{
                /**任务*/
                model.task,
                /**存储引擎*/
                model.filter_store,
                /**运行时间*/
                model.run_heart_time,
                /**创建人*/
                model.create_user,
                /**创建时间*/
                model.create_time,
                /**更新时间*/
                model.update_time,
                /**更新人*/
                model.update_user,
                /**执行结果*/
                model.exec_result,
                /**过滤筛选样本脚本*/
                model.filter_script,
                /**第一次执行过滤错误脚本*/
                model.first_filter_error_script,
                /**运行线程数*/
                model.run_threads_count,
                /**运行前脚本*/
                model.http_begin_script,
                /**运行后脚本*/
                model.http_end_script,
                /**检测终止脚本*/
                model.check_stop_script,
                /**每个线程启动时的间隔时间*/
                model.sleep_time_every_thread,
                /**节点数量*/
                model.node_count,
                /**运行节点*/
                model.run_nodes,
                /**是否使用http keepalive*/
                model.use_http_keepalive,
                /**创建用户id*/
                model.create_user_id
        };
        int rev = conn.executeSql("insert into tb_task(task,filter_store,run_heart_time,create_user,create_time,update_time,update_user,exec_result,filter_script,first_filter_error_script,run_threads_count,http_begin_script,http_end_script,check_stop_script,sleep_time_every_thread,node_count,run_nodes,use_http_keepalive,create_user_id)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_task_model model) {
        val par = new Object[]{
                /**任务*/
                model.task,
                /**存储引擎*/
                model.filter_store,
                /**运行时间*/
                model.run_heart_time,
                /**创建人*/
                model.create_user,
                /**创建时间*/
                model.create_time,
                /**更新时间*/
                model.update_time,
                /**更新人*/
                model.update_user,
                /**执行结果*/
                model.exec_result,
                /**过滤筛选样本脚本*/
                model.filter_script,
                /**第一次执行过滤错误脚本*/
                model.first_filter_error_script,
                /**运行线程数*/
                model.run_threads_count,
                /**运行前脚本*/
                model.http_begin_script,
                /**运行后脚本*/
                model.http_end_script,
                /**检测终止脚本*/
                model.check_stop_script,
                /**每个线程启动时的间隔时间*/
                model.sleep_time_every_thread,
                /**节点数量*/
                model.node_count,
                /**运行节点*/
                model.run_nodes,
                /**是否使用http keepalive*/
                model.use_http_keepalive,
                /**创建用户id*/
                model.create_user_id,
                model.id
        };
        int rev = conn.executeSql("update tb_task set task=?,filter_store=?,run_heart_time=?,create_user=?,create_time=?,update_time=?,update_user=?,exec_result=?,filter_script=?,first_filter_error_script=?,run_threads_count=?,http_begin_script=?,http_end_script=?,check_stop_script=?,sleep_time_every_thread=?,node_count=?,run_nodes=?,use_http_keepalive=?,create_user_id=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_task where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_task_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_task s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_task_model> list(DbConn conn) {
        val rs = new ArrayList<tb_task_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_task s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_task_model createModel(Map<String, Object> dr) {
        val o = new tb_task_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /**任务*/
        if (dr.containsKey("task")) {
            o.task = ConvertUtils.convert(dr.get("task"), String.class);
        }
        /**存储引擎*/
        if (dr.containsKey("filter_store")) {
            o.filter_store = ConvertUtils.convert(dr.get("filter_store"), String.class);
        }
        /**运行时间*/
        if (dr.containsKey("run_heart_time")) {
            o.run_heart_time = ConvertUtils.convert(dr.get("run_heart_time"), Date.class);
        }
        /**创建人*/
        if (dr.containsKey("create_user")) {
            o.create_user = ConvertUtils.convert(dr.get("create_user"), String.class);
        }
        /**创建时间*/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        /**更新时间*/
        if (dr.containsKey("update_time")) {
            o.update_time = ConvertUtils.convert(dr.get("update_time"), Date.class);
        }
        /**更新人*/
        if (dr.containsKey("update_user")) {
            o.update_user = ConvertUtils.convert(dr.get("update_user"), String.class);
        }
        /**执行结果*/
        if (dr.containsKey("exec_result")) {
            o.exec_result = ConvertUtils.convert(dr.get("exec_result"), String.class);
        }
        /**过滤筛选样本脚本*/
        if (dr.containsKey("filter_script")) {
            o.filter_script = ConvertUtils.convert(dr.get("filter_script"), String.class);
        }
        /**第一次执行过滤错误脚本*/
        if (dr.containsKey("first_filter_error_script")) {
            o.first_filter_error_script = ConvertUtils.convert(dr.get("first_filter_error_script"), String.class);
        }
        /**运行线程数*/
        if (dr.containsKey("run_threads_count")) {
            o.run_threads_count = ConvertUtils.convert(dr.get("run_threads_count"), Integer.class);
        }
        /**运行前脚本*/
        if (dr.containsKey("http_begin_script")) {
            o.http_begin_script = ConvertUtils.convert(dr.get("http_begin_script"), String.class);
        }
        /**运行后脚本*/
        if (dr.containsKey("http_end_script")) {
            o.http_end_script = ConvertUtils.convert(dr.get("http_end_script"), String.class);
        }
        /**检测终止脚本*/
        if (dr.containsKey("check_stop_script")) {
            o.check_stop_script = ConvertUtils.convert(dr.get("check_stop_script"), String.class);
        }
        /**每个线程启动时的间隔时间*/
        if (dr.containsKey("sleep_time_every_thread")) {
            o.sleep_time_every_thread = ConvertUtils.convert(dr.get("sleep_time_every_thread"), Integer.class);
        }
        /**节点数量*/
        if (dr.containsKey("node_count")) {
            o.node_count = ConvertUtils.convert(dr.get("node_count"), Integer.class);
        }
        /**运行节点*/
        if (dr.containsKey("run_nodes")) {
            o.run_nodes = ConvertUtils.convert(dr.get("run_nodes"), String.class);
        }
        /**是否使用http keepalive*/
        if (dr.containsKey("use_http_keepalive")) {
            o.use_http_keepalive = ConvertUtils.convert(dr.get("use_http_keepalive"), Boolean.class);
        }
        /**创建用户id*/
        if (dr.containsKey("create_user_id")) {
            o.create_user_id = ConvertUtils.convert(dr.get("create_user_id"), Integer.class);
        }
        return o;
    }
}


