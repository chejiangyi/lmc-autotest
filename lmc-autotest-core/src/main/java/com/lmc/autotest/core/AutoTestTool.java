package com.lmc.autotest.core;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.util.DateUtils;
import lombok.val;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class AutoTestTool {
    public static boolean isOnLine(Date date){
        val newDate =  DateUtils.date2LocalDateTime(date).plus(2*Config.heartbeat(), ChronoUnit.SECONDS);
        if(newDate.isAfter(LocalDateTime.now())){
            return true;
        }else
            return false;
    }

//    public static Date cornNextTime(Date date, String cron)  {
//        try {
//            CronExpression cronExpression = new CronExpression(cron);
//            return cronExpression.getNextValidTimeAfter(date);
//        }catch (Exception exp){
//            throw new BsfException("corn表达式解析出错",exp);
//        }
//    }

//    public static boolean isOnline(Date heartbeatDate){
//        return new Date().getTime()-heartbeatDate.getTime()<=2* Config.heartbeat()*1000;
//    }
}
