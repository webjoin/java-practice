package com.iquickmove.util;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
//import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;

/**
 * @author tangyu
 * @since 2019-04-08 23:26
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static RestTemplate restTemplate;

    private static final String EMPTY = "";

    private static final String QUESTION = "?";

    private static final String AND = "&";

    private static final String EQUA = "=";
    /**
     * 初始化 restTemplate 对象
     *
     * @param restTemplate1
     */
    public static RestTemplate init(RestTemplate restTemplate1) {
        if (Objects.isNull(restTemplate1)) {
            synchronized (HttpUtil.class) {
                if (Objects.nonNull(restTemplate1)) {
                    restTemplate = restTemplate1;
                } else {
                    restTemplate = new RestTemplateBuilder()
                            .setReadTimeout(Duration.ofSeconds(30))
                            .setConnectTimeout(Duration.ofSeconds(30))
                            .build();
                }
            }
        }

        return restTemplate;
    }

    /**
     * 初始化 restTemplate 对象
     */
    public static void init() {
        init(null);
    }

    /**
     * 普通get 请求
     *
     * @param reqUrl 请求地址
     * @return
     */
    public static String get(String reqUrl) {
        return get(reqUrl, null, null, String.class);
    }

    /**
     * 普通get 请求
     *
     * @param reqUrl 请求地址
     * @param reqMap 请求数据 最后会通过&链接到 URL 后面
     * @return
     */
    public static String get(String reqUrl, Map<String, Object> reqMap) {
        return get(reqUrl, reqMap, null, String.class);
    }

    /**
     * 普通get 请求 带header 参数  返回字符类型
     *
     * @param reqUrl 请求地址
     * @param reqMap 请求数据 最后会通过&链接到 URL 后面
     * @return
     */
    public static String get(String reqUrl, Map<String, Object> reqMap, Map<String, Object> reqHeader) {
        return get(reqUrl, reqMap, reqHeader, String.class);
    }

    /**
     * 普通的get 方法 带 header 参数  返回指定的类型
     *
     * @param reqUrl    请求地址
     * @param reqMap    请求数据 最后会通过&链接到 URL 后面
     * @param reqHeader 请求头
     * @param clazz     返回的类型
     * @param <T>
     * @return
     */
    public static <T> T get(String reqUrl, Map<String, Object> reqMap, Map<String, Object> reqHeader, Class<T> clazz) {

        String reqString = getReqString(reqMap);
        if (reqUrl.contains(QUESTION)) {
            reqUrl += AND;
        } else {
            reqUrl += QUESTION;
        }
        reqUrl += reqString;
        T t;
        if (Objects.nonNull(reqHeader) && !reqHeader.isEmpty()) {
            t = execute(reqUrl, new HttpEntity(getHeader(reqHeader)), HttpMethod.GET, clazz);
        } else {
            init();
            t = restTemplate.getForObject(reqUrl, clazz);
        }
        return t;
    }

    public static <T> T restTemplatePost(String reqUrl, MultiValueMap<String, Object> reqMap, Class<T> clazz) {
        init();
        return restTemplate.postForObject(reqUrl, reqMap, clazz);
    }


        /**
         * 普通的get 方法
         *
         * @param reqUrl 请求地址
         * @param reqMap 请求数据 最后会通过&链接到 URL 后面
         * @param clazz  返回的类型
         * @param <T>
         * @return
         */
    public static <T> T get(String reqUrl, Map<String, Object> reqMap, Class<T> clazz) {
        return get(reqUrl, reqMap, null, clazz);

    }

    /**
     * 构建请求header 对象
     *
     * @param reqHeader
     * @return
     */
    private static MultiValueMap<String, String> getHeader(Map<String, Object> reqHeader) {
        MultiValueMap<String, String> header = new HttpHeaders();
        reqHeader.forEach((k, v) -> header.add(k, Objects.toString(v, EMPTY)));
        return header;

    }


    /**
     * 构建请求 form表单提交的 请求体
     *
     * @param reqMap
     * @return
     */
    private static String getReqString(Map<String, Object> reqMap) {
        if (Objects.nonNull(reqMap) && !reqMap.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = reqMap.entrySet();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : entries) {
                String key = entry.getKey();
                if (!entry.getValue().getClass().isArray()) {
                    String val = Objects.toString(entry.getValue(), EMPTY);
                    sb.append(key).append(EQUA).append(val).append(AND);
                } else {
                    Object[] params;
                    if (entry.getValue() instanceof Object[]) {
                        params = (Object[]) entry.getValue();
                    } else if (entry.getValue() instanceof String[]) {
                        params = (String[]) entry.getValue();
                    } else if (entry.getValue() instanceof int[]) {
                        params = ArrayUtils.toObject((int[]) entry.getValue());
                    } else if (entry.getValue() instanceof double[]) {
                        params = ArrayUtils.toObject((double[]) entry.getValue());
                    } else {
                        params = new Object[] {};
                    }
                    logger.info("数组参数：{}", params);
                    for(Integer i = 0; i < params.length; i++) {
                        String k = key +"["+ i.toString() +"]";
                        String val = Objects.toString(params[i], EMPTY);
                        sb.append(k).append(EQUA).append(val).append(AND);
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }


    /**
     * 普通表单请求 application/x-www-form-urlencoded
     *
     * @param reqUrl  请求地址
     * @param reqData 请求的对象
     * @return 返回字符类型
     */
    public static String postFormUrlencoded(String reqUrl, Map<String, Object> reqData) {
        return postFormUrlencoded(reqUrl, reqData, String.class);
    }

    public static <T> String postFormUrlencoded(String reqUrl, T t) {
        Map<String, Object> reqMap = JsonUtil.json2Bean(JsonUtil.toJsonStr(t),
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                });

        return postFormUrlencoded(reqUrl, reqMap, String.class);
    }

    /**
     * 普通表单请求 application/x-www-form-urlencoded
     *
     * @param reqUrl  请求地址
     * @param reqData 请求的对象
     * @param clazz   数据返回类型
     * @param <T>
     * @return
     */
    public static <T> T postFormUrlencoded(String reqUrl, Map<String, Object> reqData, Class<T> clazz) {
        String reqString = getReqString(reqData);
        MultiValueMap<String, String> header = new HttpHeaders();
        header.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(reqString, header);
        T t = execute(reqUrl, httpEntity, HttpMethod.POST, clazz);
        return t;
    }

    /**
     * 普通的 对象 请求 application/json
     *
     * @param reqUrl  请求地址
     * @param reqData 请求的对象
     * @return
     */
    public static String postJson(String reqUrl, Object reqData) {
        return postJson(reqUrl, reqData, String.class);
    }


    /**
     * application/json模式发送请求
     *
     * @param urlStr
     * @param json
     * @return
     */
    public static String sendPostJson(String urlStr, String json) {
        String resp = "";
        try {
            URL url = new URL(urlStr); //url地址

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                resp = sbf.toString();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return resp;
    }

    /**
     * application/json
     *
     * @param reqUrl  请求地址
     * @param reqData 请求的对象
     * @param clazz   指定返回的类型
     * @param <T>
     * @return
     */
    public static <T> T postJson(String reqUrl, Object reqData, Class<T> clazz) {
        return execute(reqUrl, new HttpEntity(reqData), HttpMethod.POST, clazz);
    }
    public static <T> T postJson(String reqUrl, Object reqData,Map<String,Object> headers, Class<T> clazz) {
        return execute(reqUrl, new HttpEntity(reqData), HttpMethod.POST, clazz);
    }

    /**
     * application/json
     * <p>
     * 返回指定的多级泛型类
     *
     * @param reqUrl
     * @param reqData
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T postJson(String reqUrl, Object reqData, ParameterizedTypeReference<T> type) {
        return execute(reqUrl, new HttpEntity(reqData), HttpMethod.POST, type);
    }

    /**
     * 通用执行对象 application/json
     *
     * @param reqUrl 请求地址
     * @param entity 请求对象
     * @param method 请求方法
     * @param type   数据返回类型
     * @param <T>    返回序列化好的对象
     * @return
     */
    private static <T> T execute(String reqUrl, HttpEntity entity, HttpMethod method, ParameterizedTypeReference<T> type) {
        init();
        ResponseEntity<T> exchange = restTemplate.exchange(reqUrl, method, entity, type);
        return exchange.getBody();
    }

    private static <T> T execute(String reqUrl, HttpEntity entity, HttpMethod method, Class<T> clazz) {
        init();
        ResponseEntity<T> exchange = restTemplate.exchange(reqUrl, method, entity, clazz);
        return exchange.getBody();
    }


    /**
     * get
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doGet(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    /**
     * post form
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param bodys
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      Map<String, String> bodys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    /**
     * Post String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Post stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }



    private static boolean setProxy(String proxyHost, Integer proxyPort, HttpRequestBase httpRequestBase, int timeout) {
        boolean isViaProxy = false;
        if (!org.springframework.util.StringUtils.isEmpty(proxyHost) && proxyHost != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .build();
            httpRequestBase.setConfig(config);
            isViaProxy = true;
        } else {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .build();
            httpRequestBase.setConfig(config);
        }
        return isViaProxy;
    }



    public static String get(String url, String encoding, String proxyHost, Integer proxyPort, int timeoutMiniSeconds) {
        String response = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        boolean isViaProxy = setProxy(proxyHost, proxyPort, httpGet, timeoutMiniSeconds);
        try {
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            try {
                logger.debug(httpResponse.getStatusLine() + " , " + url + (isViaProxy ? " , via:" + proxyHost + ":" + proxyPort : ""));
                response = EntityUtils.toString(httpResponse.getEntity(), encoding);
                logger.debug(response);
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            exceptionHandle(e, url, timeoutMiniSeconds);
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                exceptionHandle(e, url, timeoutMiniSeconds);
            }
        }
        return response;
    }


    private static final Integer TIME_OUT = 5000;

    public static String post(String url, String postData, String mediaType, String encoding,
                              Header[] headers, String proxyHost, Integer proxyPort) {
        return post(url, postData, mediaType, encoding,
                headers, proxyHost, proxyPort, TIME_OUT);

    }

    public static String post(String url, String postData, String mediaType, String encoding,
                              Header[] headers, String proxyHost, Integer proxyPort, int timeoutMiniSeconds) {
        String response = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        boolean isViaProxy = setProxy(proxyHost, proxyPort, httpPost, timeoutMiniSeconds);
        try {
            StringEntity entity = new StringEntity(postData, encoding);
            entity.setContentType(mediaType);
            httpPost.setEntity(entity);
            if (headers != null && headers.length > 0) {
                httpPost.setHeaders(headers);
            }
            CloseableHttpResponse httpResponse = client.execute(httpPost);
            try {
                logger.debug(httpResponse.getStatusLine().getStatusCode() + " " + url + (isViaProxy ? " via:" + proxyHost + ":" + proxyPort : ""));
                byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                response = new String(bytes, encoding);
                logger.debug(response);
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            exceptionHandle(e, url, postData, timeoutMiniSeconds);
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                exceptionHandle(e, url, postData, timeoutMiniSeconds);
            }
        }
        return response;

    }

    public static String post(String url) {
        return post(url, "", "application/json");
    }

    public static String post(String url,String mediaType) {
        return post(url, "", mediaType);
    }

    public static String post(String url, String postData, int timeoutMiniSeconds) {
        return post(url, postData, "application/json", "utf-8", null, null, null, timeoutMiniSeconds);
    }

    public static String post(String url, String postData, String mediaType) {
        return post(url, postData, mediaType, "utf-8", null, null, null);
    }

    public static String post(String url, String postData, Header[] header, String mediaType) {
        return post(url, postData, mediaType, "utf-8", header, null, null);
    }

    public static String post(String url, String postData, Header[] header, String mediaType, int timeoutMiniSeconds) {
        return post(url, postData, mediaType, "utf-8", header, null, null, timeoutMiniSeconds);
    }

    private static void exceptionHandle(Exception e, String url, int timeOut) {
        e.printStackTrace();
        throw new RuntimeException("调用服务失败，服务地址：" + url + "，超时时间：" + timeOut + "，异常类型："
                + e.getClass() + "，错误原因：" + e.getMessage());
    }

    private static void exceptionHandle(Exception e, String url, String postData, int timeOut) {
        e.printStackTrace();
        throw new RuntimeException("调用服务失败，服务地址：" + url + "，请求参数：" + postData + "，超时时间：" + timeOut + "，异常类型："
                + e.getClass() + "，错误原因：" + e.getMessage());
    }





    public static String doFormPost(String url, List<NameValuePair> params, String mediaType, Header[] headers) throws IOException {
        return doFormPost(url, params, mediaType, "utf-8", headers);
    }

    public static String doFormPost(String url, List<NameValuePair> params, String mediaType, String encoding, Header[] headers) throws IOException {
        return doFormPost(url, params, mediaType, encoding, headers, TIME_OUT);
    }

    public static String doFormPost(String url, List<NameValuePair> params, String mediaType, String encoding,
                                    Header[] headers, int timeout) throws IOException {
        CloseableHttpClient client = null;
        String result = "";
        try {
            client = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .build();
            HttpPost post = new HttpPost(url);
            post.setConfig(config);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, encoding);
            formEntity.setContentType(mediaType);
            post.setEntity(formEntity);
            if (headers != null && headers.length > 0) {
                post.setHeaders(headers);
            }
            CloseableHttpResponse resp = client.execute(post);
            try {
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(resp.getEntity(), encoding);
                } else {
                    throw new RuntimeException("from remote server receive status is " + resp.getStatusLine().getStatusCode() + " , url=>" + url + ",params=>" + JsonUtil.toJsonArr(params));
                }
            } finally {
                resp.close();
            }
        } catch (Exception e) {
            exceptionHandle(e, url, timeout);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("client close with error", e);
                }
            }

        }
        return result;
    }

    /**
     * Put String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doDelete(String host, String path, String method,
                                        Map<String, String> headers,
                                        Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
//            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
//        try {
//            SSLContext ctx = SSLContext.getInstance("TLS");
//            X509TrustManager tm = new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//                public void checkClientTrusted(X509Certificate[] xcs, String str) {
//
//                }
//                public void checkServerTrusted(X509Certificate[] xcs, String str) {
//
//                }
//            };
//            ctx.init(null, new TrustManager[] { tm }, null);
//            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            ClientConnectionManager ccm = httpClient.getConnectionManager();
//            SchemeRegistry registry = ccm.getSchemeRegistry();
//            registry.register(new Scheme("https", 443, ssf));
//        } catch (KeyManagementException ex) {
//            throw new RuntimeException(ex);
//        } catch (NoSuchAlgorithmException ex) {
//            throw new RuntimeException(ex);
//        }
    }


    /*
     * 处理https GET/POST请求
     * */
    public static String https(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = null;
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * formdata post
     *
     * @param url
     * @param urlParameters
     * @return
     * @throws IOException
     */
    public static String doFormPost(String url, List<NameValuePair> urlParameters) throws IOException {
        return doFormPost(url, urlParameters, 5000);
    }

    public static String doFormPost(String url, List<NameValuePair> urlParameters, int timeout) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
        post.setConfig(config);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        StringBuffer result = new StringBuffer();
        try {
            HttpResponse response = client.execute(post);
            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            exceptionHandle(e, url, urlParameters, timeout);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {

                }
            }
        }
        return result.toString();
    }

    private static void exceptionHandle(Exception e, String url, List<NameValuePair> pairs, int timeOut) {
        e.printStackTrace();
        throw new RuntimeException("调用服务失败，服务地址：" + url + "，请求参数：" + JsonUtil.toJsonStr(pairs) + "，超时时间：" + timeOut + ",异常类型："
                + e.getClass() + "，错误原因：" + e.getMessage());
    }



    /**
     * servlet request to map
     *
     * @param request
     * @return
     */
//    public static Map convertRequestParam2Map(HttpServletRequest request) {
//        //使用map 暂存参数
//        Map params = new HashMap();
//        // 从servletRequest中拿到所有参数
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) parameterMap.get(name);
//            String valueStr = "";
//            //servlet param中一个键可能对应多个值，使用，隔开
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//            params.put(name, valueStr);
//        }
//        if (params.isEmpty()) {
//        }
//        return params;
//    }
}
