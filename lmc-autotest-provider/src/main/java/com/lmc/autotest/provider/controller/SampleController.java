package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import com.lmc.autotest.dao.tb_log_dal;
import com.lmc.autotest.dao.tb_sample_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.pager.Pager1;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/sample")
public class SampleController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String table, String sql, Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        html.s2("table", StringUtils.nullToEmpty(table)).s2("sql", StringUtils.nullToEmpty(sql))
                .s2("pageindex", pageindex).s2("pagesize", pagesize);
        //.s2("create_time_from",create_time_from).s2("create_time_to",create_time_to);
        String sql2 = (StringUtils.isEmpty(sql)?"1=1":sql);
        return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    List<tb_sample_example_model> list = new ArrayList<>();
                    List<String> tables = new ArrayList<>();
                    if (table != null) {
                        list = DbHelper.get(Config.mysqlDataSource(), c -> {
                            return new tb_sample_dal().searchPage(c, table, sql2, pageIndex2, pageSize2, totalSize);
                        });
                        new Pager1(pageIndex2, totalSize.getData()).setPageSize(pageSize2).out();
                    }
                    tables = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_sample_dal().tables(c).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    });

                    request.setAttribute("model", list);
                    request.setAttribute("tables", tables);
                }
        );
    }


}
