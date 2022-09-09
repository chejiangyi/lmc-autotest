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
 * tb_report_url_example 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-08 22:44:00
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_report_url_example_base_dal {

    public boolean add(DbConn conn, tb_report_url_example_model model) {
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
        int rev = conn.executeSql("insert into tb_report_url_example(url,node,visit_num,throughput,error,visit_time,network_read,create_time,network_write)" +
                "values(?,?,?,?,?,?,?,?,?)", par);
        return rev == 1;
    }

    public boolean edit(DbConn conn, tb_report_url_example_model model) {
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
                model.network_write,
                model.id
        };
        int rev = conn.executeSql("update tb_report_url_example set url=?,node=?,visit_num=?,throughput=?,error=?,visit_time=?,network_read=?,create_time=?,network_write=? where id=?", par);
        return rev == 1;

    }

    public boolean delete(DbConn conn, Integer id) {
        val par = new Object[]{id};
        String Sql = "delete from tb_report_url_example where id=?";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }

    public tb_report_url_example_model get(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_report_url_example s where s.id=?");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public ArrayList<tb_report_url_example_model> list(DbConn conn) {
        val rs = new ArrayList<tb_report_url_example_model>();
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_report_url_example s ");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            for (Map<String, Object> dr : ds) {
                rs.add(createModel(dr));
            }
        }
        return rs;
    }

    public tb_report_url_example_model createModel(Map<String, Object> dr) {
        val o = new tb_report_url_example_model();
        /***/
        if (dr.containsKey("id")) {
            o.id = ConvertUtils.convert(dr.get("id"), Integer.class);
        }
        /***/
        if (dr.containsKey("url")) {
            o.url = ConvertUtils.convert(dr.get("url"), String.class);
        }
        /***/
        if (dr.containsKey("node")) {
            o.node = ConvertUtils.convert(dr.get("node"), String.class);
        }
        /**访问次数*/
        if (dr.containsKey("visit_num")) {
            o.visit_num = ConvertUtils.convert(dr.get("visit_num"), Double.class);
        }
        /**吞吐量/s*/
        if (dr.containsKey("throughput")) {
            o.throughput = ConvertUtils.convert(dr.get("throughput"), Double.class);
        }
        /**错误量/s*/
        if (dr.containsKey("error")) {
            o.error = ConvertUtils.convert(dr.get("error"), Double.class);
        }
        /**avg 访问耗时/s */
        if (dr.containsKey("visit_time")) {
            o.visit_time = ConvertUtils.convert(dr.get("visit_time"), Double.class);
        }
        /**网络读/s*/
        if (dr.containsKey("network_read")) {
            o.network_read = ConvertUtils.convert(dr.get("network_read"), Double.class);
        }
        /***/
        if (dr.containsKey("create_time")) {
            o.create_time = ConvertUtils.convert(dr.get("create_time"), Date.class);
        }
        /**网络写/s*/
        if (dr.containsKey("network_write")) {
            o.network_write = ConvertUtils.convert(dr.get("network_write"), Double.class);
        }
        return o;
    }
}


