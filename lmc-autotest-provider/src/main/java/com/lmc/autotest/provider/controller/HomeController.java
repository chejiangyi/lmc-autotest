package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.tb_user_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController extends SpringMvcController {
    @RequestMapping("/")
    public ModelAndView loginPage()
    {
        request.setAttribute("checkuser",false);
        return pageVisit((m) -> {
            m.setViewName("/home/login");
        });
    }
    /**
     * 登录
     * */
    @RequestMapping(value="/login",method= RequestMethod.POST)
    public ModelAndView login(String username,String password)
    {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(),(c)->{
                tb_user_dal dal = new tb_user_dal();
                val user = dal.login(c,username,password);
                if(user!=null){
                    val u=new User();u.setUsername(user.name);u.setRole(user.role);u.setUserid(user.id);
                    User.setCurrent(u);
                }else{
                    throw new BsfException("登陆失败");
                }
            });
            return true;
        });
    }
    @RequestMapping("/loginout")
    public ModelAndView loginout()
    {
        User.setCurrent(null);
        return pageVisit((m) -> {
            m.setViewName("/home/login");
        });
    }
}
