package com.lmc.autotest.provider.controller;

import com.alibaba.fastjson.TypeReference;
import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.HttpClientUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.StringUtils;
import com.free.bsf.core.util.ThreadUtils;
import com.free.bsf.message.channel.QiYeWeiXinProvider;
import com.google.common.collect.Lists;
import com.lmc.autotest.core.ApiResponseEntity;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.pager.Pager1;
import com.netflix.discovery.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;

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
            val temp = new tb_task_model();temp.setClear_data_first(false);
            request.setAttribute("model", model==null?temp:model);
        });
    }

    @RequestMapping("/save")
    public ModelAndView save(Integer id, String task,
                             String corn, String filter_store, String filter_script, String filter_table,
                             boolean clear_data_first, String[] nodes, Integer run_threads_count,
                             String http_begin_script, String http_end_script, String check_stop_script
    ) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                if (model == null) {
                    model = new tb_task_model();
                    model.corn = "";
                    model.use_state = "停止";
                }
                model.clear_data_first = clear_data_first;
                model.http_begin_script = http_begin_script;
                model.run_heart_time = new Date();
                model.check_stop_script = check_stop_script;
                model.filter_table = filter_table;
                model.corn = corn;
                model.create_time = new Date();
                model.create_user = this.getUser().getUsername();
                model.filter_script = filter_script;
                model.filter_store = filter_store;
                model.http_end_script = http_end_script;
                model.next_time = AutoTestTool.cornNextTime(new Date(), corn);
                model.nodes = String.join(",", nodes==null?new String[]{}:nodes);
                model.run_threads_count = run_threads_count;
                model.update_time = new Date();
                model.task = task;
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

    @RequestMapping("/setUseState/")
    public ModelAndView setUseState(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                if (model != null) {
                    model.setUse_state(model.use_state == "禁用" ? "启用" : "禁用");
                }
                new tb_task_dal().edit(c, model);
            });
            return true;
        });
    }

    @RequestMapping("/setRunState/")
    public ModelAndView setRunState(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                String api = "openTask";
                if (AutoTestTool.isOnLine(model.run_heart_time)) {
                    api = "closeTask";
                }
                val nodeNames = Lists.newArrayList(StringUtils.trim(model.nodes, ',').split(","));
                val nodes = new tb_node_dal().getOnlineNodes(c);
                val lock = new Object();
                StringBuilder errors = new StringBuilder();
                val api2 = api;
                ThreadUtils.parallelFor("并行操作节点开关", nodes.size(), nodes, (n) -> {
                    if (!nodeNames.contains(n))
                        return;
                    val rs = HttpClientUtils.system().post("http://" + n.ip + ":" + n.prot + "/" + api2 + "/",
                            HttpClient.Params.custom().add("taskId", id).build());
                    ApiResponseEntity es = JsonUtils.deserialize(rs, ApiResponseEntity.class);
                    synchronized (lock) {
                        if (es.getCode() < 0 ) {
                            errors.append(n.node + ":" + StringUtils.nullToEmpty(es.getMessage() )+ "\r\n");
                        } else {
                            //errors.append(n.node + ":" +"执行成功！" + "\r\n");
                        }
                    }
                });
                if (errors.toString()!="") {
                    throw new BsfException("执行失败："+errors.toString());
                }
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
