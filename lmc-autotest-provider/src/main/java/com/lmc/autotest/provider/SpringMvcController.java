package com.lmc.autotest.provider;

import com.free.bsf.core.util.ContextUtils;
import com.free.bsf.core.util.ExceptionUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.TimeWatchUtils;
import com.lmc.autotest.core.ApiResponseEntity;
import com.lmc.autotest.provider.base.User;
import com.lmc.autotest.provider.base.Utils;
import com.lmc.autotest.provider.template.HtmlHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 扩展SpringController的实现，采用lambada表达式的方式进行类似aop的拦截
 */
@Slf4j
public class SpringMvcController {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    protected HtmlHelper html = new HtmlHelper();

    public DataSource getDataSource(){
        return ContextUtils.getBean(DataSource.class,false);
    }

    public interface IPageVisit
    {
        void invoke(ModelAndView modelAndView);
    }

    /**
     *  页面统一默认拦截器 页面模式
     */
    protected ModelAndView pageVisit(IPageVisit visit)
    {
        return TimeWatchUtils.print(true,request.getRequestURI(),()->{
            ModelAndView modelAndView = null;
            try {

                //页面模板扩展方法
                request.setAttribute("Html",html);
                request.setAttribute("Utils",new Utils());
                request.setAttribute("user", User.getCurrent());
                if(request.getAttribute("checkuser")==null||
                        ((boolean)request.getAttribute("checkuser"))==true) {
                    if (User.getCurrent() == null) {
                        return new ModelAndView("redirect:/");
                    }
                }

                //默认视图地址为/contoller name/method name,即结构对应一致
                modelAndView = new ModelAndView();
                visit.invoke(modelAndView);

                return modelAndView;
            }
            catch (Exception exp)
            {
                //默认错误拦截处理
                response.setStatus(500);
                // modelAndView = new ModelAndView("forward:/systemerror/index/");
                request.setAttribute("error", ExceptionUtils.getDetailMessage(exp));
                log.error("页面打开出错:"+request.getRequestURI(),exp);
            }
            return  modelAndView;
        });
    }


    public interface IJsonVisit
    {
        Object invoke(ModelAndView modelAndView);
    }


    /**
     * Json 统一序列化
     * @param visit
     * @return
     */
    public ModelAndView jsonVisit(IJsonVisit visit)
    {
        return TimeWatchUtils.print(true,request.getRequestURI(),()->{
            ModelAndView modelAndView = new ModelAndView("");
            //response.addHeader("Access-Control-Allow-Origin", "*");//跨域支持
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            try {
//                if(request.getAttribute("checkuser")==null||
//                        ((boolean)request.getAttribute("checkuser"))==true) {
//                    //checkUser();
//                }
                Object jsondata = visit.invoke(modelAndView);
                val data = new ApiResponseEntity(200,"",jsondata);
                String json = JsonUtils.serialize(data);
                response.getWriter().write(json);

            }
            catch (Exception exp)
            {
                //默认错误拦截处理
                //response.setStatus(500);
                ApiResponseEntity jsondata;
//                if(exp instanceof FlowException) {
//                    jsondata = ApiResponseEntity.fail(-1,((FlowException)exp).getDetailMessage());
//                }else{
                    jsondata =  new ApiResponseEntity(-1,ExceptionUtils.getDetailMessage(exp),null);
                    //ApiResponseEntity.fail(-1, ExceptionUtil.getDetailMessage(exp));
                //}
                String json = JsonUtils.serialize(jsondata);
               try{ response.getWriter().write(json);}catch (Exception e){}
                log.error("访问地址出错:"+request.getRequestURI(),exp);
            }
            modelAndView.setView(new View() {
                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

                }
            });
            return modelAndView;
        });
    }

    protected User getUser(){
        return User.getCurrent();
    }

//    protected void checkAdmin()
//    {
//        checkUser();
//        if(!new User().isAdmin())
//        {   throw new RuntimeException("无权限访问");}
//    }
//
//    protected void checkUser()
//    {
//         if(new User().getCurrent() == null)
//         {    throw new RuntimeException("用户未登陆");}
//    }
}
