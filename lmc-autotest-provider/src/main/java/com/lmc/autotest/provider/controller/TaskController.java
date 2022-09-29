package com.lmc.autotest.provider.controller;

import com.alibaba.fastjson.TypeReference;
import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.*;
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
import com.xxl.job.core.util.DateUtil;
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
                             String filter_store, String filter_script, String filter_table,
                             String first_filter_error_script, String nodes, Integer run_threads_count,
                             String http_begin_script, String http_end_script, String check_stop_script
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
                model.nodes = nodes;
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

    @RequestMapping("/setRunState/")
    public ModelAndView setRunState(Integer id,String todo) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                String api = "";
                if (AutoTestTool.isOnLine(model.run_heart_time)&&"停止".equals(todo)) {
                    api = "closetask";
                }
                if(!AutoTestTool.isOnLine(model.run_heart_time)&&"运行".equals(todo)){
                    api = "opentask";
                }
                if("".equals(api)){
                    throw new BsfException("重复操作");
                }
                val nodeNames = Lists.newArrayList(StringUtils.trim(model.nodes, ',').split(","));
                val nodes = new tb_node_dal().getOnlineNodes(c);
                val lock = new Object();
                StringBuilder errors = new StringBuilder();
                val api2 = api;
                new tb_task_dal().addResult(c, model.id,"");
                val tranId=  DateUtil.format(new Date(),"yyyy_MM_dd_HH_mm_ss");;
                ThreadUtils.parallelFor("并行操作节点开关", nodes.size(), nodes, (n) -> {
                    if (!nodeNames.contains(n.node))
                        return;
                    val rs = HttpClientUtils.system().post("http://" + n.ip + ":" + n.port + "/" + api2 + "/",
                            HttpClient.Params.custom().add("taskId", id)
                                    .add("tranId", tranId).build());
                    ApiResponseEntity es = JsonUtils.deserialize(rs, ApiResponseEntity.class);
                    synchronized (lock) {
                        if (es.getCode() < 0 ) {
                            errors.append(n.node + ":" + StringUtils.nullToEmpty(es.getMessage() )+ "\r\n");
                        } else {
                            //errors.append(n.node + ":" +"执行成功！" + "\r\n");
                        }
                    }
                });
                if (!"".equals(errors.toString())) {
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
