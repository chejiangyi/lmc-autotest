# lmc-autotest 全链路压测工具
 基于<a href='https://gitee.com/chejiangyi/free-bsf-all/tree/1.0-SNAPSHOT/'>BSF基础框架</a>构建全链路压测框架,从框架层面进行[流量录制](https://gitee.com/chejiangyi/free-bsf-all/tree/1.2-SNAPSHOT/free-bsf-autotest ),从工具层面进行流量回放，进行性能压测，自动输出压测报告，自动进行全链路功能验收，从而提升测试效能，指导网站性能优化。

## 项目结构

```
lmc-autotest
    -- lmc-autotest-core 	 #公共代码 （核心层）
    -- lmc-autotest-dao 	 #数据库操作 （数据层）
    -- lmc-autotest-service  #公共业务服务 （服务层）
    -- lmc-autotest-task 	 #压测任务节点 （任务层）
    -- lmc-autotest-provider #压测管理站点 （网站及api层）
 -- doc 					 #项目资料 （文档资料）
 -- README.md 				 #项目文档 （说明文档）

```

## 项目编译
```
cd lmc-autotest
mvn install
```

## docker 打包
压测任务节点打包
```
vim Dockerfile
FROM {jdk8 centos镜像}/jdk8:latest
ADD lmc-autotest-task-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

docker build -t lmc-autotest-task .
docker tag lmc-autotest-task:latest {镜像仓库}/lmc-autotest-task:latest
docker push {镜像仓库}/lmc-autotest-task:latest

docker run -it \
-p 8081:8081 \
-e spring.shardingsphere.datasource.main0.url="jdbc:mysql://{数据库地址+ip}/autotest?useSSL=false" \
-e spring.shardingsphere.datasource.main0.username="{数据库账号}" \
-e spring.shardingsphere.datasource.main0.password="{数据库密码}" \
lmc-autotest-task /bin/bash
```
压测任务管理站点
```
vim Dockerfile
FROM {jdk8 centos镜像}/jdk8:latest
ADD lmc-autotest-provider-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

docker build -t lmc-autotest-provider .
docker tag lmc-autotest-provider:latest {镜像仓库}/lmc-autotest-provider:latest
docker push {镜像仓库}/lmc-autotest-provider:latest

docker run -it \
-p 8080:8080 \
-e spring.shardingsphere.datasource.main0.url="jdbc:mysql://{数据库地址+ip}/autotest?useSSL=false" \
-e spring.shardingsphere.datasource.main0.username="{数据库账号}" \
-e spring.shardingsphere.datasource.main0.password="{数据库密码}" \
lmc-autotest-provider /bin/bash
```

## 设计文档
* [全链路压测原型](/doc/全链路压测.rp)
* [全链路压测sql](/doc/install.sql)
* 全链路压测设计图
![Image text](/doc/全链路压测设计图.jpg)

## 定制场景录制
* [定制场景录制文档](/README-ModHeader.md)
## 进阶篇
1. 暂不支持文件上传类的流量录制。
2. 微服务的流量录制目前支持feign进行traceid传递,支持mybatis进行操作或仅查询两种访问类型识别。
3. 期望通过sharding-jdbc支持影子表和影子库,未来还会在jdbc层面做影子表和影子库扩展能力。


by 车江毅