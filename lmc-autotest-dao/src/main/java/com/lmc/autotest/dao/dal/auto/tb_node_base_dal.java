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
 * tb_node 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-08 22:43:59
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_node_base_dal {

    public boolean add(DbConn conn, tb_node_model model) {
        val par = new Object[]{
                /***/
                model.node,
                /***/
                model.cpu,
                /***/
                model.threads,
                /***/
                model.ip,
                /***/
                model.heatbeat_time,
                /***/
                model.prot,
                /***/
                model.memory,
                /***/
                model.local_cpu,
                /***/
                model.local_memory
        };
        int rev = conn.executeSql("insert into tb_node(node,cpu,threads,ip,heatbeat_time,prot,memory,local_cpu,local_memory)" +
                "values(?,?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_node_model model) {
        val par = new Object[]{
                /***/
                model.node,
                /***/
                model.cpu,
                /***/
                model.threads,
                /***/
                model.ip,
                /***/
                model.heatbeat_time,
                /***/
                model.prot,
                /***/
                model.memory,
                /***/
                model.local_cpu,
                /***/
                model.local_memory,
                model.id
        };
        int rev = conn.executeSql("update tb_node set node=?,cpu=?,threads=?,ip=?,heatbeat_time=?,prot=?,memory=?,local_cpu=?,local_memory=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_node where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_node_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_node s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_node_model> list(DbConn conn) {
        val rs = new ArrayList<tb_node_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_node s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_node_model createModel(Map<String, Object> dr) {
        val o = new tb_node_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /***/
        if (dr.containsKey("node")) {
            o.node = ConvertUtils.convert(dr.get("node"), String.class);
        }
        /***/
        if (dr.containsKey("cpu")) {
            o.cpu = ConvertUtils.convert(dr.get("cpu"), Double.class);
        }
        /***/
        if (dr.containsKey("threads")) {
            o.threads = ConvertUtils.convert(dr.get("threads"), Integer.class);
        }
        /***/
        if (dr.containsKey("ip")) {
            o.ip = ConvertUtils.convert(dr.get("ip"), String.class);
        }
        /***/
        if (dr.containsKey("heatbeat_time")) {
            o.heatbeat_time = ConvertUtils.convert(dr.get("heatbeat_time"), Date.class);
        }
        /***/
        if (dr.containsKey("prot")) {
            o.prot = ConvertUtils.convert(dr.get("prot"), String.class);
        }
        /***/
        if (dr.containsKey("memory")) {
            o.memory = ConvertUtils.convert(dr.get("memory"), Double.class);
        }
        /***/
        if (dr.containsKey("local_cpu")) {
            o.local_cpu = ConvertUtils.convert(dr.get("local_cpu"), Integer.class);
        }
        /***/
        if (dr.containsKey("local_memory")) {
            o.local_memory = ConvertUtils.convert(dr.get("local_memory"), Integer.class);
        }
        return o;
    }
}


