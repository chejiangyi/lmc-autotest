# lmc-autotest 全链路压测工具阿里云安装笔记

#### 安装准备
* [sql初始化脚本](/doc/install.sql )
* [管理端provider包](https://gitee.com/chejiangyi/lmc-autotest/releases/ )
* [节点端task包](https://gitee.com/chejiangyi/lmc-autotest/releases/ )
* [安装文档](README-Install.md)

#### 数据库初始化
1. 执行install.sql脚本
   ![编写模板内容](/doc/aliyun/1.png )
#### docker打包
2. docker 打包
   ![编写模板内容](/doc/aliyun/2.png )
   ![编写模板内容](/doc/aliyun/3.png )
3. provider yaml文件
```
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: lmc-autotest-provider
  name: lmc-autotest-provider
  namespace: lmc-autotest
spec:
  selector:
    matchLabels:
      app: lmc-autotest-provider
  template:
    metadata:
      labels:
        app: lmc-autotest-provider
    spec:
      containers:
        - env:
            - name: JAVA_HOME
              value: /usr/java/latest
            - name: CLASSPATH
              value: '.://usr/java/latest/lib/tools.jar:/usr/java/latest/lib/dt.jar'
            - name: LC_ALL
              value: en_US.UTF-8
            - name: TZ
              value: Asia/Shanghai
            - name: spring.datasource.druid.url
              value: >-
                jdbc:mysql://{阿里云ip}:3306/pre-autotest?useSSL=false&serverTimezone=Asia/Shanghai
            - name: spring.datasource.druid.username
              value: {username}
            - name: spring.datasource.druid.password
              value: ${password}
          image: >-
            {镜像仓库}/lmc-autotest-provider:latest
          imagePullPolicy: Always
          name: lmc-autotest-provider
          resources:
            limits:
              cpu: '1'
              memory: 1Gi
            requests:
              cpu: 10m
              memory: 128Mi
```
4. task yaml文件
```
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: lmc-autotest-task
  name: lmc-autotest-task
  namespace: lmc-autotest
spec:
  selector:
    matchLabels:
      app: lmc-autotest-task
  template:
    metadata:
      labels:
        app: lmc-autotest-task
    spec:
      containers:
        - env:
            - name: JAVA_HOME
              value: /usr/java/latest
            - name: CLASSPATH
              value: '.://usr/java/latest/lib/tools.jar:/usr/java/latest/lib/dt.jar'
            - name: LC_ALL
              value: en_US.UTF-8
            - name: TZ
              value: Asia/Shanghai
            - name: spring.datasource.druid.url
              value: >-
                jdbc:mysql://{阿里云ip}:3306/pre-autotest?useSSL=false&serverTimezone=Asia/Shanghai
            - name: spring.datasource.druid.username
              value: {username}
            - name: spring.datasource.druid.password
              value: {password}
          image: >-
            {镜像仓库}/lmc-autotest-task:latest
          imagePullPolicy: Always
          name: lmc-autotest-task
          resources:
            requests:
              memory: 256Mi
      restartPolicy: Always
```

#### 最终部署图
![编写模板内容](/doc/aliyun/4.png )

#### 备注
* 如果使用多语言simple-sdk,则需要在provider的容器环境变量中再附加额外的bsf autotest采集样本的配置即可。
* 如果采用完整版本的bsf autotest sdk则不需要！

by [车江毅](https://www.cnblogs.com/chejiangyi/)