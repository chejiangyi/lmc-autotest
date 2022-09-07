package com.lmc.autotest.core;

import lombok.val;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class AutoTestTool {
    public static boolean isOnLine(Date date){
        val newDate = DateUtils.addSeconds(date,Config.heartbeat());
        if(newDate.getTime()>new Date().getTime()){
            return true;
        }else
            return false;
    }
}
