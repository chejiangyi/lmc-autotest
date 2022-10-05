package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.HttpClientUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.StringUtils;
import com.free.bsf.core.util.ThreadUtils;
import com.lmc.autotest.core.ApiResponseEntity;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_log_dal;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.pager.Pager1;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/log")
public class LogController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String taskid,String node, String type,String message, String create_time_from,String create_time_to, Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        html.s2("node", StringUtils.nullToEmpty(node)).s2("taskid", StringUtils.nullToEmpty(taskid)).s2("type", StringUtils.nullToEmpty(type))
                .s2("pageindex", pageindex).s2("pagesize", pagesize).s2("message", message)
                .s2("create_time_from",create_time_from).s2("create_time_to",create_time_to);
        return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_log_dal().searchPage(c,taskid, node, type,message,create_time_from,create_time_to, pageIndex2, pageSize2, totalSize);
                    });
                    new Pager1(pageIndex2, totalSize.getData()).setPageSize(pageSize2).out();
                    request.setAttribute("model", list);
                }
        );
    }
    @RequestMapping("/clear/")
    public ModelAndView clear() {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                new tb_log_dal().clear(c);
            });
            return true;
        });
    }

}
