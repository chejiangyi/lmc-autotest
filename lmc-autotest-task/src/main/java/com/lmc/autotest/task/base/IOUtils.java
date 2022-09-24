package com.lmc.autotest.task.base;

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
}
