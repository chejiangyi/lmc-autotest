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
import com.lmc.autotest.task.base.IOUtils;
import lombok.val;
import lombok.var;

import java.lang.management.ManagementFactory;
import java.util.Date;

public class NodeProvider {
    public void heartbeat(){
        tb_node_model node = DbHelper.get(Config.mysqlDataSource(),(c)->{
            return new tb_node_dal().get(c,Config.nodeName());
        });
        ThreadUtils.system().submit("心跳更新节点",()->{
            while (!ThreadUtils.system().isShutdown()){
                try {
                  val update = newNode(node.id);
                    DbHelper.call(Config.mysqlDataSource(),(c)->{
                         new tb_node_dal().edit(c,update);
                    });
                }catch (Exception e){
                    LogTool.error(AutoTestProvider.class, Config.appName(),"心跳更新节点",e);
                }
                ThreadUtils.sleep(Config.heartbeat());
            }

        });
    }

    public NodeProvider create(){
        DbHelper.transactionGet(Config.mysqlDataSource(),(c)->{
            var node = new tb_node_dal().get(c,Config.nodeName());
            if(node ==null){
                node = newNode(0);
                new tb_node_dal().add(c,node);
            }
            if(node.heatbeat_time.getTime()>new Date().getTime()){
                throw new BsfException(Config.nodeName()+"节点已存在");
            }
            return true;
        });
        return this;
    }

    private tb_node_model newNode(int id){
        val node = new tb_node_model();
        node.id=id;
        node.heatbeat_time=new Date();
        node.cpu= IOUtils.cpu();
        node.node = Config.nodeName();
        node.ip= NetworkUtils.getIpAddress();
        node.prot= PropertyUtils.getPropertyCache("server.port","0");
        node.memery= IOUtils.memory();
        node.threads= IOUtils.threadCount();
        return node;
    }
}
