# 公共方法库示例
公共方法库是es5语法js编写的通用函数库。

#### 公共方法库的案例模板
```
/*针对部署场景，域名映射处理*/
function MyReplaceUrl(dataMap){
	 var header = JSON.parse(dataMap["header"]);
      var appName = dataMap["app_name"];
	 var attribute = JSON.parse(dataMap["attribute"]);
	 var url = dataMap["url"];
	 var host = header["host"];
	 var env = null;
	 if(attribute!=null){
		 env=attribute["env"];
	 }
	 if(env!=null && env !=""){
	     /* /basicData  /pay/  /openApi/   /live/   /customer/  /operation/  /platform/ 针对自身实际情况调整*/
		  var domain= {'lmc-platform-provider':'/platform','lmc-pay-provider':'/pay','lmc-openApi-provider':'/openApi',
		  'lmc-live-provider':'/live', 'lmc-customer-provider':'/customer','lmc-operation-provider':'/operation',
		  'lmc-basic-data-provider':'/basicData'};
		  /*域名映射,解决k8s内网访问问题；重写url host和端口,指向k8s内部负载均衡节点*/
		  for(var key in domain){
			  if(appName==key){
					 return url.replace(host,env+"-gateway.linkmore.com"+domain[key]).replace("http:","https:");/*替换域名,走网关*/
			   }
		  }
	 }
	 return url;
}
/*扩展api动态参数获取支持默认值*/
function MyApiPsParams(key,defaultValue){
    if(api.ps.params[key]!=null){
       return api.ps.params[key];/*动态参数*/
    }
    return defaultValue;
}
/*格式化时间:yyyy_MM_dd*/
function MyDateFormat(dat){
    var year = dat.getFullYear();
    var mon = (dat.getMonth()+1) < 10 ? "0"+(dat.getMonth()+1) : dat.getMonth()+1;
    var data = dat.getDate()  < 10 ? "0"+(dat.getDate()) : dat.getDate();
    var newDate = year +"_"+ mon +"_"+ data;
    return newDate;
}
/*时间日期累加*/
function MyAddDays(date,add) {
    date.setDate(date.getDate()+add);
    return date;
}
/*url 获取相对路径*/
function MyUrlRelativePath(url)
{
　　var ss = url.split("//");
   if(ss.length!=2)
      return url;/*不合规*/
    var url2=ss[1];
　　var start = url2.indexOf("/");
    if(start<=-1){
      return "";/*无path */
    }
　　var relUrl = url2.substring(start);
    /*无query*/
　　if(relUrl.indexOf("?") != -1){
　　　　relUrl = relUrl.split("?")[0];
　　}
　　return relUrl;
}
/*根据url path去重,取path集合*/
function MyDistinctUrlPath(urls){
    var distincts=[];
    for(var i=0;i<urls.length;i++){
        var path = MyUrlRelativePath(urls[i]);
        if(distincts.indexOf(path)<=-1){
            distincts.push(path);
        }
    }
    return distincts;
}
/*消息通知,目前通知飞书*/
function MyNotify(env,content,reportid){
    var token="8e8c2031-18d0-4cf4-9ff1-a8f238ee7b55";
    if(env==null||env=="test"||env=="dev"){
        /*token="";*/
    }
    if(content!=null&&env!=null){
        content+=" 环境:"+env;
    }
    if(content!=null&&reportid!=null){
        content+=" 报告地址:http://{域名}/report/view/?id="+reportid;
    }
    api.httpPost("https://open.feishu.cn/open-apis/bot/v2/hook/"+token,{"msg_type":"text","content":{"text":content}});
}
   ```
公共方法库仅供参考,实际根据自身场景编写公共方法！一般公共函数要有特殊开头，便于识别。比如我们会以My开头~


by [车江毅](https://www.cnblogs.com/chejiangyi/)