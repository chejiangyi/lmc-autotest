package com.lmc.autotest.provider.base;

import com.free.bsf.core.db.DbHelper;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_job_model;
import com.lmc.autotest.dao.tb_job_dal;
import lombok.val;

import java.util.HashMap;

public class JobUtils {
    public static void operatorJob(Integer id,String todo){
        DbHelper.call(Config.mysqlDataSource(), c -> {
            tb_job_model model = new tb_job_dal().get(c, id);
            val ps = new HashMap<String,Object>();ps.put("jobid",id);
            if("运行".equals(todo)) {
                QuartzManager.addJob("定时计划-" + model.id, QuartzJob.class, model.corn, ps);
                model.state="运行";
            }else{
                QuartzManager.removeJob("定时计划-" + model.id);
                model.state="停止";
            }
            new tb_job_dal().edit(c,model);
        });
    }
}
