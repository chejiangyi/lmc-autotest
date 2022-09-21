package com.lmc.autotest.task;

import com.free.bsf.core.base.BsfException;
import com.lmc.autotest.task.base.provider.AutoTestProvider;
import lombok.val;

import java.util.concurrent.ConcurrentHashMap;

public class AutoTestManager {
    public static AutoTestManager Default = new AutoTestManager();
    private ConcurrentHashMap<Integer, AutoTestProvider> running = new ConcurrentHashMap<>();
    private Object lock = new Object();
    public void open(Integer taskId,String tranId){
        if(running.containsKey(taskId)){
            throw new BsfException("任务已经在运行中");
        }else{
            synchronized (lock) {
                if(!running.containsKey(taskId)) {
                    AutoTestProvider autoTestProvider = new AutoTestProvider(taskId,tranId);
                    autoTestProvider.init();
                    autoTestProvider.run();
                    running.put(taskId, autoTestProvider);
                }
            }
        }
    }
    public void close(Integer taskId,String reason){
        if (running.containsKey(taskId)) {
            synchronized (lock) {
                if (running.containsKey(taskId)) {
                    val task = running.get(taskId);
                    running.remove(taskId);//先移除，否则死循环
                    task.close(reason);
                }
            }
        }
    }
}
