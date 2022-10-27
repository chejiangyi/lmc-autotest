package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_job_model;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_job_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.JobUtils;
import com.lmc.autotest.provider.base.QuartzJob;
import com.lmc.autotest.provider.base.QuartzManager;
import com.lmc.autotest.provider.base.User;
import com.lmc.autotest.provider.pager.Pager1;
import com.lmc.autotest.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.CronExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/job")
public class JobController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String title, String create_user, Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        return pageVisit((m) -> {
            val create_user2 = create_user==null?User.getCurrent().getUsername():create_user;
            html.s2("title", StringUtils.nullToEmpty(title)).s2("create_user", create_user2).s2("pageindex", pageindex).s2("pagesize", pagesize);
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_job_dal().searchPage(c, title, create_user2, pageIndex2, pageSize2, totalSize);
                    });
                    new Pager1(pageIndex2, totalSize.getData()).setPageSize(pageSize2).out();
                    request.setAttribute("model", list);
                }
        );
    }

    @RequestMapping("/edit")
    public ModelAndView edit(Integer id) {
        html.s2("id", id);
        return pageVisit((m) -> {
            val model = DbHelper.get(Config.mysqlDataSource(), c -> {
                return new tb_job_dal().get(c, id);
            });
            val temp = new tb_job_model();temp.state="停止";temp.create_user_id=this.getUser().getUserid();
            request.setAttribute("model", model==null?temp:model);
        });
    }

    @RequestMapping("/save")
    public ModelAndView save(Integer id, String remark,
                             String corn, String jscript, String title
    ) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_job_model model = new tb_job_dal().get(c, id);
                if (model == null) {
                    model = new tb_job_model();
                    model.create_time = new Date();
                    model.create_user = this.getUser().getUsername();
                    model.create_user_id=this.getUser().getUserid();
                    model.state="停止";
                }else {
                }
                model.remark = remark;
                model.corn = corn;
                model.jscript = jscript;
                model.title = title;
                try {
                    CronExpression cronExpression = new CronExpression(corn);
                    cronExpression.getCronExpression();
                }catch (Exception e){
                    throw new BsfException("cron表达式格式错误!");
                }
                if(model.id==null||model.id==0){
                    new tb_job_dal().add(c,model);
                }else {
                    new tb_job_dal().edit(c,model);
                }
            });
            return true;
        });
    }

    @RequestMapping("/setState/")
    public ModelAndView setState(Integer id,String todo) {
        return jsonVisit((m) -> {
            if("停止".equals(todo)&&QuartzManager.isJobRunning(id+"")){
                throw new BsfException("当前计划正在执行中,请进行'强杀'终止");
            }
            JobUtils.operatorJob(id,todo);
            return true;
        });
    }

    @RequestMapping("/del/")
    public ModelAndView del(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_job_model model = new tb_job_dal().get(c, id);
                if ("运行".equals(model.state)) {
                    throw new BsfException("定时计划运行中,无法删除");
                }
                if(model!=null) {
                    new tb_job_dal().delete(c, id);
                }
            });
            return true;
        });
    }

    @RequestMapping("/interrupt/")
    public ModelAndView interrupt(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                JobUtils.operatorJob(id,"停止");
            });
            return true;
        });
    }

    @RequestMapping("/copy/")
    public ModelAndView copy(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_job_model model = new tb_job_dal().get(c, id);
                if(model!=null) {
                    model.id=0;
                    model.title="复制-"+model.title;
                    model.create_user = this.getUser().getUsername();
                    model.create_user_id=this.getUser().getUserid();
                    new tb_job_dal().add(c, model);
                }
            });
            return true;
        });
    }
}
