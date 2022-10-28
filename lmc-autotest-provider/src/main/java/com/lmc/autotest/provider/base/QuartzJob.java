package com.lmc.autotest.provider.base;

import com.free.bsf.core.config.CoreProperties;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.ConvertUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_job_model;
import com.lmc.autotest.dao.tb_job_dal;
import com.lmc.autotest.provider.base.js.DynamicScript;
import com.lmc.autotest.service.LogTool;
import lombok.val;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QuartzJob implements Job {
    //当前正在执行的任务列表
    private static ConcurrentLinkedQueue<Integer> runningJobs = new ConcurrentLinkedQueue<>();
    public void execute(JobExecutionContext c2) throws JobExecutionException{
        Integer jobid = 0;
        val params = ConvertUtils.convert(c2.getMergedJobDataMap().get("parameterList"), Map.class);
        jobid = ConvertUtils.convert(params.get("jobid"),Integer.class);
        if(runningJobs.contains(jobid))//同一个任务不会有并发
        {
            LogTool.info(QuartzJob.class,0,CoreProperties.Project,"计划任务id:"+jobid+"正在运行中,则跳过本次执行!");
            return;
        }
        try {
            runningJobs.add(jobid);
            LogTool.info(QuartzJob.class,0,CoreProperties.Project,"计划任务id:"+jobid+"开始执行....");
            val jobid2 = jobid;
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_job_model model = new tb_job_dal().get(c, jobid2);
                val ps = new LinkedHashMap<String,Object>();ps.put("job",model);
                DynamicScript.run("计划脚本-"+jobid2.toString(),model.jscript,ps);
            });
            LogTool.info(QuartzJob.class,0,CoreProperties.Project,"计划任务id:"+jobid+"执行完毕!");
        }catch (Exception e){
            LogTool.error(QuartzJob.class,0, CoreProperties.Project,"计划任务id:"+jobid+"执行失败",e);
        }finally {
            runningJobs.remove(jobid);
        }

    }

}