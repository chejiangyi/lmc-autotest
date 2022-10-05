package com.lmc.autotest.provider.template;

import com.free.bsf.core.util.StringUtils;
import com.free.bsf.core.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 模板访问java代码的注入类
 * 一般通过Html或者tpl访问
 * 如:Html.request()或者tpl.request()
 */
public class TemplateProvider {
    public HttpServletRequest request()
    {
        return WebUtils.getRequest();
    }
    public HttpServletResponse response()
    {
        return WebUtils.getResponse();
    }
    public HttpSession session()
    {
        return WebUtils.getRequest().getSession();
    }

    /**
     * 设置参数
     * request.setAttribute(key,value)
     * @param key
     * @param value
     * @return
     */
    public void setattr(String key,Object value)
    {
        request().setAttribute(key,value);
    }
    /**
     * 获取参数
     * 默认request.getAttribute(key)
     * 支持key,也支持public字段表达式多层数据获取（通过反射深度查找）；格式: key.字段(或public).字段(或public)
     * @param key
     * @return
     */
    public Object getattr(String key)
    {
        if(key.contains("."))
        {
            Object r =null;
            String[] path = key.split(".");
            if(path != null && path.length>0) {
                for (int i = 0; i < path.length; i++) {
                    String p = path[i];
                    if (i == 0)
                    {    r = request().getAttribute(p);}
                    else {
                        try {
                            Field f = r.getClass().getDeclaredField(p);
                            f.setAccessible(true);
                            r = f.get(r);
                        } catch (Exception exp) {
                            r = null;
                        }
                    }
                    if (r == null)
                    {    break;}
                }
            }
            return r;
        }
        else
        {    return request().getAttribute(key);}
    }


    /**
     * where 三元运算符
     * @param istrue 条件bool值
     * @param trueObj true 结果
     * @param falseObj false结果
     * @return
     */
    public Object where (Boolean istrue,Object trueObj,Object falseObj)
    {
        return istrue?trueObj:falseObj;
    }

    /**
     * 截断字符串，后缀...
     * @param str 字符串
     * @param maxlen 最大长度
     * @return
     */
    public String cutstring(String str, int maxlen)
    {
        if (StringUtils.isEmpty(str))
            return str;
        if (str.length() <= maxlen)
            return str;
        return StringUtils.subString2(str,maxlen)+"...";
    }

    /**
     * 日期字符串
     * @param date
     * @param format
     * @return
     */
    public String datestring(Date date,String format)
    {
        if(date==null)
        {    return "";}
        return  new SimpleDateFormat(format).format(date);
    }

    /**
     * 默认打印
     * @param o
     * @return
     */
    public String print(Object o)
    {
        if(o==null)
        {    return "";}
        if(o instanceof Double){
            return String.format("%.2f", o);
        }
        if(o instanceof Date)
        {   return datestring((Date) o,"yyyy-MM-dd HH:mm:ss");}
        return o.toString();
    }

}
