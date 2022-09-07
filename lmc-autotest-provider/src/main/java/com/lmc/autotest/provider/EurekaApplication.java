package com.lmc.autotest.provider;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: chejiangyi
 * @version: 2019-06-28 14:18
 * Eureka client 调用其他客户端 api示例
 * 注意要配合properties 的配置使用
 **/
/****************调试时候去掉SpringBootApplication注释*************/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
/**
 * 【关键配置】需要使用的项目api 协议类,一般放provider包下面;多个项目则配置多个
 * 注意:spring.main.allow-bean-definition-overriding=true 必须要配置否则会报错
 */
@ComponentScan(basePackages = {"com.lmc.autotest"})
public class EurekaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EurekaApplication.class, args);

        //LoggingFailureAnalysisReporter
    }
    //    private void getTask(){
//        ThreadUtils.system().submit("获取压测任务",()->{
//            while (!ThreadUtils.system().isShutdown()){
//                try {
//                    val task = DbHelper.get(Config.mysqlDataSource(), (c) -> {
//                        return new tb_task_dal().getWaitingTask(c);
//                    });
//                    if (task != null){
//                        createSampleFile(task);
//                        filterError(task);
//                    }
//                }catch (Exception e){
//                    LogUtils.error(AutoTestProvider.class,Config.AppName(),"获取压测任务出错",e);
//                }
//                ThreadUtils.sleep(5000);
//            }
//
//        });
//    }
}
