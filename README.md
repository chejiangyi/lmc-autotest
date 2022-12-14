# lmc-autotest BSF全链路压测工具
 基于<a href='https://gitee.com/chejiangyi/free-bsf-all/tree/1.0-SNAPSHOT/'>BSF基础框架</a>构建全链路压测框架,从框架层面进行[流量录制](https://gitee.com/chejiangyi/free-bsf-all/tree/1.2-SNAPSHOT/free-bsf-autotest )或浏览器自定义录制,从工具层面进行流量回放，进行性能压测，自动输出压测报告，自动进行全链路功能验收，从而提升测试效能，指导网站性能优化。工具支持分布式部署，万级高并发压测，灵活定制化压测。

![Image text](/doc/全链路压测设计图.jpg)
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
#外网开发人员使用, 注意bsf-core.jar和bsf-autotest.jar包,可能出现包引用错误
#请至gitee release中下载相应jar包。
cd lmc-autotest
mvn install
```

## 设计文档
* [全链路压测原型](/doc/全链路压测.rp)
* [全链路压测sql](/doc/install.sql)
* [全链路压测设计图](/README-Design.md)
* [压测报告示例图](/doc/demo/test-demo.jpg)

## 安装文档
* [release](https://gitee.com/chejiangyi/lmc-autotest/releases/)
* [快速安装](/README-Install.md)
* [阿里云部署实战笔记](/README-Install-aliyun.md)

## 使用文档
* [定制场景录制文档](/README-ModHeader.md)
* [任务编写案例](/README-Demo.md)
* [任务编写案例2](/README-Demo2.md)
* [计划任务编写案例](/README-Job.md)
* [样本导入案例](/README-Sample.md)
* [公共方法库案例](/README-PublicCode.md)

## 开发文档
* [OPEN API](/README-OpenApi.md)
* [java完整版自动录制样本流量sdk集成](/actual/README-1.md)
* [C#版本流量录制simple sdk](/doc/sdk/simpleSampleFilter.cs)
* [java版本流量录制simple sdk](/doc/sdk/simpleSampleFilter.java)

## 实践笔记
* [小白快速上手压测](/actual/README-6.md)
* [小白快速流量回放压测](/actual/README-7.md)
* [自动化QPS核心和主要接口jenkins触发性能压测](/actual/README-5.md)
* [自动化QPS核心和主要接口性能url压测](/actual/README-1.md)
* [自动化QPS不同级别接口压测](/actual/README-2.md)
* [自动化QPS不同开发人员接口压测](/actual/README-3.md)
* [自动化QPS全链路接口压测](/actual/README-4.md)
* 自动化QPS秒杀接口压测
* 自动化TPS秒杀场景压测
* [k8s滚动升级验证](https://www.cnblogs.com/chejiangyi/p/16808139.html)
  

## 兼容性
建议使用chrome浏览器

## 进阶篇
1. 暂不支持文件上传类的流量录制。
2. 微服务的流量录制目前支持feign进行traceid传递,支持mybatis进行操作或仅查询两种访问类型识别。
3. 期望通过sharding-jdbc支持影子表和影子库,未来还会在jdbc层面做影子表和影子库扩展能力。

## 更新记录
1. 2022-10-17 增加时钟对齐,增加http连接池能力,固定时间心跳上报压测信息。
2. 2022-11-01 增加用户权限,计划任务,attribute属性信息等。
3. 2022-11-03 增加openapi及simple多语言sdk范例。
4. 2022-11-04 增加高亮和智能提示。
5. 2022-11-11 增加公共方法库。
6. 2022-11-14 修复get无法请求的bug问题[重要]。

by [车江毅](https://www.cnblogs.com/chejiangyi/)


## 使用交流
[全链路压测效能10倍提升的压测工具实践笔记](https://www.cnblogs.com/chejiangyi/p/16900586.html)

工作微信,注明:全链路压测交流<br/>
<img src="/doc/weixin/weixin.jpg" width="250">