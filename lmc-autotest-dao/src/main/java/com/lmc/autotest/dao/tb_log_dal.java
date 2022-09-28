package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_log_base_dal;
import com.lmc.autotest.dao.model.auto.tb_log_model;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.xxl.job.core.util.DateUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_log_dal extends tb_log_base_dal {
    public List<tb_log_model> searchPage(DbConn db,String taskid,String node, String type,String message, String create_time_from,String create_time_to, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_log_model>();

        StringBuilder sb = new StringBuilder(" from tb_log where 1=1 ");
        if(!StringUtils.isEmpty(taskid)){
            par.add(taskid);
            sb.append(" and task_id = ?");
        }
        if(!StringUtils.isEmpty(node)){
            par.add(node);
            sb.append(" and node like concat('%', ?, '%')");
        }
        if(!StringUtils.isEmpty(message)){
            par.add(message);
            sb.append(" and message like concat('%', ?, '%')");
        }
        if(!StringUtils.isEmpty(type)){
            par.add(type);
            sb.append(" and type = ?");
        }
        if(!StringUtils.isEmpty(create_time_from)){
            par.add(DateUtil.parse(create_time_from,"yyyy-MM-dd HH:mm:ss"));
            sb.append(" and create_time > ?");
        }
        if(!StringUtils.isEmpty(create_time_to)){
            par.add(DateUtil.parse(create_time_to,"yyyy-MM-dd HH:mm:ss"));
            sb.append(" and create_time < ?");
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

    public boolean clear(DbConn conn) {
        val par = new Object[]{};
        String Sql = "truncate table tb_log";
        int rev = conn.executeSql(Sql, par);
        return rev == 1;
    }
}
