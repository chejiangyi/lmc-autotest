package com.lmc.autotest.dao;

import com.free.bsf.core.db.DbConn;
import com.lmc.autotest.dao.dal.auto.tb_report_url_example_base_dal;
import com.lmc.autotest.dao.model.auto.tb_report_url_example_model;
import lombok.val;

import java.util.List;

public class tb_report_url_dal extends tb_report_url_example_base_dal {
    public String copyNewTable(DbConn conn, String name){
        conn.executeSql("CREATE TABLE tb_report_url_"+name+" LIKE tb_report_url_example",new Object[]{});
        return "tb_report_url_"+name;
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
