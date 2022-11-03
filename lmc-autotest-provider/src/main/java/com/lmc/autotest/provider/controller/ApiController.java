package com.lmc.autotest.provider.controller;

import com.free.bsf.autotest.base.RequestInfo;
import com.free.bsf.autotest.store.BaseStore;
import com.free.bsf.autotest.store.MysqlStore;
import com.free.bsf.autotest.store.StoreManager;
import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.ContextUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.ReflectionUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_job_dal;
import com.lmc.autotest.dao.tb_task_dal;
import com.lmc.autotest.dao.tb_user_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.User;
import com.lmc.autotest.provider.base.Utils;
import com.lmc.autotest.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class ApiController extends SpringMvcController {
    private static BaseStore store=null;
    static {
        try {
            store = StoreManager.Default.createOrGet();
        }catch (Exception e){

        }
    }

    @RequestMapping("/openTask/")
    public ModelAndView openTask(Integer id,String params ,String username,String token) {
        return jsonVisit((m) -> {
            checkToken(username,token);
            DbHelper.call(ContextUtils.getBean(DataSource.class, false), (c) -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                val user =  new tb_user_dal().get(c,username);
                if(model ==null){
                    throw new BsfException("任务id不存在:"+id);
                }
                new TaskService().operatorTask(c, id, "运行", user.id, JsonUtils.deserialize(params,new HashMap<String,Object>().getClass()));
            });
            new Utils().checkCondition(5000,()->{
                return DbHelper.get(ContextUtils.getBean(DataSource.class, false), (c) -> {
                    tb_task_model model = new tb_task_dal().get(c, id);
                    return new Utils().isOnline(model.run_heart_time);
                });
            });
            return true;
        });
    }

    @RequestMapping("/closeTask/")
    public ModelAndView closeTask(Integer id,String username,String token) {
        return jsonVisit((m) -> {
            checkToken(username,token);
            DbHelper.call(ContextUtils.getBean(DataSource.class, false), (c) -> {
                tb_task_model model = new tb_task_dal().get(c, id);
                val user =  new tb_user_dal().get(c,username);
                new TaskService().operatorTask(c, id, "停止", user.id,null);
            });
            new Utils().checkCondition(5000,()->{
                return DbHelper.get(ContextUtils.getBean(DataSource.class, false), (c) -> {
                    tb_task_model model = new tb_task_dal().get(c, id);
                    return !new Utils().isOnline(model.run_heart_time);
                });
            });
            return true;
        });
    }


    @RequestMapping("/samples/")
    public ModelAndView samples(String requests, String username, String token) {
        return jsonVisit((m) -> {
            checkToken(username,token);
            val requests2 = (RequestInfo[])JsonUtils.deserialize(requests,RequestInfo[].class);
            if(store ==null){
                throw new BsfException("未配置autotest.store.type等相关存储配置信息,请联系管理员配置");
            }

            try {
                val m2 = Arrays.stream(store.getClass().getDeclaredMethods()).filter(c->c.getName().equals("innerRun")).findFirst().get();
                m2.setAccessible(true);
                val r = m2.invoke(store, new Object[]{requests2});
            }catch (Exception e){
                throw new BsfException(e);
            }
            return true;
        });
    }

    private void checkToken(String username,String token){
        DbHelper.call(ContextUtils.getBean(DataSource.class, false), (c) -> {
            val user =  new tb_user_dal().get(c,username);
            val token1 = Utils.token(user);
            if(!token1.equals(token)|| StringUtils.isEmpty(token1)){
                throw new BsfException("token 无效或不正确,请联系管理员");
            }
        });
    }
}