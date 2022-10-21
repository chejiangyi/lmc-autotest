package com.lmc.autotest.core;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.*;
import lombok.val;
import org.springframework.util.CollectionUtils;

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
import java.util.*;

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

    public static void checkSampleSelectSql(String sql){
        //SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, DbType.mysql);
        if (CollectionUtils.isEmpty(stmtList)) {
            throw new BsfException("未检测到sql");
        }
        val tableNames = new ArrayList<String>();
        for (SQLStatement sqlStatement : stmtList) {
            if(!(sqlStatement instanceof SQLSelectStatement)){
                throw new BsfException("检测到非select语句:"+sqlStatement.toString());
            }
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);
            Map<TableStat.Name, TableStat> tables = visitor.getTables();
            Set<TableStat.Name> tableNameSet = tables.keySet();
            for (TableStat.Name name : tableNameSet) {
                String tableName = name.getName();
                if (!StringUtils.isEmpty(tableName)) {
                    tableNames.add(tableName);
                }
            }
        }
        for(val tableName:tableNames){
            if(!tableName.startsWith("auto_tb_sample_")){
                throw new BsfException("仅支持查询auto_tb_sample_开头的样本表");
            }
        }
    }

    public static void main(String[] args){
        val o = new NodeInfo(){};
        AutoTestTool.setObjectNullToEmpty(o);
        val o2 = o;
    }
}
