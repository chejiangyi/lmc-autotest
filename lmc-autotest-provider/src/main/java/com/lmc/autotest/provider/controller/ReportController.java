package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.tb_report_dal;
import com.lmc.autotest.dao.tb_sample_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.pager.Pager1;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String report_name, String task_name, String create_time_from,String create_time_to, Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        html.s2("report_name", StringUtils.nullToEmpty(report_name)).s2("task_name", StringUtils.nullToEmpty(task_name))
                .s2("pageindex", pageindex).s2("pagesize", pagesize)
                .s2("create_time_from",create_time_from).s2("create_time_to",create_time_to);
        return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_report_dal().searchPage(c, report_name, task_name,create_time_from,create_time_to, pageIndex2, pageSize2, totalSize);
                    });
                    new Pager1(pageIndex2, totalSize.getData()).setPageSize(pageSize2).out();
                    request.setAttribute("model", list);
                }
        );
    }


}
