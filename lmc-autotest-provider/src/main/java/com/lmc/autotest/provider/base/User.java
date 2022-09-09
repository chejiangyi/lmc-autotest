package com.lmc.autotest.provider.base;

import com.free.bsf.core.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public  static User getCurrent()
    {
        Object u = WebUtils.getRequest().getSession().getAttribute("Current");
        if (u == null)
        {
            return null;
        }
        else
        {
            return (User)u;
        }
    }
    public static void setCurrent(User value)
    {
        WebUtils.getRequest().getSession().setAttribute("Current",value);
    }
    private String username;
}