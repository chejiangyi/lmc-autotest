package com.lmc.autotest.task.base.provider;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.NetworkUtils;
import com.free.bsf.core.util.PropertyUtils;
import com.free.bsf.core.util.ThreadUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_node_model;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.service.LogTool;
import com.lmc.autotest.task.NodeManager;
import com.lmc.autotest.task.base.IOUtils;
import lombok.Getter;
import lombok.val;
import lombok.var;

import java.lang.management.ManagementFactory;
import java.util.Date;

public class NodeProvider {
    //node信息会自动更新
    @Getter
    private tb_node_model node_model = null;

    public void heartbeat(){
        ThreadUtils.system().submit("心跳更新节点",()->{
            while (!ThreadUtils.system().isShutdown()){
                try {
                   DbHelper.call(Config.mysqlDataSource(),(c)->{
                       node_model = new tb_node_dal().get(c,Config.nodeName());
                       node_model = newNode(node_model);
                       if(node_model.id==0){
                           new tb_node_dal().add(c,node_model);
                       }else {
                           new tb_node_dal().edit(c, node_model);
                       }
                    });
                    //更新当前节点可用状态(在异常情况下,保持状态最终一致)
                    checkState();
                }catch (Exception e){
                    LogTool.error(AutoTestProvider.class, 0,Config.appName(),"心跳更新节点",e);
                }
                ThreadUtils.sleep(Config.heartbeat()*1000);
            }

        });
    }
    protected Integer errorStateCheckCount=0;
    protected void checkState(){
        //更新当前节点可用状态(在异常情况下,保持状态最终一致)
        if(NodeManager.Default.getAutoTestProvider()!=null&&node_model.used==false) {
            errorStateCheckCount++;
            if(errorStateCheckCount>1){
                updateState(true);
                errorStateCheckCount=0;
                LogTool.error(AutoTestProvider.class, 0,Config.appName(),"自动重置状态",new Exception("心跳检查发现节点状态不一致,自动重置状态为使用中"));
            }
        }
        else if(NodeManager.Default.getAutoTestProvider()==null&&node_model.used==true){
            errorStateCheckCount++;
            if(errorStateCheckCount>3){
                updateState(false);
                errorStateCheckCount=0;
                LogTool.error(AutoTestProvider.class, 0,Config.appName(),"自动重置状态",new Exception("心跳检查发现节点状态不一致,自动重置状态为未使用"));
            }
        }else {
            errorStateCheckCount = 0;
        }
    }

    public void updateState(boolean used){
        try {
            DbHelper.call(Config.mysqlDataSource(), (c) -> {
                node_model = new tb_node_dal().get(c, Config.nodeName());
                if (node_model != null) {
                    node_model.used = used;
                    new tb_node_dal().updateUsed(c, node_model.id, used);
                }
            });
        }catch (Exception e){
            throw e;
        }
    }

    public NodeProvider create(){
        DbHelper.transactionGet(Config.mysqlDataSource(),(c)->{
            node_model = new tb_node_dal().get(c,Config.nodeName());
            node_model = newNode(node_model);
            if(node_model.id==0){
                new tb_node_dal().add(c,node_model);
            }
            if(node_model.heatbeat_time.getTime()>new Date().getTime()){
                throw new BsfException(Config.nodeName()+"节点已存在,启动失败");
            }
            updateState(false);//重置节点为未使用
            return true;
        });
        return this;
    }

    private tb_node_model newNode(tb_node_model old){
        var node = old;
        if(node == null) {
            node = new tb_node_model();
            node.id=0;
            node.used = false;
        }
        node.heatbeat_time=new Date();
        node.cpu= IOUtils.cpu();
        node.node = Config.nodeName();
        node.ip= NetworkUtils.getIpAddress();
        node.port= PropertyUtils.getPropertyCache("server.port","0");
        node.memory= IOUtils.memory();
        node.threads= IOUtils.threadCount();
        node.local_cpu = IOUtils.localCpuCores();
        node.local_memory=IOUtils.LocalMemorySize().intValue();
        return node;
    }
}
