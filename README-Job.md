# 定时计划编写脚本示例
定时计划编写脚本示例(注意压测任务的脚本api是不同的，实现也是不一样的，两者api不通用)。

#### 普通计划启动任务模板
```
var taskid=5;/*此为任务id*/
if(api.isTaskExist(taskid)&&!api.isTaskRunning(taskid))
{
    try{
       api.openTask(taskid);
       api.log("任务"+taskid+"已启动");
    }
    catch(e){
      api.error(["任务"+taskid+"失败",e]);
    }
}
   ```

#### 计划启动批量任务模板
```
var taskid=31;/*此为任务id*/
var urls="sys/getFirstCategoryList,liveSchedules/getGoodsListPreSchedules,/getStsServiceConfig".split(",");
for(var i=0;i<urls.length;i++){
   if(api.isTaskExist(taskid)&&!api.isTaskRunning(taskid))
   {
       var info="任务:"+taskid+"url:"+urls[i];
       api.openTask2(taskid,{"url":urls[i]});/*传参方式开启任务*/
       api.log(info+"已启动");
        /*等待当前压测结束*/
        while(api.isTaskRunning(taskid)){
           api.sleep(5000);
        }
    }
}
   ```
#### 飞书通知模板
```
 var content = "自动化定时任务测试";
 api.httpPost("https://open.feishu.cn/open-apis/bot/v2/hook/6c26281e-20cf-46db-b4ec-a4ad8a3963e1",{"msg_type":"text","content":{"text":content}});
```

by [车江毅](https://www.cnblogs.com/chejiangyi/)