package com.lmc.autotest.provider.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Ref;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.StringUtils;
import com.free.bsf.core.util.WebUtils;
import com.lmc.autotest.core.AutoTestTool;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import com.lmc.autotest.dao.tb_log_dal;
import com.lmc.autotest.dao.tb_sample_dal;
import com.lmc.autotest.provider.SpringMvcController;
import com.lmc.autotest.provider.base.User;
import com.lmc.autotest.provider.pager.Pager1;
import com.lmc.autotest.service.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/sample")
public class SampleController extends SpringMvcController {
    @RequestMapping("/index")
    public ModelAndView index(String table, String sql, Integer pageindex, Integer pagesize, Boolean download) {
        val pageIndex2 = (pageindex == null ? 1 : pageindex);
        val pageSize2 = (pagesize == null ? 10 : pagesize);
        html.s2("table", StringUtils.nullToEmpty(table)).s2("sql", StringUtils.nullToEmpty(sql))
                .s2("pageindex", pageindex).s2("pagesize", pagesize);
        //.s2("create_time_from",create_time_from).s2("create_time_to",create_time_to);
        String sql2 = (StringUtils.isEmpty(sql)?"1=1":sql);
        if(download!=null&&download==true){
            String fileName = UUID.randomUUID().toString().replace("-","")+"-"+table+".xlsx";
            val file =new File(fileName);
            try {
                // ?????? ????????????????????????class??????
                try (ExcelWriter excelWriter = EasyExcel.write(fileName, tb_sample_example_model.class).build()) {
                    // ???????????? ???????????????sheet??????????????????
                    WriteSheet writeSheet = EasyExcel.writerSheet().build();
                    // ???????????????,?????????????????????????????????????????????????????????????????????????????????
                    for (int i = 1; i < 100; i++) {
                        val i2=i;
                        val list = DbHelper.get(Config.mysqlDataSource(), c -> {
                            return new tb_sample_dal().searchPage(c, table, sql2, i2, 2000, null);
                        });
                        if(list.size()>0) {
                            excelWriter.write(list, writeSheet);
                        }else{
                            break;
                        }
                    }
                }
                AutoTestTool.downLoad(WebUtils.getResponse(),file,table+".xlsx");
            }catch (Exception exp){
                throw new BsfException("????????????",exp);
            }finally {
                file.delete();
            }
            return pageVisit((m) -> {
            });
        }
        return pageVisit((m) -> {
                    Ref<Integer> totalSize = new Ref<>(0);
                    List<tb_sample_example_model> list = new ArrayList<>();
                    List<String> tables = new ArrayList<>();
                    if (table != null) {
                        list = DbHelper.get(Config.mysqlDataSource(), c -> {
                            return new tb_sample_dal().searchPage(c, table, sql2, pageIndex2, pageSize2, totalSize);
                        });
                        new Pager1(pageIndex2, totalSize.getData()).setPageSize(pageSize2).out();
                    }
                    tables = DbHelper.get(Config.mysqlDataSource(), c -> {
                        return new tb_sample_dal().tables(c).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    });

                    tables = tables.stream().filter(t->
                            User.getCurrent().isAdmin()||
                            DateUtils.strToDate(t.replace("auto_tb_sample_",""),"yyyy_MM_dd")!=null||
                            t.equalsIgnoreCase("auto_tb_sample_"+User.getCurrent().getUsername())).collect(Collectors.toList());

                    request.setAttribute("model", list);
                    request.setAttribute("tables", tables);
                }
        );
    }
    @RequestMapping("/import")
    public ModelAndView importFile(@RequestPart("file") MultipartFile file) {
        return this.jsonVisit((m)->{
            try {
                if(User.getCurrent()==null){
                    throw new BsfException("??????????????????");
                }
                String username = User.getCurrent().getUsername();

                File file2 = new File(UUID.randomUUID().toString().replace("-", "") + "-temp.xlsx");
                try {
                    file.transferTo(file2.getAbsoluteFile());
                    // ?????? ????????????????????????class??????????????????????????????sheet ????????????????????????
                    // ?????????????????????100????????? ?????????????????? ??????????????????????????????
                    val dal = new tb_sample_dal();
                    DbHelper.call(Config.mysqlDataSource(), c -> {
                        String table = "auto_tb_sample_" + username;
                        if (!dal.tableIsExist(c, table)) {
                            dal.copyNewTable(c, table);
                        }
                        EasyExcel.read(file2.getAbsolutePath(), tb_sample_example_model.class, new PageReadListener<tb_sample_example_model>(dataList -> {
                            for(val d:dataList) {
                                AutoTestTool.setObjectNullToEmpty(d);
                            }
                            dal.batch(c, table, dataList);
                        })).sheet().doRead();
                    });
                }finally {
                    if(file2.exists()){
                        file2.delete();
                    }
                }
                return true;
            }catch (Exception e){
                throw new BsfException(e);
            }
        });
    }

    @RequestMapping("/clearmy/")
    public ModelAndView clearMy() {
        return jsonVisit((m) -> {
            if(User.getCurrent()==null){
                throw new BsfException("??????????????????");
            }
            String username = User.getCurrent().getUsername();
            String table = "auto_tb_sample_"+username;
            DbHelper.call(Config.mysqlDataSource(), c -> {
                new tb_sample_dal().clear(c,table);
            });
            return true;
        });
    }

    @RequestMapping("/clearsample/")
    public ModelAndView clearSample() {
        return jsonVisit((m) -> {
            val tables = DbHelper.get(Config.mysqlDataSource(), c -> {
                return new tb_sample_dal().tables(c).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            });
            for(val table : tables){
                val time = table.replace("auto_tb_sample_","");
                val date = DateUtils.strToDate(time,"yyyy_MM_dd");
                if(date!=null && date.getTime()<(new Date().getTime()-60*1000*60*24*7*2)) {
                    DbHelper.call(Config.mysqlDataSource(), c -> {
                        new tb_sample_dal().clear(c, table);
                    });
                }
            }
            return true;
        });
    }

    @RequestMapping("/check/")
    public ModelAndView check(String table,Long id) {
        return jsonVisit((m) -> {
            val sample = DbHelper.get(Config.mysqlDataSource(), c -> {
                return new tb_sample_dal().get(c,table,id);
            });
            val response = HttpUtils.request(sample,false,false);
            if(response.getCode()==200) {
                return response.getBody();
            }else{
                throw new BsfException("?????????:"+response.getCode());
            }
        });
    }
}
