# qps-不同开发人员接口压测
 [AutoTestAttribute注解](https://gitee.com/chejiangyi/free-bsf-all/blob/1.6-SNAPSHOT/free-bsf-autotest/README.md )的不同开发人员的接口的定时压测报告,主要生成不同开发人员的性能报告,方便开发人员及时优化。
## 公共函数库
[公共函数库案例](../README-PublicCode.md)
My开头的为自定义的公共函数。

## 压测任务编写
[任务编写案例](/README-Demo.md)
```
/*根据动态传入的level参数执行任务*/
var develop= MyApiPsParams('develop','苡仁');/*默认人员*/
var env= MyApiPsParams('env','test');/*env*/
var date = MyApiPsParams('date',api.nowFormat("yyyy_MM_dd"));
api.log(["develop",develop]);
/*通过sql流处理分批获取样本数据，然后写入到本地样本(sample)文件中*/
api.streamSql2("select * from auto_tb_sample_"+date+" where  attribute->'$.env'='"+env+"' and attribute->'$.apiType'='查询' and attribute->'$.test'='需要' "+
"and (attribute->'$.author'='"+develop+"') and operator_type='仅查询' order by id desc limit 1500",[],function (dataMap){
      dataMap["url"]=MyReplaceUrl(dataMap);/*公共库函数执行*/
      api.writeSample(dataMap);
})
```
![img.png](img3-1.png)

## 定时计划编写
[计划任务编写案例](/README-Job.md)
```
var taskid=40;/*此为任务id*/
var env="pre";
var date = MyDateFormat(MyAddDays(new Date(),-1));/*前一天的样本数据*/
var develops = ["XX","XX","XX","XX"];/*开发人员*/
for(var i=0;i<develops.length;i++){
   var develop=develops[i];
   if(api.isTaskExist(taskid)&&!api.isTaskRunning(taskid))
   {
       var info="任务:"+taskid+"开发:"+develop;
       api.openTask2(taskid,{"develop":develop,"env":env,"date":date});/*传参方式开启任务*/
       api.log(info+"已启动");
        /*等待当前压测结束*/
        while(api.isTaskRunning(taskid)){
           api.sleep(5000);
        }
    }
}
```
![img.png](img3-2.png)

## 效果图
![img.png](img3-3.jpg)
![img.png](img3-4.png)

by [车江毅](https://www.cnblogs.com/chejiangyi/)