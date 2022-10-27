# 计划任务编写脚本示例
计划任务编写脚本示例

#### 1. 普通启动任务模板
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

by [车江毅](https://www.cnblogs.com/chejiangyi/)