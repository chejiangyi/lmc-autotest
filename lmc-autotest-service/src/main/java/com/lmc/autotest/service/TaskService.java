package com.lmc.autotest.service;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.http.HttpClient;
import com.free.bsf.core.util.*;
import com.lmc.autotest.core.ApiResponseEntity;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.dao.tb_node_dal;
import com.lmc.autotest.dao.tb_task_dal;
import lombok.val;
import lombok.var;
import org.apache.commons.compress.utils.Lists;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    //任务操作锁,未来多节点换成分布式锁
    private static Object TASK_OPERATOR_LOCK = new Object();
    public void operatorTask(DbConn c, Integer id, String todo, Integer userid, Map<String,Object> params){
        synchronized (TASK_OPERATOR_LOCK) {
            tb_task_model model = new tb_task_dal().get(c, id);
            var nodes = new tb_node_dal().getOnlineNodes(c);
            String api = "";
            if (AutoTestTool.isOnLine(model.run_heart_time) && "停止".equals(todo)) {
                api = "closetask";
                val nodeNames = Arrays.asList(StringUtils.trim(model.run_nodes, ',').split(","));
                nodes = nodes.stream().filter(n->nodeNames.contains(n.node)).collect(Collectors.toList());
            }
            if (!AutoTestTool.isOnLine(model.run_heart_time) && "运行".equals(todo)) {
                api = "opentask";
                if(model.node_count<=0){
                    throw new BsfException(String.format("当前任务未设置节点数量",nodes.size(),model.node_count));
                }
                //动态分配可用节点
                nodes = nodes.stream().filter(n->n.used==false).limit(model.node_count).collect(Collectors.toList());
                if(nodes.size()<model.node_count)
                    throw new BsfException(String.format("可用节点数不足,当前可用节点数为%s,任务所需节点数为%s",nodes.size(),model.node_count));
                new tb_task_dal().setRunNodes(c,id,String.join(",",nodes.stream().map(n->n.node).collect(Collectors.toList())));
            }
            if ("".equals(api)) {
                throw new BsfException("重复操作,任务已开启或已关闭");
            }

            //并行操作锁
            val lock = new Object();
            StringBuilder errors = new StringBuilder();
            val api2 = api;
            new tb_task_dal().addResult(c, model.id, "");
            val tranId = DateUtils.format(new Date(), "yyyy_MM_dd_HH_mm_ss");
            val nodes2=nodes;
            ThreadUtils.parallelFor("并行操作节点开关", nodes2.size(), nodes2, (n) -> {
                val index = nodes2.indexOf(n);
                val rs = HttpClientUtils.system().post("http://" + n.ip + ":" + n.port + "/" + api2 + "/",
                        HttpClient.Params.custom().add("taskId", id).add("userid",userid)
                                .add("tranId", tranId).add("index",index)
                                .add("params",JsonUtils.serialize(params)).build());
                ApiResponseEntity es = JsonUtils.deserialize(rs, ApiResponseEntity.class);
                synchronized (lock) {
                    if (es.getCode() < 0) {
                        errors.append(n.node + ":" + StringUtils.nullToEmpty(es.getMessage()) + "\r\n");
                    } else {
                    }
                }
            });

            if (!"".equals(errors.toString())) {
                throw new BsfException("执行失败：" + errors.toString());
            }
        }
    }
}
