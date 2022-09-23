package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_report_base_dal;
import com.lmc.autotest.dao.model.auto.tb_log_model;
import com.lmc.autotest.dao.model.auto.tb_report_model;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import com.xxl.job.core.util.DateUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_report_dal extends tb_report_base_dal {
    public List<tb_report_model> searchPage(DbConn db,String report_name, String task_name, String create_time_from,String create_time_to, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_report_model>();

        StringBuilder sb = new StringBuilder(" from tb_report where 1=1 ");
        if(!StringUtils.isEmpty(report_name)){
            par.add(report_name);
            sb.append(" and report_name like concat('%', ?, '%')");
        }
        if(!StringUtils.isEmpty(task_name)){
            par.add(task_name);
            sb.append(" and task_name like concat('%', ?, '%')");
        }
        if(!StringUtils.isEmpty(create_time_from)){
            par.add(DateUtil.parse(create_time_from,"yyyy-MM-dd HH:mm:ss"));
            sb.append(" and create_time_from > ?");
        }
        if(!StringUtils.isEmpty(create_time_to)){
            par.add(DateUtil.parse(create_time_to,"yyyy-MM-dd HH:mm:ss"));
            sb.append(" and create_time_to < ?");
        }

        String sql = "select * "+sb.toString() +" order by id desc "+String.format(" limit %s,%s",(pageindex-1)*pagesize,pagesize);
        String countSql = "select count(0) "+sb.toString();
        val ds = db.executeList(sql, par.toArray());
        if (ds != null && ds.size() > 0)
        {
            for (Map<String,Object> dr : ds)
            {
                rs.add(createModel(dr));
            }
        }
        totalSize.setData(ConvertUtils.convert(db.executeScalar(countSql,par.toArray()),int.class));
        return rs;
    }
    public tb_report_model getByTaskIdWithLock(DbConn conn, Object taskid,String tranId) {
        val par = new Object[]{taskid,tranId};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_report s where s.task_id=? and s.tran_id=? FOR UPDATE");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public boolean updateFilterTableInfo(DbConn conn, int id,int filter_table_lines,int filter_table_error_lines) {
        val par = new Object[]{
                filter_table_lines,
                filter_table_error_lines,
                id
        };
        int rev = conn.executeSql("update tb_report set filter_table_lines=?,filter_table_error_lines=? where id=?", par);
        return rev == 1;

    }

    public boolean updateEndTime(DbConn conn, int id) {
        val par = new Object[]{
                id
        };
        int rev = conn.executeSql("update tb_report set end_time=now() where id=?", par);
        return rev == 1;

    }
}
