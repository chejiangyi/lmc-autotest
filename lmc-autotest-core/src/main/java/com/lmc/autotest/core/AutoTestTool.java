package com.lmc.autotest.core;

import com.free.bsf.core.base.BsfException;
import lombok.val;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

public class AutoTestTool {
    public static boolean isOnLine(Date date){
        val newDate = DateUtils.addSeconds(date,Config.heartbeat());
        if(newDate.getTime()>new Date().getTime()){
            return true;
        }else
            return false;
    }

    public static Date cornNextTime(Date date, String cron)  {
        try {
            CronExpression cronExpression = new CronExpression(cron);
            return cronExpression.getNextValidTimeAfter(date);
        }catch (Exception exp){
            throw new BsfException("corn表达式解析出错",exp);
        }
    }

    public static boolean isOnline(Date heartbeatDate){
        return new Date().getTime()-heartbeatDate.getTime()<=2* Config.heartbeat()*1000;
    }
}
