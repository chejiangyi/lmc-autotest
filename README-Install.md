# lmc-autotest 全链路压测工具安装文档
 安装分为docker版本安装和普通虚拟机安装两种模式。

#### release包下载
* [sql初始化脚本](/doc/install.sql )
* 管理端provider包 (gitee 不支持大文件上传!)
* 节点端task包 (gitee 不支持大文件上传!)


#### 普通运行
```
nohup java -jar \
-Dspring.shardingsphere.datasource.main0.url=jdbc:mysql://127.0.0.1:3306/pre-autotest?useSSL=false \
-Dspring.shardingsphere.datasource.main0.username=sa \
-Dspring.shardingsphere.datasource.main0.password=sa \
lmc-autotest-provider.jar > provider.log 2>&1 &

nohup java -jar \
-Dspring.shardingsphere.datasource.main0.url=jdbc:mysql://127.0.0.1:3306/pre-autotest?useSSL=false \
-Dspring.shardingsphere.datasource.main0.username=sa \
-Dspring.shardingsphere.datasource.main0.password=sa \
lmc-autotest-task.jar > task.log 2>&1 &
```

#### docker 打包
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

by 车江毅