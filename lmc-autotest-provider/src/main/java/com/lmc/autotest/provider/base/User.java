package com.lmc.autotest.provider.base;

import com.free.bsf.core.base.BsfException;
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
    private Integer userid;
    private Integer role;

    public String roleName(){
        return role==1?"管理员":"普通用户";
    }

    public Boolean isAdmin(){
        if(getCurrent()!=null && getCurrent().getRole()==1){
            return true;
        }else{
            return false;
        }
    }

    public Boolean isAdminOrIsUser(Integer userid){
        if(getCurrent()!=null && (isAdmin()||getCurrent().getUserid()==userid)){
            return true;
        }else{
            return false;
        }
    }

    public void checkAdmin(){
        if(!isAdmin()){
            throw new BsfException("非管理员操作");
        }
    }
}