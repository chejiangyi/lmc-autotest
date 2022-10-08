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

    public boolean addHeartBeatList(DbConn conn,String table, List<tb_report_url_example_model> models) {
        val pars = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("insert into "+table+"(url,node,visit_num,throughput,error,visit_time,network_read,create_time,network_write) values");
        for(val model: models){
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
            for(val o:par) {
                pars.add(o);
            }
            sql.append("(?,?,?,?,?,?,?,?,?),");
        }
        int rev = conn.executeSql(StringUtils.trimRight(sql.toString(),',')+";", pars.toArray());
        return rev == 1;
    }

    public String copyNewTable(DbConn conn, String name){
        conn.executeSql("CREATE TABLE if not exists auto_tb_report_url_"+name+" LIKE tb_report_url_example",new Object[]{});
        return "auto_tb_report_url_"+name;
    }

    public int countUrls(DbConn conn,String table){
        val obj = conn.executeScalar("select count(distinct(url)) from {table}".replace("{table}",table),new Object[]{});
        return ConvertUtils.convert(obj,int.class);
    }

    public List<Map<String,Object>> nodeReport(DbConn conn, String node, String tableName,String order){
        val stringSql = new StringBuilder();val where = new StringBuilder();
        String lineSql = "(select visit_time from \n" +
                "(select visit_time,FLOOR(count(0) over()*{per})  as num,\n" +
                "CASE url \n" +
                "    WHEN @curUrl{num}  THEN @curRow{num} := @curRow{num} + 1 \n" +
                "    ELSE @curRow{num} := 1\n" +
                "END AS orderNo,\n" +
                "@curUrl{num} := url AS url\n" +
                "from {table} t2,(SELECT @curRow{num} := 0,@curUrl{num} := '') o where t2.url =a.url   order by visit_time) tt2\n" +
                "where tt2.num=tt2.orderNo)";
        stringSql.append("\n" +
                "select *,sum_visit_num/sum(sum_visit_num) over() as sum_visit_num_per,{98line} as jiuba_visit_time,{95line} as jiuwu_visit_time,{90line} as jiuling_visit_time from (select max(id) max_id,\n" +
                "max(visit_num) as max_visit_num,min(visit_num) as min_visit_num,avg(visit_num) as avg_visit_num,sum(visit_num) as sum_visit_num,\n" +
                "max(throughput) as max_throughput,min(throughput) as min_throughput,avg(throughput) as avg_throughput,sum(throughput) as sum_throughput,\n" +
                "max(error) as max_error,min(error) as min_error,avg(error) as avg_error,sum(error) as sum_error,\n" +
                "max(visit_time) as max_visit_time,min(visit_time) as min_visit_time,avg(visit_time) as avg_visit_time,sum(visit_time) as sum_visit_time,\n" +
                "max(network_read) as max_network_read,min(network_read) as min_network_read,avg(network_read) as avg_network_read,sum(network_read) as sum_network_read,\n" +
                "max(network_write) as max_network_write,min(network_write) as min_network_write,avg(network_write) as avg_network_write,sum(network_write) as sum_network_write\n" +
                "from {table} {where} group by url) as t, {table} a where t.max_id=a.id order by "+order+" desc");
        if(!StringUtils.isEmpty(node)){
            where.append(" where node='{node}'");
        }
        val ds = conn.executeList(stringSql.toString()
                        .replace("{90line}",lineSql.replace("{per}","0.9").replace("{num}","1"))
                        .replace("{95line}",lineSql.replace("{per}","0.95").replace("{num}","2"))
                        .replace("{98line}",lineSql.replace("{per}","0.98").replace("{num}","3"))
                        .replace("{where}",where.toString())
                        .replace("{table}",tableName)
                        .replace("{node}",StringUtils.nullToEmpty(node))
                , new Object[]{});
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
