# OpenApi文档
此为管理端provider web的openapi文档.

### 协议
目前支持get请求,post表单请求,暂不支持application/json提交数据。<br/>
必传字段
```
+ token:
  示例: xxxxxxxxx,详情web管理中的用户详情中的 api token信息,用以安全性校验。
```
返回协议:
```
{
   code:-1,//返回状态码,一般大于0为正确
   message:'',//返回信息
   data:? //返回数据
}
```

#### 接口如下
```
#简化版样本上传接口,无需java sdk支持,第三方语言也可以支持同步上传样本(不建议大量使用,性能有限)
http://{web端}/api/samples/
参数: String requests, String username, String token
+ requests:
  示例: 格式类似如[{"app_name":"test","url":"http://www.baidu.com","header":"{}","body":"{}","method":"GET"}] 数组转json字符串,其他扩展字段详见web管理中的样本信息。
+ username:
  示例: admin,用户名
+ token:
  示例: xxxxxxxxx,详情web管理中的用户详情中的 api token信息。
 

#启动task
http://{web端}/api/openTask/
参数: Integer id,String params ,String username,String token
+ id:
  示例: 任务id,详情web中的任务管理中的id信息。
+ params:
  示例: 任务params,格式如{'aaa':'bbb'}转json字符串,任务支持动态传参,一般用于定时计划时候和api调用时候传参,web界面操作任务时是不传参的。
+ username:
  示例: admin,用户名
+ token:
  示例: xxxxxxxxx,详情web管理中的用户详情中的 api token信息。


#关闭task
http://{web端}/api/closeTask/
参数: Integer id,String username,String token
+ id:
  示例: 任务id,详情web中的任务管理中的id信息。
+ username:
  示例: admin,用户名
+ token:
  示例: xxxxxxxxx,详情web管理中的用户详情中的 api token信息。

   ```

by [车江毅](https://www.cnblogs.com/chejiangyi/)