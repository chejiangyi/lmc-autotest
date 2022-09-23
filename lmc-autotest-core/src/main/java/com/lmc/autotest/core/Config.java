package com.lmc.autotest.core;

import com.free.bsf.core.util.ContextUtils;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.PropertyUtils;
import com.free.bsf.core.util.StringUtils;
import lombok.val;

import javax.sql.DataSource;
import java.net.InetAddress;

public class Config {
    public static DataSource mysqlDataSource(){
        return ContextUtils.getBean(DataSource.class,false);
    }

    public static String appName(){
        return PropertyUtils.getPropertyCache("spring.application.name","");
    }

    public static int heartbeat(){
        return 5;
    }

    public static String nodeName(){
        val nodeName = PropertyUtils.getPropertyCache("autotest.node","");
        if(StringUtils.isEmpty(nodeName)){
            try{
                return InetAddress.getLocalHost().getHostName();
            }
            catch (Exception e){
                return "";
            }
        }
        return nodeName;
    }
}
