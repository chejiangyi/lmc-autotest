package com.lmc.autotest.core;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.ConvertUtils;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.ThreadUtils;
import lombok.val;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class AutoTestTool {
    //时钟对齐,只允许心跳起始点开始!最多一个心跳周期内对齐成功
    public static void clockCorrection(){
        Date startDate = new Date();
        while (!ThreadUtils.system().isShutdown()) {
            Date now = DbHelper.get(Config.mysqlDataSource(), (c) -> {
                Object time = c.executeScalar("select now() as time", new Object[]{});
                Date n = ConvertUtils.convert(time, Date.class);
                return n;
            });
            if (now!=null && DateUtils.date2LocalDateTime(now).getSecond() % Config.heartbeat() == 0) {
                return;
            }
            if((new Date().getTime()-startDate.getTime())>(Config.heartbeat()+1)*1000) {
                throw new BsfException("时钟对齐超时,时钟对齐失败");
            }
            ThreadUtils.sleep(50);
        }
    }

    public static boolean isOnLine(Date date){
        LocalDateTime newDate =  DateUtils.date2LocalDateTime(date).plus(2*Config.heartbeat(), ChronoUnit.SECONDS);
        if(newDate.isAfter(LocalDateTime.now())){
            return true;
        }else
            return false;
    }

    public static void downLoad(HttpServletResponse response, File file,String fileName){
        if(!file.exists()){
            throw new BsfException("文件"+file.getName()+"不存在");
        }
        try {
            // 读到流中
            try(InputStream inStream = new FileInputStream(file.getAbsolutePath())) {
                String fileName2 = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                // 设置输出的格式
                response.reset();
                response.setContentType("bin");
                response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName2 + "\"");
                // 循环取出流中的数据
                byte[] b = new byte[100];
                int len;
                while ((len = inStream.read(b)) > 0)
                {    response.getOutputStream().write(b, 0, len);}
                response.getOutputStream().flush();
             }
        } catch (IOException e) {
            throw new BsfException("文件下载失败");
        }
    }
    public static void upload(HttpServletResponse response, File file,String fileName){
        if(!file.exists()){
            throw new BsfException("文件"+file.getName()+"不存在");
        }
        try {
            // 读到流中
            try(InputStream inStream = new FileInputStream(file.getAbsolutePath())) {
                String fileName2 = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                // 设置输出的格式
                response.reset();
                response.setContentType("bin");
                response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName2 + "\"");
                // 循环取出流中的数据
                byte[] b = new byte[100];
                int len;
                while ((len = inStream.read(b)) > 0)
                {    response.getOutputStream().write(b, 0, len);}
            }
        } catch (IOException e) {
            throw new BsfException("文件下载失败");
        }
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

    public static void setObjectNullToEmpty(Object obj){
        if(obj == null)
            return;
        Field[] declaredFields = obj.getClass().getFields();
        for (Field field:declaredFields){
            if (field.getType() == String.class){
                try {
                    field.setAccessible(true);
                    Object object = field.get(obj);
                    if (object == null) {
                        field.set(obj, "");
                    }
                }catch (Exception e){
                    LogUtils.error(AutoTestTool.class,Config.appName(),"反射出错",e);
                }
            }
        }
    }

    public static void main(String[] args){
        val o = new NodeInfo(){};
        AutoTestTool.setObjectNullToEmpty(o);
        val o2 = o;
    }
}
