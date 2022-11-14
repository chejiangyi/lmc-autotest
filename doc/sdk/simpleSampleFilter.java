package com.lmc.test.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.free.bsf.autotest.AutoTestContext;
import com.free.bsf.autotest.AutoTestProperties;
import com.free.bsf.autotest.AutoTestRecordFilter;
import com.free.bsf.autotest.base.AutoTestUtil;
import com.free.bsf.autotest.base.CachedRequestWrapper;
import com.free.bsf.autotest.base.OperatorTypeEnum;
import com.free.bsf.autotest.store.StoreManager;
import com.free.bsf.core.config.CoreProperties;
import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.core.util.PropertyUtils;
import com.free.bsf.core.util.StringUtils;
import lombok.val;
import lombok.var;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * java simple-sdk
 * autotest 应用使用/api/samples 拦截样本上传
 * by chejiangyi
 */
@Component
public class AutoTestSimpleSampleFilter implements Filter {
    //最大缓存数量
    public static int MaxCacheSize = 5000;
    //autotest api的url地址
    public static String AutoTestApiUrl = "http://localhost:8080/api/samples/";
    //autotest 用户名
    public static String UserName = "admin";
    //autotest token秘钥
    public static String Token = "3875e83a718c4ec27c355a8c8dc4027f";
    //autotest 应用名
    public static String AppName = "java-default";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain
            chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Request requestInfo =null;
        if ("application/json".equalsIgnoreCase(req.getContentType())) {
            req=new BufferedRequestWrapper(req);
            requestInfo = new Request();
            requestInfo.method = req.getMethod();
            requestInfo.appName = AppName;
            requestInfo.header = mapper.writeValueAsString(getHeaders(req));
            requestInfo.body =  new String(((BufferedRequestWrapper)req).requestBody, StandardCharsets.UTF_8);
            requestInfo.url = getEntireUrl(req);
            requestInfo.createTime=new Date();
        }

        chain.doFilter(req, res);

        if(requestInfo!=null){
            add(requestInfo);
        }
    }
    @Override
    public void destroy() {
    }
    @Override
    public void init(FilterConfig filterConfig) {

    }

    //获取完整地址
    public String getEntireUrl(HttpServletRequest request){
        String baseUrl = request.getRequestURL().toString();
        if(!StringUtils.isEmpty(request.getQueryString())){
            return baseUrl+"?"+StringUtils.nullToEmpty(request.getQueryString());
        }
        return baseUrl;
    }

    private static ConcurrentLinkedDeque<Request> requests = new ConcurrentLinkedDeque<>();
    private static Object locked = new Object();
    private static Boolean init = false;
    private static ObjectMapper mapper = new ObjectMapper()
            .enable(MapperFeature.USE_ANNOTATIONS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setTimeZone(TimeZone.getTimeZone("GMT+8"))
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));;

    private void add(Request request) {
        if (requests.size() > MaxCacheSize) {
            return;
        }
        requests.push(request);
        if (init == false)
        {
            synchronized (locked)
            {
                if (init == false)
                {
                    val thread = new Thread(()->{
                        while (true) {
                            try {
                                val cached = requests.toArray(new Request[]{});
                                requests.clear();
                                if(cached.length>0) {
                                    val form = new HashMap<String, String>();
                                    form.put("requests", mapper.writeValueAsString(cached));
                                    form.put("username", UserName);
                                    form.put("token", Token);
                                    httpPostForm(AutoTestApiUrl, form);
                                }
                            } catch (Exception ex) {
                                System.out.println("【警告】录制流量失败：" + ex.getMessage());
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (Exception e) {
                            }
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
                    init = true;
                }
            }
        }
    }

    private void httpPostForm(String url, HashMap<String,String> data) throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String,String> entry : data.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            params.add(pair);
        }
        //设置请求实体(参数)
        val httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
        try (var httpClient = HttpClients.createDefault()) {
            var r = httpClient.execute(httpPost);
            r.getStatusLine();
            r.close();
        }
    }

    private Map getHeaders (HttpServletRequest request) {
        Map map = new HashMap();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name  = headerNames.nextElement();
            String value = request.getHeader(name);
            map.put(name,value);
        }
        return map;
    }

    //请求对象协议
    private class Request
    {
        public String appName;
        public String url;
        public String header;
        public String body;
        public String method;
        public Date createTime;
        //{"app_name":"test","url":"http://www.baidu.com","header":"{}","body":"{}","method":"GET"}
    }
    //缓存Request重写
    private class BufferedRequestWrapper extends HttpServletRequestWrapper {
        //参数字节数组
        private byte[] requestBody;
        //Http请求对象
        private HttpServletRequest request;

        public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.requestBody =toArrays(request.getInputStream());
            this.request = request;
        }

        private byte[] toArrays(InputStream input) throws IOException {
            try(ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.flush();
                return output.toByteArray();
            }
        }

        /**
         * @return
         * @throws IOException
         */
        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
            return new ServletInputStream() {

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }

                @Override
                public int read() {
                    return bais.read();
                }
            };
        }

        public byte[] getRequestBody() {
            return requestBody;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
    }
}
