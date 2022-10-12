# lmc-autotest 全链路压测工具安装文档
 安装分为docker版本安装和普通虚拟机安装两种模式。

#### release包下载
* [sql初始化脚本](/doc/install.sql )
* [管理端provider包](https://gitee.com/chejiangyi/lmc-autotest/releases/ )
* [节点端task包](https://gitee.com/chejiangyi/lmc-autotest/releases/ )


#### 第一种部署方案: 普通运行
```
nohup java -jar \
-Dspring.datasource.druid.url=jdbc:mysql://127.0.0.1:3306/pre-autotest?useSSL=false \
-Dspring.datasource.druid.username={数据库账号} \
-Dspring.datasource.druid.password={数据库密码} \
lmc-autotest-provider.jar > provider.log 2>&1 &

nohup java -jar \
-Dspring.datasource.druid.url=jdbc:mysql://127.0.0.1:3306/pre-autotest?useSSL=false \
-Dspring.datasource.druid.username={数据库账号} \
-Dspring.datasource.druid.password={数据库密码} \
lmc-autotest-task.jar > task.log 2>&1 &
```

#### 第二种部署方案: docker 打包
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
-e spring.datasource.druid.url="jdbc:mysql://{数据库地址+ip}/autotest?useSSL=false" \
-e spring.datasource.druid.username="{数据库账号}" \
-e spring.datasource.druid.password="{数据库密码}" \
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
-e spring.datasource.druid.url="jdbc:mysql://{数据库地址+ip}/autotest?useSSL=false" \
-e spring.datasource.druid.username="{数据库账号}" \
-e spring.datasource.druid.password="{数据库密码}" \
lmc-autotest-provider /bin/bash
```

##### 其他:task任务节点内存调优笔记
```
期望:
task压测节点日常基本不用,需要内存要收缩到最低;这样在无压测任务时，特别是在容器部署场景,内存可以让给其他业务应用。
一旦需要进行压测,节点可以内存伸缩(逐步申请内存，反应慢一点影响不大),最终达到稳态。压测结束后,内存再次收缩最小到最低。
调优参数(其他调优参数还在探索):
-Xss256k #压测线程原理简单，调用栈无需很深,可以减少线程的栈大小。
-XX:MinHeapFreeRatio=5 #free内存无需很多,要用的时候重新申请即可。
-XX:MaxHeapFreeRatio=10 #free内存无需很多,要用的时候重新申请即可。
其他:
[计算公式参考]单个节点最大线程数(一般2000-3000够了)*256k(Xss)+50M=限制内存大小(如果要限制task节点最大内存 -Xmx)
示例:
java -jar -Xss256k -XX:MinHeapFreeRatio=5 -XX:MaxHeapFreeRatio=10 -Xmx??M lmc-autotest-task.jar
```
by 车江毅