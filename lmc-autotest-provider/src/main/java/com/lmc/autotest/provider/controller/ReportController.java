package com.lmc.autotest.provider.controller;

import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.core.NodeInfo;
import com.lmc.autotest.dao.*;
import com.lmc.autotest.dao.model.auto.tb_task_model;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.Utils;
import com.lmc.autotest.provider.pager.Pager1;
import com.xxl.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String report_name, String task_name, String create_time_from,String create_time_to, Integer pageindex, Integer pagesize) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        html.s2("report_name", StringUtils.nullToEmpty(report_name)).s2("task_name", StringUtils.nullToEmpty(task_name))
                .s2("pageindex", pageindex).s2("pagesize", pagesize)
                .s2("create_time_from",create_time_from).s2("create_time_to",create_time_to);
        return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_report_dal().searchPage(c, report_name, task_name,create_time_from,create_time_to, pageIndex2, pageSize2, totalSize);
                    });
                    new Pager1(pageIndex2, totalSize.getData()).setPageSize(pageSize2).out();
                    request.setAttribute("model", list);
                }
        );
    }
    @RequestMapping("/view")
    public ModelAndView view(Integer id) {
        html.s2("id", id);
        html.s2("checkuser",false);//报告查看无需登陆！
        return pageVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                val report = new tb_report_dal().get(c, id);
                val task = new tb_task_dal().get(c,report.task_id);
                val urlcount = new tb_report_url_dal().countUrls(c,report.report_url_table);
                val maxthroughput = new tb_report_node_dal().getMaxThroughput(c,report.getReport_node_table());
                val maxthroughputWithNoError = new tb_report_node_dal().getMaxThroughputWithNoError(c,report.getReport_node_table());


                request.setAttribute("task", task);
                request.setAttribute("model", report);
                request.setAttribute("urlcount", urlcount);
                request.setAttribute("maxthroughput", new Utils().listToMap(maxthroughput));
                request.setAttribute("maxthroughputWithNoError",new Utils().listToMap(maxthroughputWithNoError));
                request.setAttribute("nodeinfos", JsonUtils.deserialize(report.nodes_info, new ArrayList<NodeInfo>().getClass()));
            });

        });
    }

    @RequestMapping("/nodesReport")
    public ModelAndView nodesReport(Integer id,String weidu) {
        return jsonVisit((m) -> {
            return DbHelper.get(Config.mysqlDataSource(), c -> {
                val report = new tb_report_dal().get(c, id);
                val nodes = report.nodes.split(",");
                val nodesreports  = new tb_report_node_dal().nodesReport(c,report.report_node_table,weidu);
                Map<String, Object[]> nodesValues = new HashMap<>();
                for(val node2:nodes) {
                    val values = new ArrayList<>();
                    for (val o : nodesreports) {
                        if(node2.equals(o.get("node"))){
                            values.add(new Object[]{o.get("create_time"),o.get(weidu)});
                        }
                    }
                    nodesValues.put(node2, values.toArray());
                }
                Map<String,Object> nodeReport= new HashMap<>();
                nodeReport.put("nodes",nodesValues);
                return nodeReport;
            });

        });
    }

    @RequestMapping("/nodeReport")
    public ModelAndView nodeReport(Integer id,String node) {
        return jsonVisit((m) -> {
            return DbHelper.get(Config.mysqlDataSource(), c -> {
                val report = new tb_report_dal().get(c, id);
                List<Map<String,Object>>  nodesreports  = null;
                if(!StringUtils.isEmpty(node)) {
                    nodesreports = new tb_report_node_dal().nodeReport(c, node, report.report_node_table);
                }else{
                    nodesreports = new tb_report_node_dal().nodeSumReport(c, report.report_node_table);
                }
                val weidus = new String[]{"cpu","memory","active_threads","throughput","error","network_read","network_write"};
                Map<String, Object[]> nodesValues = new HashMap<>();
                for(val weidu:weidus) {
                    val values = new ArrayList<>();
                    for (val o : nodesreports) {
                        values.add(new Object[]{o.get("create_time"),o.get(weidu)});
                    }
                    nodesValues.put(weidu, values.toArray());
                }
                Map<String,Object> nodeReport= new HashMap<>();
                nodeReport.put("node",nodesValues);
                return nodeReport;
            });

        });
    }

    @RequestMapping("/urlReport")
    public ModelAndView urlReport(Integer id,String node,String order) {
        return jsonVisit((m) -> {
            return DbHelper.get(Config.mysqlDataSource(), c -> {
                val report = new tb_report_dal().get(c, id);
                List<Map<String,Object>> r = new tb_report_url_dal().nodeReport(c, node, report.report_url_table,order);
                Map<String,Object> urlreport= new HashMap<>();
                urlreport.put("report",r);
                return urlreport;
            });

        });
    }

    @RequestMapping("/urlChart")
    public ModelAndView urlChart(Integer id,String node,String url) {
        return jsonVisit((m) -> {
            return DbHelper.get(Config.mysqlDataSource(), c -> {
                val report = new tb_report_dal().get(c, id);
                List<Map<String,Object>> urlReport = new tb_report_url_dal().urlChart(c, node,url, report.report_url_table);
                //"throughput":"吞吐量/s","error":"错误量/s","network_read":"网络读/s","network_write":"网络写/s","visit_time":"耗时/s"
                val weidus = new String[]{"visit_time","throughput","error","network_read","network_write"};
                Map<String, Object[]> urlValues = new HashMap<>();
                for(val weidu:weidus) {
                    val values = new ArrayList<>();
                    for (val o : urlReport) {
                        values.add(new Object[]{o.get("create_time"),o.get(weidu)});
                    }
                    urlValues.put(weidu, values.toArray());
                }
                Map<String,Object> urlreport= new HashMap<>();
                urlreport.put("chart",urlValues);
                return urlreport;
            });

        });
    }

    @RequestMapping("/clear/")
    public ModelAndView clear(Integer saveCount) {
        return jsonVisit((m) -> {
            DbHelper.call(Config.mysqlDataSource(), c -> {
                val tasks = new tb_task_dal().list(c);
                val dal = new tb_report_dal();
                for(val task:tasks) {
                   val reports = dal.clearReport(c, task.id, saveCount==null?5:saveCount);
                   for(val report:reports) {
                       dal.dropTable(c,report.report_node_table);
                       dal.dropTable(c,report.report_url_table);
                       dal.delete(c,report.id);
                   }
                }
            });
            return true;
        });
    }
}
