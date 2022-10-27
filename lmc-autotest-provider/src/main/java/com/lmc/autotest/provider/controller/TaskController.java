package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.*;
import com.lmc.autotest.core.ApiResponseEntity;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.dao.tb_user_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.User;
import com.lmc.autotest.provider.pager.Pager1;
import com.lmc.autotest.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/task")
public class TaskController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String task, String create_user, Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        return pageVisit((m) -> {
                    val create_user2 = create_user==null?User.getCurrent().getUsername():create_user;
                    html.s2("task", StringUtils.nullToEmpty(task)).s2("create_user", create_user2).s2("pageindex", pageindex).s2("pagesize", pagesize);
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_task_dal().searchPage(c, task, create_user2, pageIndex2, pageSize2, totalSize);
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
                return new tb_task_dal().get(c, id);
            });
            val temp = new tb_task_model();temp.create_user_id=this.getUser().getUserid();
            request.setAttribute("model", model==null?temp:model);
        });
    }

    @RequestMapping("/save")
    public ModelAndView save(Integer id, String task,
                             String filter_store, String filter_script, Integer sleep_time_every_thread,
                             String first_filter_error_script, String nodes, Integer run_threads_count,
                             String http_begin_script, String http_end_script, String check_stop_script,
                             Boolean use_http_keepalive,
                             Integer node_count
    ) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                if (model == null) {
                    model = new tb_task_model();
                    model.run_heart_time = DateUtils.strToDate("1900-01-01","yyyy-MM-dd");
                    model.create_time = new Date();
                    model.run_nodes="";
                    model.exec_result="";
                    model.create_user = this.getUser().getUsername();
                    model.create_user_id=this.getUser().getUserid();
                }else {
                }
                model.first_filter_error_script = first_filter_error_script;
                model.http_begin_script = http_begin_script;
                model.check_stop_script = check_stop_script;
//                model.filter_table = filter_table;
                model.filter_script = filter_script;
                model.filter_store = filter_store;
                model.http_end_script = http_end_script;
                model.node_count = node_count;
                model.run_threads_count = run_threads_count;
                model.task = task;
                model.sleep_time_every_thread=sleep_time_every_thread;
                model.use_http_keepalive = use_http_keepalive;
                model.update_time = new Date();
                model.update_user = this.getUser().getUsername();
                if(model.id==null||model.id==0){
                    new tb_task_dal().add(c,model);
                }else {
                    new tb_task_dal().edit(c, model);
                }
            });
            return true;
        });
    }

    @RequestMapping("/setRunState/")
    public ModelAndView setRunState(Integer id,String todo) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                val user = new tb_user_dal().get(c,User.getCurrent().getUserid());
                if(model.node_count> user.limit_node_count)
                    throw new BsfException(String.format("当前任务所需节点数为%s,超过当前用户最大可调用的压测节点数%s,请调整任务可用节点数或联系管理员取消限制",model.node_count,user.limit_node_count));
                new TaskService().operatorTask(c,id,todo,user.id,null);
            });
            return true;
        });
    }

    @RequestMapping("/del/")
    public ModelAndView del(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                if (AutoTestTool.isOnLine(model.run_heart_time)) {
                    throw new BsfException("任务运行中,无法删除");
                }
                if(model!=null) {
                    new tb_task_dal().delete(c, id);
                }
            });
            return true;
        });
    }

    @RequestMapping("/copy/")
    public ModelAndView copy(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                if(model!=null) {
                    model.id=0;
                    model.task="复制-"+model.task;
                    model.create_user = this.getUser().getUsername();
                    model.create_user_id=this.getUser().getUserid();
                    model.update_time = new Date();
                    model.update_user = this.getUser().getUsername();
                    model.run_heart_time = DateUtils.strToDate("1900-01-01","yyyy-MM-dd");
                    model.exec_result="";
                    model.run_nodes="";
                    new tb_task_dal().add(c, model);
                }
            });
            return true;
        });
    }
}
