# 自动化流量样本录制说明
 自动化录制流量是通过代码中集成sdk的模式,实现http的流量及feign调用链的流量录制和采样,相应的sdk为[bsf框架](https://gitee.com/chejiangyi/free-bsf-all/tree/1.2-SNAPSHOT/ )中的core模块和autotest模块以及相应的配置。
## 引入pom文件
```
    <!--选择添加,包版本定义文件,可以不添加,假如出现依赖问题的话,可查看此文件-->
    <dependency>
        <groupId>com.free.bsf</groupId>
        <artifactId>free-bsf-dependencies</artifactId>
        <version>1.2-SNAPSHOT</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    <!--必须,引入基础包-->
    <dependency>
        <groupId>com.free.bsf</groupId>
        <artifactId>free-bsf-core</artifactId>
        <version>1.2-SNAPSHOT</version>
    </dependency>
    <!--必须,引入autotest包-->
    <dependency>
        <groupId>com.free.bsf</groupId>
        <artifactId>free-bsf-autotest</artifactId>
        <version>1.2-SNAPSHOT</version>  
    <!--选择添加,如果出现feign.xxx相关的错误则添加解决-->
    </dependency>
        <dependency>
        <groupId>io.github.openfeign</groupId>
        <artifactId>feign-core</artifactId>
        <optional>true</optional>
    </dependency>
```
#### 配置开关
参考配置出处[bsf-autotest](https://gitee.com/chejiangyi/free-bsf-all/blob/1.2-SNAPSHOT/free-bsf-autotest/README.md )
```
#【基本配置:必填】
spring.application.name=free-demo-provider
#一键启用开关,默认false,重启后生效【必填,true】
autotest.enabled=false

#是否开启流量录制,默认false,实时开关生效【必填,true】
autotest.record.enabled=false

#录制频率,默认为1,即1:1录制;<=0则默认不采集;实时生效【选填】
autotest.record.frequency=1

#录制跳过部分url,支持前后*模糊匹配，支持逗号分割多个url,默认为空。【选填】
autotest.record.skip.urls=

#录制批量缓存的最大数量,默认5000【选填】
autotest.record.cache.max=5000

#针对微服务,是否开启链路追求录制流量,仅支持feign调用http场景;开启后http header 中增加autotest-traceid头。【选填,没有feign就无需填写】
autotest.traceid.enabled=true

#录制流量时,自动检测操作类型是否为仅查询/操作,目前仅支持mybatis场景下的sql检测;如insert,update,delete被判别为操作类型。【选填,没有feign就无需填写】
autotest.operatortype.enabled=false

#录制流量存储持久化刷新的周期,单位ms,默认5000【选填】
autotest.store.flush.timespan=5000

#录制流量存储的保存类型,默认mysql,未来支持es,kafka,rocketmq【必填】
autotest.store.type=mysql

### mysql 存储引擎【以下必填】
autotest.store.mysql.driver=com.mysql.cj.jdbc.Driver
#注意开启:rewriteBatchedStatements=true
autotest.store.mysql.url=jdbc:mysql://{填入db信息}:3306/autotest?useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&rewriteBatchedStatements=true
autotest.store.mysql.user={填入账号}
autotest.store.mysql.password={填入密码}
```

### 其他
源码比较简单,需要集成的人可以抽取源码另外打包亦可！


by 车江毅