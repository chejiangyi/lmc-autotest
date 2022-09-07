package com.lmc.autotest.core;

import com.free.bsf.core.util.ContextUtils;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.PropertyUtils;

import javax.sql.DataSource;

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
        return PropertyUtils.getPropertyCache("autotest.node","");
    }
}
