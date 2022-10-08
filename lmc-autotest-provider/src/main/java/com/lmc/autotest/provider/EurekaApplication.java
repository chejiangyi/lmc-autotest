package com.lmc.autotest.provider;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.DateUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.ThreadUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.tb_node_dal;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

/**
 * @author: chejiangyi
 * @version: 2019-06-28 14:18
 * Eureka client 调用其他客户端 api示例
 * 注意要配合properties 的配置使用
 **/
/****************调试时候去掉SpringBootApplication注释*************/
@SpringBootApplication()//exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class}
/**
 * 【关键配置】需要使用的项目api 协议类,一般放provider包下面;多个项目则配置多个
 * 注意:spring.main.allow-bean-definition-overriding=true 必须要配置否则会报错
 */
@ComponentScan(basePackages = {"com.lmc.autotest"})
public class EurekaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EurekaApplication.class, args);
        clearOffLineNodes();
    }
     private static void clearOffLineNodes(){
        ThreadUtils.system().submit("清理离线节点",()->{
            while (!ThreadUtils.system().isShutdown()){
                try {
                    val clearNodes = DbHelper.get(Config.mysqlDataSource(), (c) -> {
                        return new tb_node_dal().getClearNodes(c);
                    });
                    for(val n:clearNodes){
                        DbHelper.call(Config.mysqlDataSource(), (c) -> {
                            new tb_node_dal().delete(c,n.id);
                        });
                    }
                }catch (Exception e){
                    LogUtils.error(EurekaApplication.class,Config.appName(),"清理离线节点出错",e);
                }
                ThreadUtils.sleep(5000);
            }

        });
    }
}
