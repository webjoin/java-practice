package com.iquickmove.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;


/**
 * java后台get、post请求
 *
 * @author kxm
 */
public class HttpRequest {

    /**
     * 读取超时时间
     */
    private static int READ_TIME_OUT = 5_000;

    /**
     * 连接超时时间
     */
    private static int CONNECT_TIME_OUT = 5_000;

    public static void main(String[] args) {
        String url = "http://localhost:50100/water/api/sign/callBack";
        Map<String,Object> map = new HashMap<>();
        map.put("data","WCGa7Y7ZgZwcGzJhMOMJUQh64p9pGxDZ4F+R5yvHc+XhoSTkL11GgSEHXXMnaWQR424YZl+ZsFwDgDFOrheI0ja7jv7QN5kd23fyw9K5YiAWNDIJhsHrC9ghKo0mPjl91g3E4ehEMq52bqEpwejBfLUOJNk6sFVvIbgYuFQFJvEZcACNKR/abtotpBzEI8bo7duqHuCq2QZb6kc4RGfbce4z0PThHUttQhY7/C4ppT7+5khTeVauXcfW0dIT5nml");
        String s = HttpRequest.sendPost(url, map);
        System.out.println(s);
    }

    private static Logger logger = LoggerFactory.getLogger(HttpRequest.class);


    private static String sendPost(String url, Map<String, Object> param, Object req, String type, int connectTimeout, int readTimeout, boolean isNeedError) {
        logger.info("请求地址{},参数{}", url, req);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection httpUrlConn = null;
        try {

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpUrlConn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setReadTimeout(READ_TIME_OUT);
            httpUrlConn.setConnectTimeout(CONNECT_TIME_OUT);

            if (connectTimeout > 0) {
                httpUrlConn.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                httpUrlConn.setReadTimeout(readTimeout);
            }

            String body = null;
            if (Objects.equals(type, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                if (Objects.nonNull(param)) {
                    body = getParam(param);
                }
                if (Objects.nonNull(req)) {
                    body = getParamFromObj(req);
                }

            } else if (Objects.equals(type, MediaType.APPLICATION_JSON_UTF8_VALUE)) {
                httpUrlConn.setRequestProperty("content-type", type);
                body = JSON.toJSONString(req);
            }
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(body);

            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("请求地址{},结果{}", url, result);
        } catch (Exception e) {
            if (isNeedError) {
                logger.error("发送 POST 请求出现异常！url:{} param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace((e)));
            } else {
                logger.warn("发送 POST 请求出现异常！url:{},param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace((e)));
            }
        }
        //使用finally块来关闭输出流、输入流
        finally {
            httpClose(in, out, httpUrlConn);
        }
        return result;

    }


    private static String sendPost(String url, Map<String, Object> param, Object req, String type, int connectTimeout, int readTimeout) {
        return sendPost(url, param, req, type, connectTimeout, readTimeout, true);
    }

    private static String sendPost(String url, Map<String, Object> param, Object req, String type) {
        return sendPost(url, param, req, type, 0, 0, true);
    }

//    private static String sendPostNoError(String url, Map<String, String> param, Object req, String type) {
//        return sendPostFormTimeNoError(url, param, null, null);
//    }

    private static String sendPostError(String url, Map<String, Object> param, Object req, String type) {
        return sendPost(url, param, req, type, 0, 0, true);
    }

    private static String sendPostForm(String url, Map<String, Object> param) {
        return sendPost(url, param, null, null, 0, 0, true);
    }

    public static String sendPostFormTimeNoError(String url, Map<String, Object> param, int connectTimeout, int readTimeout) {
        return sendPost(url, param, null, MediaType.APPLICATION_FORM_URLENCODED_VALUE, connectTimeout, readTimeout, false);
    }

    /**
     * 发送 post form 请求   不输出异常日志
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendPostFormNoError(String url, Map<String, Object> param) {
        return sendPostFormTimeNoError(url, param, 0, 0);
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是Map的形式,请在key为参数，value为值。
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String sendPost(String url, Map<String, Object> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection httpUrlConn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpUrlConn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setReadTimeout(READ_TIME_OUT);
            httpUrlConn.setConnectTimeout(CONNECT_TIME_OUT);
//            httpUrlConn.setReadTimeout(CONNECT_LONG_TIME_OUT_LONG_60);
//            httpUrlConn.setConnectTimeout(CONNECT_LONG_TIME_OUT_LONG_60);
//            CONNECT_LONG_TIME_OUT_LONG_60
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(getParam(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(httpUrlConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！url:{}, param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace((e)));
        }
        //使用finally块来关闭输出流、输入流
        finally {
            httpClose(in, out, httpUrlConn);
        }
        return result;
    }

    public static String sendPostObject(String url, Map<String, Object> param) {
        return sendPostObject(url, param, true);
    }

    /**
     * 发送 post  不输入ERROR 日志
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendPostMapNoErr(String url, Map<String, Object> param) {
        return sendPostObject(url, param, true);
    }

    public static String sendPostObject(String url, Map<String, Object> param, boolean isNeedError) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection httpUrlConn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpUrlConn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setReadTimeout(READ_TIME_OUT);
            httpUrlConn.setConnectTimeout(CONNECT_TIME_OUT);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(getParamObject(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(httpUrlConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            if (isNeedError) {
                logger.error("发送 POST 请求出现异常！url:{}, param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace(e));
            } else {
                logger.warn("发送 POST 请求出现异常！url:{}, param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace((e)));

            }
//            throw new Exception("发送 POST 请求出现异常！", e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            httpClose(in, out, httpUrlConn);
        }
        return result;
    }


    /**
     * 构建请求参数
     *
     * @param params
     * @return
     */
    public static String getParamObject(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        String str = new String();
        if (params != null) {
            for (Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue().toString());
                sb.append("&");
            }
            str = sb.substring(0, sb.length() - 1);
        }
        return str;
    }

    public static String getParam(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        String str = "";
        if (params != null) {
            for (Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(Objects.toString(e.getValue(), ""));
                sb.append("&");
            }
            str = sb.substring(0, sb.length() - 1);
        }
        return str;
    }

    public static String getParamFromObj(Object req) {
        if (Objects.isNull(req)) {
            return "";
        }
        Class<?> aClass = req.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Map<String, Object> prams = new HashMap<>(declaredFields.length);
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Class<?> type = field.getType();
            JSONField annotation = field.getAnnotation(JSONField.class);
            if (Objects.nonNull(annotation)) {
                fieldName = annotation.name();
            } else {
                JsonProperty annotation1 = field.getAnnotation(JsonProperty.class);
                if (Objects.nonNull(annotation1)) {
                    fieldName = annotation1.value();
                }
            }

            String valStr;
            Object valObj = null;
            try {
                valObj = field.get(req);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(valObj)) {
                if ("List".equals(type.getSimpleName())) {
                } else {
                    valStr = valObj.toString();
                    prams.put(fieldName, valStr);
                }

            }
        }
        return getParam(prams);
    }

    public static String sendPostByJson(String url, String json) {
        return sendPostByJson(url, json, true);
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是Map的形式,请在key为参数，value为值。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPosts(String url, Map<String, String> param, int timeout) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection httpUrlConn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpUrlConn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");

            if (timeout != 0) {
                httpUrlConn.setConnectTimeout(timeout);
                httpUrlConn.setReadTimeout(timeout);
            } else {
                httpUrlConn.setConnectTimeout(5000);
                httpUrlConn.setReadTimeout(5000);
            }
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(getParams(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(httpUrlConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！url:{}, param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace((e)));
        }
        //使用finally块来关闭输出流、输入流
        finally {
            httpClose(in, out, httpUrlConn);
        }
        return result;
    }

    private static void httpClose(BufferedReader in,
                                  PrintWriter out,
                                  HttpURLConnection conn) {
        if (Objects.nonNull(in)) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(out)) {
            out.close();
        }
        if (Objects.nonNull(conn)) {
            conn.disconnect();
        }


    }

    /**
     * 构建请求参数
     *
     * @param params
     * @return
     */
    public static String getParams(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue().toString());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是Map的形式,请在key为参数，value为值。
     * @return 所代表远程资源的响应结果
     */
    public static String sendObjectPost(String url, Map<String, Object> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection httpUrlConn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpUrlConn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setConnectTimeout(5000);
            httpUrlConn.setReadTimeout(5000);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(getObjectParam(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(httpUrlConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！url:{}, param:{},exception:{}", url, JSON.toJSONString(param), ExceptionUtils.getStackTrace((e)));
        }
        //使用finally块来关闭输出流、输入流
        finally {
            httpClose(in, out, httpUrlConn);
        }
        return result;
    }

    public static String sendPostByJson(String url, String json, boolean contentTypeJson, int readTIme) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection httpUrlConn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpUrlConn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (contentTypeJson) {
                httpUrlConn.setRequestProperty("content-type", "application/json");
            }
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setConnectTimeout(readTIme);
            httpUrlConn.setReadTimeout(readTIme);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(json);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(httpUrlConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！url:{}, param:{},exception:{}", url, json, ExceptionUtils.getStackTrace((e)));
            result = "发送 POST 请求出现异常！" + e;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            httpClose(in, out, httpUrlConn);
        }
        return result;

    }

    public static String sendPostByJson(String url, String json, boolean contentTypeJson) {
        return sendPostByJson(url, json, contentTypeJson, CONNECT_TIME_OUT);
    }


    public static String getObjectParam(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public static String sendGet(String url) {
        return sendGet(url, true);
    }

    /**
     * get请求
     */
    public static String sendGet(String url, Map<String, Object> params) {
        String objectParam = getParam(params);
        if (Objects.nonNull(objectParam) && !objectParam.isEmpty()) {
            url = String.join("", url, "?", objectParam);

        }
        return sendGet(url, true);
    }

    /**
     * get请求
     */
    public static String sendGet(String url, boolean isNeedError) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setReadTimeout(CONNECT_TIME_OUT);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            if (isNeedError) {
                logger.error("发送GET请求出现异常！请求地址：{},异常{}", url, ExceptionUtils.getStackTrace(e));
            } else {
                logger.warn("发送GET请求出现异常！请求地址：{},异常{}", url, ExceptionUtils.getStackTrace(e));
            }
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("{},{}", url, ExceptionUtils.getStackTrace(e2));
            }
        }
        return result;
    }


    //    private static String BOUNDARY = "========7d4a6d158c9";
    private static String BOUNDARY = "---------------------------123821742118716";

    /**
     * 支持单个文件 图片 其他文件类型<br/>
     * <p>
     * 文件服务器目前还不支持多个
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @return
     */
    public static String uploadFile(String urlStr, Map<String, String> textMap,
                                    Map<String, File> fileMap) {
        String rn = "\r\n";
        String quotation = "\"";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        StringBuilder strBuf = new StringBuilder();
        StringBuilder rsBuilder = new StringBuilder();
        String contentType;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                for (Entry<String, String> entry : textMap.entrySet()) {
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append(rn).append("--").append(BOUNDARY).append(rn);
                    strBuf.append("Content-Disposition: form-data; name=").append(quotation).append(inputName).append(quotation).append(rn + rn);
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            // file
            if (fileMap != null) {
                for (Entry<String, File> entry : fileMap.entrySet()) {
                    String inputName = entry.getKey();
                    File file = entry.getValue();
                    if (file == null) {
                        continue;
                    }
                    String filename = file.getName();
                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    strBuf.append(rn).append("--").append(BOUNDARY).append(rn);
                    strBuf.append("Content-Disposition: form-data")
                            .append("; name=").append(quotation).append(inputName).append(quotation)
                            .append("; filename=").append(quotation).append(filename).append(quotation).append(rn);
                    strBuf.append("Content-Type:" + contentType + rn + rn);

                    out.write(strBuf.toString().getBytes());
                    DataInputStream destInputStream = new DataInputStream(new FileInputStream(file));
                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = destInputStream.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    destInputStream.close();
                }
            }
            byte[] endData = (rn + "--" + BOUNDARY + "--" + rn).getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                rsBuilder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (Objects.nonNull(conn)) {
                conn.disconnect();
            }
        }
        return rsBuilder.toString();
    }


}
