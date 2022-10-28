# 任务编写脚本示例2说明
特殊写法的脚本沉淀（注意任务的api和定时计划的api是不一样的两种实现）

#### 采集样本筛选脚本-直接url压测版本
```
/*通过直接写url和压测参数，然后写入到本地样本(sample)文件中*/
var dataMaps = [
{"app_name":"test","url":"http://www.baidu.com","method":"GET","header":JSON.stringify({'aaa':'bbb'}),"body":JSON.stringify({})},
{"app_name":"test","url":"http://www.baidu.com","method":"GET","header":"{}","body":"{}"}
];
/*var dataMaps=api.httpGet("http://远程数据源/");*/
for(var i=0;i<dataMaps.length;i++){
    api.writeSample(dataMaps[i]);
}
   ```

#### 采集样本筛选脚本-重写http url示范
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

#### 采集样本筛选脚本-样本筛选根据url分组,每个url使用最新的n条样本
```
/*通过sql流处理分批获取样本数据，然后写入到本地样本(sample)文件中*/
/*这里根据create_time倒排,使用每个url最后5条样本*/
api.streamSql2("select * from auto_tb_sample_"+api.nowFormat("yyyy_MM_dd")+" where  id in (select id from  (select @row_number := IF(@last_url = url, @row_number + 1, 1) AS orderUrlNum, @last_url := url as url2,id from auto_tb_sample_"+api.nowFormat("yyyy_MM_dd")+" where url like '%get%' order by url,create_time desc) as t where t.orderUrlNum<5) order by id desc",[],function (dataMap){
      api.writeSample(dataMap);
})
   ```

by [车江毅](https://www.cnblogs.com/chejiangyi/)