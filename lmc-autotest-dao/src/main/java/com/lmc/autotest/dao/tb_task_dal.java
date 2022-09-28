package com.lmc.autotest.dao;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.dao.dal.auto.tb_task_base_dal;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.xxl.job.core.util.DateUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tb_task_dal extends tb_task_base_dal {
    public tb_task_model getWaitingTask(DbConn conn) {
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_task s where use_state='使用' and next_time<now() order by next_time asc limit 1");
        val ds = conn.executeList(stringSql.toString(), new Object[]{});
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public tb_task_model getWithLock(DbConn conn, Integer id) {
        val par = new Object[]{id};
        val stringSql = new StringBuilder();
        stringSql.append("select s.* from tb_task s where s.id=? for update");
        val ds = conn.executeList(stringSql.toString(), par);
        if (ds != null && ds.size() > 0) {
            return createModel(ds.get(0));
        }
        return null;
    }

    public void runHeartBeat(DbConn conn, int id) {
        val par = new Object[]{
                id
        };
        int rev = conn.executeSql("update tb_task set run_heart_time=now() where id=?", par);
    }

    public void closeHeartBeat(DbConn conn, int id) {
        val par = new Object[]{
                id
        };
        int rev = conn.executeSql("update tb_task set run_heart_time='1900-01-01' where id=?", par);
    }

    public void addResult(DbConn conn, int id,String reason) {
        val par = new Object[]{
                reason,id
        };
        int rev = conn.executeSql("update tb_task set exec_result=? where id=?", par);
    }

    public void appendResult(DbConn conn, int id,String reason) {
        val par = new Object[]{
                reason,id
        };
        int rev = conn.executeSql("update tb_task set exec_result=CONCAT(?,exec_result) where id=?", par);
    }

    public List<tb_task_model> searchPage(DbConn db, String task, String create_user, Integer pageindex, Integer pagesize, Ref<Integer> totalSize){
        val par = new ArrayList<>();
        val rs = new ArrayList<tb_task_model>();

        StringBuilder sb = new StringBuilder(" from tb_task where 1=1 ");
        if(!StringUtils.isEmpty(task)){
            par.add(task);
            sb.append(" and task like concat('%', ?, '%')");
        }
        if(!StringUtils.isEmpty(create_user)){
            par.add(create_user);
            sb.append(" and create_user like concat('%', ?, '%')");
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

}
