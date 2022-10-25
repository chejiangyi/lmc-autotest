# 任务编写脚本示例2说明
特殊写法的脚本沉淀

#### 1. 采集样本筛选脚本-重写http url示范
```
/*通过sql流处理分批获取样本数据，然后写入到本地样本(sample)文件中*/
api.streamSql2("select * from auto_tb_sample_"+api.nowFormat("yyyy_MM_dd")+" where url like '%get%' order by id desc limit 5000",[],function (dataMap){
      /*重写url host和端口*/
      var header = JSON.parse(dataMap["header"]);
      var app_name = dataMap["app_name"];
      if(app_name=='lmc-platform-provider')
      {
          dataMap["url"] = dataMap["url"].replace(header["host"],"platform.linkmore.com").replace("http:","https:");
      } 
      if(app_name=='lmc-operation-provider')
      {
          dataMap["url"] = dataMap["url"].replace(header["host"],"operation.linkmore.com").replace("http:","https:");
      } 
     if(app_name=='lmc-live-provider')
      {
          dataMap["url"] = dataMap["url"].replace(header["host"],"live.linkmore.com").replace("http:","https:");
      } 
      /*api.log(dataMap);*/
      api.writeSample(dataMap);
})
   ```


by [车江毅](https://www.cnblogs.com/chejiangyi/)