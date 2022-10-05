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
import com.lmc.autotest.dao.model.auto.tb_node_model;
import com.lmc.autotest.dao.model.auto.tb_task_model;
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
@RequestMapping("/node")
public class NodeController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index() {
      return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_node_dal().list(c);
                    });
                    new Pager1(1, totalSize.getData()).setPageSize(100000).out();
                    request.setAttribute("model", list);
                }
        );
    }

    @RequestMapping("/del/")
    public ModelAndView del(Integer id) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_node_model model = new tb_node_dal().get(c, id);
                if (AutoTestTool.isOnLine(model.heatbeat_time)) {
                    throw new BsfException("节点运行中,无法删除");
                }
                if(model!=null) {
                    new tb_node_dal().delete(c, id);
                }
            });
            return true;
        });
    }

}
