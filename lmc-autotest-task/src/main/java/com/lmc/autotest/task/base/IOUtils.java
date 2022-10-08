package com.lmc.autotest.task.base;

import com.free.bsf.core.config.CoreProperties;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.StringUtils;
import lombok.val;

import java.lang.management.ManagementFactory;

public class IOUtils {
    public static double cpu(){
        val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return systemBean.getProcessCpuLoad();
    }

    public static double memory(){
       // val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024;
    }

    public static double totalMemory(){
        // val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return (Runtime.getRuntime().totalMemory())/1024/1024;
    }
    public static double freeMemory(){
        // val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return (Runtime.getRuntime().freeMemory())/1024/1024;
    }
    public static double maxMemory(){
        // val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return (Runtime.getRuntime().maxMemory())/1024/1024;
    }
    public static int localCpuCores(){
       // val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return Runtime.getRuntime().availableProcessors();
    }

    public static Long LocalMemorySize(){
        val systemBean = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
        return systemBean.getTotalPhysicalMemorySize()/1024/1024;
    }

    public static int threadCount(){
//        return ManagementFactory.getThreadMXBean().getThreadCount();
        return ThreadPoolUtils.System.getThreadPool().getActiveCount();
    }

//    public static double dataCheck(String info,double value,Object[] infos){
//        if(Double.isNaN(value)||Double.isInfinite(value)){
//             LogUtils.error(IOUtils.class, CoreProperties.Project, StringUtils.nullToEmpty(info)+"数据异常:"+value+",默认重置为0处理");
//            return 0;
//        }
//        return value;
//    }

    public static double dataCheck(String info,double value){
        if(Double.isNaN(value)||Double.isInfinite(value)){
            LogUtils.error(IOUtils.class, CoreProperties.Project, StringUtils.nullToEmpty(info)+"数据异常:"+value+",默认重置为0处理");
            return 0;
        }
        return value;
    }
}
