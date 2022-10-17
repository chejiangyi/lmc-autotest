package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.dal.auto.tb_report_node_example_base_dal;
import com.lmc.autotest.dao.model.auto.tb_report_node_example_model;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_report_node_dal extends tb_report_node_example_base_dal {
    public boolean addHeartBeat(DbConn conn, String table,tb_report_node_example_model model) {
        val par = new Object[]{
                /***/
                model.node,
                /***/
                model.cpu,
                /***/
                model.network_read,
                /***/
                model.network_write,
                /***/
                model.active_threads,
                /***/
                model.throughput,
                /***/
                model.error,
                /***/
                //model.create_time,
                /***/
                model.memory
        };
        int rev = conn.executeSql("insert into "+table+"(node,cpu,network_read,network_write,active_threads,throughput,error,create_time,memory)" +
                "values(?,?,?,?,?,?,?,now(),?)", par);
        return rev == 1;
    }
    public String copyNewTable(DbConn conn, String name){
        conn.executeSql("CREATE TABLE if not exists  auto_tb_report_node_"+name+" LIKE tb_report_node_example",new Object[]{});
        return "auto_tb_report_node_"+name;
    }

    public List<Map<String,Object>> nodesReport(DbConn conn, String tableName,String weidu){
        val stringSql = new StringBuilder();
        stringSql.append("select node,{weidu},create_time from {table}".replace("{table}",tableName)
                .replace("{weidu}","cpu".equals(weidu)?"cpu*100 as cpu":weidu));
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        return ds;
    }

    public List<Map<String,Object>> nodeReport(DbConn conn, String node, String tableName){
        val stringSql = new StringBuilder();
        stringSql.append("select id, node,cpu*100 as cpu,network_read,network_write,active_threads,throughput,error,create_time,memory from {table} where node='{node}'".replace("{table}",tableName).replace("{node}",node));
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        return ds;
    }

    private String tempTable=" select batch,node,max(create_time) as create_time,max(cpu) as cpu,max(network_read) as network_read,max(network_write) as network_write,max(active_threads) as active_threads,max(throughput) as throughput,max(error) as error,max(memory) as memory from (" +
            "select *,floor((UNIX_TIMESTAMP(create_time))/{heartbeat} )as batch from {table}) as t1 GROUP BY batch,node";
    public List<Map<String,Object>> nodeSumReport(DbConn conn, String tableName){
        val stringSql = new StringBuilder();
        stringSql.append(("SELECT max(create_time) as create_time,sum(cpu)*100 as cpu,sum(network_read) as network_read,\n" +
                "                 sum(network_write) as network_write,sum(active_threads) as active_threads,sum(throughput) as throughput,\n" +
                "                 sum(error) as error,sum(memory) as memory from \n" +
                "("+tempTable+") as t GROUP BY batch")
                .replace("{heartbeat}", Config.heartbeat()+"")
                .replace("{table}",tableName));
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        return ds;
    }

    public List<Map<String,Object>> getMaxThroughputWithNoError(DbConn conn,String tableName){
        String sql = (
                "select * from (\n" +
                "SELECT max(create_time) as create_time,sum(cpu)*100 as cpu,sum(network_read) as network_read,\n" +
                        "                 sum(network_write) as network_write,sum(active_threads) as active_threads,sum(throughput) as throughput,\n" +
                        "                 sum(error) as error,sum(memory) as memory from \n" +
                        "("+tempTable+") as t GROUP BY batch) as t WHERE error=0 order by throughput desc LIMIT 0,1")
                .replace("{heartbeat}", Config.heartbeat()+"")
                .replace("{table}",tableName);
        val ds = conn.executeList(sql.toString(), new Object[]{});
        return ds;
    }

    public List<Map<String,Object>> getMaxThroughput(DbConn conn,String tableName){
        String sql = (
                "select * from (\n" +
                "SELECT max(create_time) as create_time,sum(cpu)*100 as cpu,sum(network_read) as network_read,\n" +
                "                 sum(network_write) as network_write,sum(active_threads) as active_threads,sum(throughput) as throughput,\n" +
                "                 sum(error) as error,sum(memory) as memory from \n" +
                "("+tempTable+") as t GROUP BY batch) as t order by throughput desc LIMIT 0,1")
                .replace("{heartbeat}", Config.heartbeat()+"")
                .replace("{table}",tableName);
        val ds = conn.executeList(sql.toString(), new Object[]{});
        return ds;
    }

//    public List<String> nodeReportCreateTimes(DbConn conn, String tableName){
//        val rs = new ArrayList<String>();
//        val stringSql = new StringBuilder();
//        stringSql.append(("select * from (select DATE_FORMAT(create_time, '%Y-%m-%d %H:%i') as create_time2 from {table}" +
//                " GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d %H:%i')) t order by create_time2 asc").replace("{table}",tableName));
//        val ds = conn.executeList(stringSql.toString(), new Object[]{});
//        if (ds != null && ds.size() > 0) {
//            for (Map<String, Object> dr : ds) {
//                rs.add(ConvertUtils.convert(dr.get("create_time2"),String.class));
//            }
//        }
//        return rs;
//    }
}
