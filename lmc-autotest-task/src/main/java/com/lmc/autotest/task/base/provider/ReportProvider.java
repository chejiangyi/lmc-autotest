package com.lmc.autotest.task.base.provider;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.JsonUtils;
import com.google.common.util.concurrent.AtomicDouble;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.core.NodeInfo;
import com.lmc.autotest.dao.*;
import com.lmc.autotest.dao.model.auto.tb_report_model;
import com.lmc.autotest.dao.model.auto.tb_report_node_example_model;
import com.lmc.autotest.dao.model.auto.tb_report_url_example_model;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.task.base.FileUtils;
import com.lmc.autotest.service.HttpUtils;
import com.lmc.autotest.task.base.IOUtils;
import lombok.Getter;
import lombok.val;
import lombok.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ReportProvider {
    @Getter
    tb_report_model report_model = null;
    volatile ReportNodeInfo reportNodeInfo = null;
    volatile ReportUrlMap reportUrlMap = null;
    public void init(tb_task_model task_model,String tranId,Integer userid){
            report_model = DbHelper.get(Config.mysqlDataSource(), (c) -> {
                val taskTemp = new tb_task_dal().getWithLock(c,task_model.id);
                //lock tb_task data
                new tb_task_dal().runHeartBeat(c,task_model.id);
                var nodes = new tb_node_dal().getOnlineNodes(c);
                var nodeNames = Arrays.asList(task_model.run_nodes.split(","));
                var user = new tb_user_dal().get(c,userid);
                nodes=nodes.stream().filter(n->nodeNames.contains(n.node)).collect(Collectors.toList());
                val nodeInfos = new ArrayList<NodeInfo>();
                for(val n:nodes){
                    val ni = new NodeInfo();
                    ni.node=n.node;
                    ni.cpu=n.local_cpu;
                    ni.memory=n.local_memory;
                    ni.threads=task_model.run_threads_count;
                    nodeInfos.add(ni);
                }
                var report = new tb_report_dal().getByTaskIdWithLock(c,task_model.id,tranId);
                if(report==null) {
                    tb_report_model model = new tb_report_model();
                    model.setCreate_time(new Date());
                    model.setBegin_time(new Date());
                    model.setEnd_time(DateUtils.strToDate("1900-01-01","yyyy-MM-dd"));
                    model.setFilter_store(task_model.filter_store);
                    model.setReport_name(task_model.task+"_"+tranId);
                    model.setReport_node_table(new tb_report_node_dal().copyNewTable(c,tranId));
                    model.setReport_url_table(new tb_report_url_dal().copyNewTable(c,tranId));
                    model.setTask_id(task_model.id);
                    model.setFilter_table(FileUtils.getSampleFile(task_model.id,tranId));
                    model.setTask_name(task_model.task);
                    model.setNodes(task_model.run_nodes);
                    model.setNodes_info(JsonUtils.serialize(nodeInfos));
                    model.setTran_id(tranId);
                    model.setFilter_table_error_lines(0);
                    model.setFilter_table_lines(0);
                    model.create_user=user.name;
                    model.create_user_id=user.id;
                    new tb_report_dal().tryAdd(c,model);
                }
                report = new tb_report_dal().getByTaskIdWithLock(c,task_model.id,tranId);
                return report;
            });
            if(report_model==null)
                throw new BsfException("报表初始化失败,报表为空!");
    }

    public ReportNodeInfo updateReport(HttpUtils.HttpResponse response){
        val reportNode=reportNodeInfo;
        if(reportNode!=null) {
            if (response.isSuccess()) {
                reportNode.all_throughput.addAndGet(1);
            } else {
                reportNode.all_error.addAndGet(1);
            }
            reportNode.all_network_write.addAndGet(response.getRequestSize());
            reportNode.all_network_read.addAndGet(response.getResponseSize());
        }
        //reportNode.active_threads.

        val reportUrl=reportUrlMap;
        if(reportUrl!=null) {
            reportUrl.put(response.getRequest().getAppName() + ":" + response.getRequest().getHttpUrl(),
                    response.getRequestSize(), response.getResponseSize(), response.isSuccess(), response.getTimeMs());
        }
        return reportNode;
    }

    public ReportNodeInfo heartBeatReport(){
        if(this.reportNodeInfo!=null){
            val model = this.reportNodeInfo.toModel();
            DbHelper.call(Config.mysqlDataSource(),(c)->{
                new tb_report_node_dal().addHeartBeat(c,report_model.report_node_table,model);
            });
        }if(this.reportUrlMap!=null){
            val urls = new ArrayList<tb_report_url_example_model>();
            for(val o:this.reportUrlMap.map.values()){
                urls.add(o.toModel());
            }
            if(urls.size()>0) {
                DbHelper.call(Config.mysqlDataSource(), (c) -> {
//                for(val url:urls) {
//                    new tb_report_url_dal().addHeartBeat(c,report_model.report_url_table,url);
//                }
                    new tb_report_url_dal().addHeartBeatList(c, report_model.report_url_table, urls);
                });
            }
        }
        val returnInfo = this.reportNodeInfo;
        this.reportNodeInfo=new ReportNodeInfo();
        this.reportUrlMap=new ReportUrlMap();
        DbHelper.call(Config.mysqlDataSource(),(c)->{
            new tb_report_dal().updateEndTime(c,this.report_model.id);
        });
        return returnInfo;
    }

    public static class ReportNodeInfo{
        public String node=Config.nodeName();
        public double cpu= IOUtils.cpu();
        public double memory=IOUtils.memory();
        public AtomicDouble all_network_read=new AtomicDouble(0);
        public AtomicDouble all_network_write=new AtomicDouble(0);
        public int active_threads=IOUtils.threadCount();
        public AtomicLong all_throughput=new AtomicLong(0);
        public AtomicLong all_error=new AtomicLong(0);
        public Date create_time=new Date();

        public void refresh(){
            cpu= IOUtils.cpu();
            memory=IOUtils.memory();
            active_threads=IOUtils.threadCount();
        }

        public double getTimeSpan(){
            return ((double) new Date().getTime()-(double) create_time.getTime())/1000;
        }

        public tb_report_node_example_model toModel(){
            this.refresh();
            tb_report_node_example_model model = new tb_report_node_example_model();
            model.active_threads=this.active_threads;
            model.cpu=this.cpu;
            model.error=IOUtils.dataCheck("error",this.all_error.get()/this.getTimeSpan());
            model.memory=IOUtils.dataCheck("memory",this.memory);
            model.create_time=new Date();
            model.network_read=IOUtils.dataCheck("network_read",this.all_network_read.get()/this.getTimeSpan());
            model.network_write=IOUtils.dataCheck("network_write",this.all_network_write.get()/this.getTimeSpan());
            model.node=this.node;
            model.throughput=IOUtils.dataCheck("throughput",this.all_throughput.get()/this.getTimeSpan());
            return model;
        }
    }

    private static class ReportUrlMap{
        ConcurrentHashMap<String,ReportUrlInfo> map = new ConcurrentHashMap();
        Object lock = new Object();
        public void put(String url,double reqSize,double rpsSize,boolean isSuccess,double time){
            var info = map.get(url);
            if(info==null){
                synchronized (lock){
                    info = map.get(url);
                    if(info==null){
                        ReportUrlInfo urlInfo = new ReportUrlInfo();
                        urlInfo.url=url;
                        map.put(url,urlInfo);
                        info=urlInfo;
                    }
                }
            }
            info.all_visit_num.addAndGet(1);
            if(!isSuccess){
                info.all_error.addAndGet(1);
            }else{
                info.all_throughput.addAndGet(1);
            }
            info.all_visit_time.addAndGet(time);
            info.all_network_read.addAndGet(rpsSize);
            info.all_network_write.addAndGet(reqSize);
        }
    }

    private static class ReportUrlInfo{
        public String node=Config.nodeName();
        public String url;
        public AtomicLong all_visit_num=new AtomicLong(0);
        public AtomicLong all_throughput=new AtomicLong(0);
        public AtomicLong all_error=new AtomicLong(0);
        public AtomicDouble all_visit_time=new AtomicDouble(0);
        public AtomicDouble all_network_write=new AtomicDouble(0);
        public AtomicDouble all_network_read=new AtomicDouble(0);
        public Date create_time=new Date();


        public double getTimeSpan(){
            return ((double) new Date().getTime()-(double) create_time.getTime())/1000;
        }

        public tb_report_url_example_model toModel(){
            val o = this;
            tb_report_url_example_model model = new tb_report_url_example_model();
            model.url = o.url;
            model.create_time=new Date();
            model.network_write=IOUtils.dataCheck("network_write",o.all_network_write.get()/o.getTimeSpan());
            model.error=IOUtils.dataCheck("error",o.all_error.get()/o.getTimeSpan());
            model.network_read=IOUtils.dataCheck("network_read",o.all_network_read.get()/o.getTimeSpan());
            model.node=o.node;
            model.throughput=IOUtils.dataCheck("throughput",o.all_throughput.get()/o.getTimeSpan());
            model.visit_num=IOUtils.dataCheck("visit_num",(double)o.all_visit_num.get());
            model.visit_time=IOUtils.dataCheck("visit_time",o.all_visit_time.get()/o.all_visit_num.get()/o.getTimeSpan());

            return model;
        }

    }
}
