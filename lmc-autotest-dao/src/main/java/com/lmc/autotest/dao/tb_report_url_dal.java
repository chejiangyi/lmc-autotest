package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_report_url_example_base_dal;
import com.lmc.autotest.dao.model.auto.tb_report_url_example_model;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_report_url_dal extends tb_report_url_example_base_dal {
    public boolean addHeartBeat(DbConn conn,String table, tb_report_url_example_model model) {
        val par = new Object[]{
                /***/
                model.url,
                /***/
                model.node,
                /**访问次数*/
                model.visit_num,
                /**吞吐量/s*/
                model.throughput,
                /**错误量/s*/
                model.error,
                /**avg 访问耗时/s */
                model.visit_time,
                /**网络读/s*/
                model.network_read,
                /***/
                model.create_time,
                /**网络写/s*/
                model.network_write
        };
        int rev = conn.executeSql("insert into "+table+"(url,node,visit_num,throughput,error,visit_time,network_read,create_time,network_write)" +
                "values(?,?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public String copyNewTable(DbConn conn, String name){
        conn.executeSql("CREATE TABLE auto_tb_report_url_"+name+" LIKE tb_report_url_example",new Object[]{});
        return "auto_tb_report_url_"+name;
    }

    public int countUrls(DbConn conn,String table){
        val obj = conn.executeScalar("select count(distinct(url)) from {table}".replace("{table}",table),new Object[]{});
        return ConvertUtils.convert(obj,int.class);
    }

    public List<Map<String,Object>> nodeReport(DbConn conn, String node, String tableName){
        val stringSql = new StringBuilder();
        stringSql.append(
                "select url,sum(visit_num) as all_visit_num,max(throughput) as max_throughput,max(error) as max_error,min(visit_time) as min_visit_time,max(visit_time) as max_visit_time,\n" +
                "avg(visit_time) as avg_visit_time,max(network_read) as max_network_read,max(network_write) as max_network_write\n" +
                "from {table}");
        if(!StringUtils.isEmpty(node)){
            stringSql.append(" where node='{node}'");
        }
        stringSql.append(" group by url order by url");
        val ds = conn.executeList(stringSql.toString().replace("{table}",tableName).replace("{node}",StringUtils.nullToEmpty(node)), new Object[]{});
        return ds;
    }

    public List<Map<String,Object>> urlChart(DbConn conn, String node, String url, String tableName){
        val stringSql = new StringBuilder();
        val par = new ArrayList();
        stringSql.append(
                "select * from {table} where 1=1");
        if(!StringUtils.isEmpty(node)){
            par.add(node);
            stringSql.append(" and node=?");
        }
        if(!StringUtils.isEmpty(url)){
            par.add(url);
            stringSql.append(" and url=?");
        }
        //stringSql.append(" group by url order by url");
        val ds = conn.executeList(stringSql.toString().replace("{table}",tableName), par.toArray());
        return ds;
    }

    public void batch(DbConn conn, List<tb_report_url_example_model> models){
        try {
            conn.beginTransaction(1);
            val ps = new Object[models.size()][];
            for(int i=0;i<ps.length;i++) {
                val model = models.get(i);
                ps[i]=new Object[]{
                        /***/
                        model.url,
                        /***/
                        model.node,
                        /**访问次数*/
                        model.visit_num,
                        /**吞吐量/s*/
                        model.throughput,
                        /**错误量/s*/
                        model.error,
                        /**avg 访问耗时/s */
                        model.visit_time,
                        /**网络读/s*/
                        model.network_read,
                        /***/
                        model.create_time,
                        /**网络写/s*/
                        model.network_write
                };
            }
            conn.batch("insert into tb_report_url_example(url,node,visit_num,throughput,error,visit_time,network_read,create_time,network_write)" +
                    "values(?,?,?,?,?,?,?,?,?)", ps);
            conn.commit();
        }catch (Exception e){
            conn.rollback();
            throw e;
        }
    }
}
