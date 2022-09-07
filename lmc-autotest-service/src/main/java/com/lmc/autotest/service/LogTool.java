package com.lmc.autotest.service;

import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.ExceptionUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_log_model;
import com.lmc.autotest.dao.tb_log_dal;
import com.lmc.autotest.dao.tb_task_dal;
import lombok.val;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class LogTool {
    public static void info(Class cls,String project,String msg)
    {
        LogUtils.info(cls,project,msg);
        DbHelper.call(Config.mysqlDataSource(), (c) -> {
            val model = new tb_log_model();
            model.create_time=new Date();
            model.message= StringUtils.nullToEmpty(msg);
            model.node=StringUtils.nullToEmpty(Config.nodeName());
            model.type="日常";
            new tb_log_dal().add(c,model);
        });
    }

    public static void error(Class cls,String project,String msg,Throwable exp)
    {
        LogUtils.error(cls,project,msg,exp);
        DbHelper.call(Config.mysqlDataSource(), (c) -> {
            val model = new tb_log_model();
            model.create_time=new Date();
            model.message= StringUtils.nullToEmpty(msg+":"+(exp==null?"": ExceptionUtils.getDetailMessage(exp)));
            model.node=StringUtils.nullToEmpty(Config.nodeName());
            model.type="错误";
            new tb_log_dal().add(c,model);
        });
    }
}
