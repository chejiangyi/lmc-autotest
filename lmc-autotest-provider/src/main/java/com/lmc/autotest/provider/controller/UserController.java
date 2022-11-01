package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_user_model;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.dao.tb_user_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.User;
import com.lmc.autotest.provider.base.Utils;
import com.lmc.autotest.provider.pager.Pager1;
import com.lmc.autotest.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        html//.s2("task", StringUtils.nullToEmpty(task)).s2("create_user", StringUtils.nullToEmpty(create_user))
                .s2("pageindex", pageindex).s2("pagesize", pagesize);
        return pageVisit((m) -> {
                    User.getCurrent().checkAdmin();
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_user_dal().searchPage(c, pageIndex2, pageSize2, totalSize);
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
            User.getCurrent().checkAdmin();
            val model = DbHelper.get(Config.mysqlDataSource(), c -> {
                return new tb_user_dal().get(c, id);
            });
            val temp = new tb_user_model();
            temp.role=0;
            temp.limit_node_count=100;
            request.setAttribute("model", model==null?temp:model);
        });
    }

    private static boolean checkAlphabet(String name) {
        char[] ch = name.toCharArray();
        for (char c : ch) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/save")
    public ModelAndView save(Integer id, String name,
                             String pwd, Integer role,
                             Integer limit_node_count
    ) {
        return jsonVisit((m) -> {
            if(!checkAlphabet(name)){
                throw new BsfException("用户名必须为字母");
            }
            User.getCurrent().checkAdmin();
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_user_model model = new tb_user_dal().get(c, id);
                if (model == null) {
                    model = new tb_user_model();
                    model.create_time = new Date();
                }else {
                }

                model.name=name;
                model.pwd=pwd;
                model.role=role;
                model.limit_node_count=limit_node_count;

                if(model.id==null||model.id==0){
                    new tb_user_dal().add(c,model);
                }else {
                    new tb_user_dal().edit(c, model);
                }
            });
            return true;
        });
    }



    @RequestMapping("/del/")
    public ModelAndView del(Integer id) {
        return jsonVisit((m) -> {
            User.getCurrent().checkAdmin();
            DbHelper.call(Config.mysqlDataSource(), c -> {
                tb_user_model model = new tb_user_dal().get(c, id);
                if(model!=null) {
                    new tb_user_dal().delete(c, id);
                }

            });
            return true;
        });
    }

    @RequestMapping("/token")
    public ModelAndView token(Integer id
    ) {
        return jsonVisit((m) -> {
            User.getCurrent().checkAdmin();
            return DbHelper.get(Config.mysqlDataSource(), c -> {
                tb_user_model model = new tb_user_dal().get(c, id);
                return Utils.token(model);
            });
        });
    }
}
