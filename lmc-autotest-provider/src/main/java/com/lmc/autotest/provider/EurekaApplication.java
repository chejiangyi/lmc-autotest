package com.lmc.autotest.provider;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.lmc.autotest.api.provider.CustomerProvider;
import org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.plugin.core.PluginRegistry;

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
//@MapperScan(basePackages = "com.bmc.demo.dao")
@EnableFeignClients(basePackages = {"com.lmc.autotest.api"})
public class EurekaApplication {
    /**
     * 获取注入对象方式一
     */
    @Autowired(required=false)
    CustomerProvider customerProvider;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EurekaApplication.class, args);

        //LoggingFailureAnalysisReporter
    }
}
