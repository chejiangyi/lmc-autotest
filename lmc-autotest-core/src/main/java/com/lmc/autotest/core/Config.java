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

    //压测节点的标识名称
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

    //流每次获取数据的大小,用于采样文件生成场景
    public static Integer streamSize(){
        val streamSize = PropertyUtils.getPropertyCache("autotest.node.streamSize",10000);
        return streamSize;
    }

    //每次开启线程的最大间隔周期,用于压测时给被压测程序反应时间,单位毫秒
    public static Integer maxSleepPerTheadOpen(){
        return PropertyUtils.getPropertyCache("autotest.node.maxSleepPerThreadOpen",1000);
    }
    //http 连接池是否开启
    public static boolean httpPoolEnabled(){
        return PropertyUtils.getPropertyCache("autotest.node.httpPool.enabled",true);
    }
    //http 连接池最大大小(每个host),默认1万不做限制
    public static int httpPoolMaxSize(){
        return PropertyUtils.getPropertyCache("autotest.node.httpPool.maxSize",10000);
    }
    //http 连接池链接超时时间 单位毫秒
    public static int httpPoolConnectTimeout(){
        return PropertyUtils.getPropertyCache("autotest.node.httpPool.connectTimeout",3000);
    }
    //http 连接池访问超时时间 单位毫秒
    public static int httpPoolReadTimeout(){
        return PropertyUtils.getPropertyCache("autotest.node.httpPool.readTimeout",60000);
    }
}
