package com.lmc.autotest.task.base;

import com.free.bsf.core.thread.ThreadPool;


/**
 * 自定义线程池及关键方法包装实现
 * 装饰
 * @author: chejiangyi
 * @version: 2019-07-23 20:56
 **/
public class ThreadPoolUtils {
    static {
        initSystem();
    }

    private static void initSystem() {
        System = new ThreadPool("autotest.threadPool", 0, Integer.MAX_VALUE);
    }

    public static ThreadPool System;
}