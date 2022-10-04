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
 * @since 2022-10-04 07:18:01
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
            model.memory,
            /***/
            model.threads,
            /***/
            model.ip,
            /***/
            model.heatbeat_time,
            /***/
            model.port,
            /***/
            model.local_cpu,
            /***/
            model.local_memory,
            /**节点状态 1:使用中 0:未使用*/
            model.used
        };
        int rev = conn.executeSql("insert into tb_node(node,cpu,memory,threads,ip,heatbeat_time,port,local_cpu,local_memory,used)" +
                "values(?,?,?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_node_model model) {
       val par = new Object[]{
           /***/
            model.node,
           /***/
            model.cpu,
           /***/
            model.memory,
           /***/
            model.threads,
           /***/
            model.ip,
           /***/
            model.heatbeat_time,
           /***/
            model.port,
           /***/
            model.local_cpu,
           /***/
            model.local_memory,
           /**节点状态 1:使用中 0:未使用*/
            model.used,
            model.id
        };
        int rev = conn.executeSql("update tb_node set node=?,cpu=?,memory=?,threads=?,ip=?,heatbeat_time=?,port=?,local_cpu=?,local_memory=?,used=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_node where id=?";
        int rev = conn.executeSql(Sql, par);
        if (rev == 1) {
            return true;
        } else {
            return false;
        }
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
            o.id = ConvertUtils.convert(dr.get("id"),Integer.class);
        }
        /***/
        if (dr.containsKey("node")) {
            o.node = ConvertUtils.convert(dr.get("node"),String.class);
        }
        /***/
        if (dr.containsKey("cpu")) {
            o.cpu = ConvertUtils.convert(dr.get("cpu"),Double.class);
        }
        /***/
        if (dr.containsKey("memory")) {
            o.memory = ConvertUtils.convert(dr.get("memory"),Double.class);
        }
        /***/
        if (dr.containsKey("threads")) {
            o.threads = ConvertUtils.convert(dr.get("threads"),Integer.class);
        }
        /***/
        if (dr.containsKey("ip")) {
            o.ip = ConvertUtils.convert(dr.get("ip"),String.class);
        }
        /***/
        if (dr.containsKey("heatbeat_time")) {
            o.heatbeat_time = ConvertUtils.convert(dr.get("heatbeat_time"),Date.class);
        }
        /***/
        if (dr.containsKey("port")) {
            o.port = ConvertUtils.convert(dr.get("port"),String.class);
        }
        /***/
        if (dr.containsKey("local_cpu")) {
            o.local_cpu = ConvertUtils.convert(dr.get("local_cpu"),Integer.class);
        }
        /***/
        if (dr.containsKey("local_memory")) {
            o.local_memory = ConvertUtils.convert(dr.get("local_memory"),Integer.class);
        }
        /**节点状态 1:使用中 0:未使用*/
        if (dr.containsKey("used")) {
            o.used = ConvertUtils.convert(dr.get("used"),Boolean.class);
        }
        return o;
    }
}


