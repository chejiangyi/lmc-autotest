package com.lmc.autotest.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.free.bsf.autotest.AutoTestProperties;
import com.free.bsf.autotest.base.AutoTestIOUtils;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.model.auto.tb_sample_example_model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 *
 * https://www.cnblogs.com/caoweixiong/p/14716187.html
 */
public class HttpUtils {
    static {
        //调优http配置
        if(Config.httpPoolEnabled()) {
            System.setProperty("http.keepAlive", "true");
            System.setProperty("http.maxConnections", Config.httpPoolMaxSize() + "");
        }
    }
    public static HttpResponse request(tb_sample_example_model request,Boolean printLog,boolean keepAlivePool){
        HttpRequest r = new HttpRequest();
        r.httpUrl=StringUtils.nullToEmpty(request.getUrl());r.method=StringUtils.nullToEmpty(request.getMethod());
        r.header = JsonUtils.deserialize(request.header, new TypeReference<HashMap<String,String>>() {});
        r.body = request.getBody();
        r.appName = StringUtils.nullToEmpty(request.app_name);
        r.attribute = (StringUtils.isEmpty(request.getAttribute())?"{}": request.getAttribute());
        //LogUtils.info(HttpUtils.class, Config.nodeName(),"访问:"+r.httpUrl);
        val rs = httpRequest(r,keepAlivePool);
        if(printLog){
            Map info = new HashMap(2);
            info.put("request",r);
            info.put("response",rs);
            LogUtils.info(HttpUtils.class,Config.nodeName(),JsonUtils.serialize(info));
        }
        return rs;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HttpRequest{
        String appName;
        String httpUrl;
        String method;
        HashMap<String,String> header;
        String body;
        String attribute;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HttpResponse{
        HttpRequest request;
        int code;
        String method;
        HashMap<String,String> header;
        String body;
        long requestSize=0L;
        long responseSize=0L;
        long timeMs=0L;

        public boolean isSuccess(){
            if(code == 200){
                return true;
            }
            return false;
        }
    }
    private static HttpResponse httpRequest(HttpRequest httpRequest,Boolean keepAlivePool){
        HttpURLConnection conn=null;
        HttpResponse response = new HttpResponse();
        val beginTime = new Date().getTime();
        try {

            val url = new URL(httpRequest.httpUrl);
            response.setRequest(httpRequest);
            response.setMethod(httpRequest.method);
            conn = (HttpURLConnection)url.openConnection();
            // 获取请求方式
            conn.setRequestMethod(httpRequest.method.toUpperCase());
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            //备注:底层http url connection 遇到get会自动转成post发出请求
            if(!"GET".equals(conn.getRequestMethod())) {
                conn.setDoOutput(true);
            }
            // 设置连接输入流为true
            conn.setDoInput(true);
            conn.setConnectTimeout(Config.httpPoolConnectTimeout());
            conn.setReadTimeout(Config.httpPoolReadTimeout());
            conn.setUseCaches(false);
            //压测标识头
            conn.setRequestProperty(AutoTestProperties.AutoTestRunning,"true");
            conn.setRequestProperty("Accept-Charset","utf-8");
            //conn.setInstanceFollowRedirects(true);
            //理论上默认本身应该开启keep-alive,此处手工再加一次确保！
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 获取请求头部信息
            for (val kv:httpRequest.header.entrySet()) {
                String key = kv.getKey();
                String value = kv.getValue();
                //默认
                if (key.equals("content-type") && value == null) {
                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                } else {
                    conn.setRequestProperty(key, value);
                }
            }
            //备注: 强行抹去gzip等压缩,转发不做压缩处理,否则会涉及自解压。而且内网传输性能损耗不大。
            conn.setRequestProperty("Accept-Encoding","");
            //tryAddPool(url,conn);
            long requestSize =0;
            conn.connect();
            if(conn.getDoOutput()) {
                // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    val bs = httpRequest.body.getBytes("UTF8");
                    requestSize+=bs.length;
                    // 将参数输出到连接
                    out.write(bs);
                    // 输出完成后刷新并关闭流
                    out.flush();
                }
            }
            response.requestSize=requestSize;
            response.code=conn.getResponseCode();
            response.header = new HashMap<>();
            for(val filed:conn.getHeaderFields().entrySet()){
                response.header.put(filed.getKey()==null? "":filed.getKey()  ,conn.getHeaderField(filed.getKey()));
            }
            long responseSize =0;
            try(InputStream in = conn.getInputStream()) {
                val bs = AutoTestIOUtils.toArrays(in);
                responseSize+=bs.length;
                response.body = AutoTestIOUtils.toString(bs);
            }
            response.responseSize=responseSize;

            return response;
        }catch (Exception e){
            response.code=404;
            return  response;
            //throw new BsfException(e); 不报错
        }finally {
            response.timeMs = new Date().getTime()-beginTime;
            //释放连接以为不能复用连接池,但是可以避免连接溢出风险
            //底层太复杂,做好关闭连接池的配置预案,但是会降低性能
            if(Config.httpPoolEnabled()==false||keepAlivePool==false) {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    public static String getUrlPath(String url){
        try {
            val u = new URL(url);
            return u.getPath();
        }catch (Exception e){
            return url;
        }
    }

    public static void main(String[] args){
        val path = HttpUtils.getUrlPath("http://www.baidu.com:300/baidu/?aaa=中文&b=11");
        System.out.println(path);
    }
}
