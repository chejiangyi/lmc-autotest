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
import com.lmc.autotest.provider.SpringMvcController;
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
        html.s2("task", StringUtils.nullToEmpty(task)).s2("create_user", StringUtils.nullToEmpty(create_user)).s2("pageindex", pageindex).s2("pagesize", pagesize);
        return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_task_dal().searchPage(c, task, create_user, pageIndex2, pageSize2, totalSize);
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
            val temp = new tb_task_model();
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
                    model.exec_result="";
                }else {
                }
                model.first_filter_error_script = first_filter_error_script;
                model.http_begin_script = http_begin_script;
                model.check_stop_script = check_stop_script;
//                model.filter_table = filter_table;
                model.create_user = this.getUser().getUsername();
                model.filter_script = filter_script;
                model.filter_store = filter_store;
                model.http_end_script = http_end_script;
                model.node_count = node_count;
                model.run_threads_count = run_threads_count;
                model.update_time = new Date();
                model.task = task;
                model.update_user = this.getUser().getUsername();
                model.sleep_time_every_thread=sleep_time_every_thread;
                model.use_http_keepalive = use_http_keepalive;
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
                new TaskService().operatorTask(c,id,todo);
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
}
