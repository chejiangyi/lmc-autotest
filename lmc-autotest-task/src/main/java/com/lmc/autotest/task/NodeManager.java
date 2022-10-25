package com.lmc.autotest.task;

import com.free.bsf.core.base.BsfException;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.service.LogTool;
import com.lmc.autotest.task.base.provider.AutoTestProvider;
import com.lmc.autotest.task.base.provider.NodeProvider;
import lombok.Getter;
import lombok.val;

import java.util.concurrent.ConcurrentHashMap;

public class NodeManager {
    public static NodeManager Default = new NodeManager();
    @Getter
    private   NodeProvider nodeProvider = new NodeProvider();
    @Getter
    private   AutoTestProvider autoTestProvider = null;
    public NodeManager(){

    }
    public void init(){
        try {
            nodeProvider.create().heartbeat();
        }catch (Exception e){
            LogTool.error(NodeManager.class, 0, Config.appName(),"节点初始化失败",e);
            throw e;
        }
    }
    public synchronized void open(Integer taskId,String tranId,Integer index,Integer userid){
        if(autoTestProvider !=null){
            throw new BsfException("任务已经在运行中");
        }
        if(this.nodeProvider.getNode_model().used==true){
            throw new BsfException("节点正在运行任务");
        }
        this.nodeProvider.updateState(true);
        AutoTestProvider t = new AutoTestProvider(taskId,tranId,nodeProvider,index, userid);
        t.init();
        autoTestProvider=t;
        t.run();
    }
    public synchronized void close(Integer taskId,String reason,Boolean isDisposed){
        if(this.autoTestProvider!=null&&this.autoTestProvider.getTaskid()==taskId) {
            if (isDisposed == false) {
                this.autoTestProvider.close(reason);
            }
            this.autoTestProvider = null;
            this.nodeProvider.updateState(false);
        }
    }
}
