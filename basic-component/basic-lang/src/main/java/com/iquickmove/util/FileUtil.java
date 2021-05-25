package com.iquickmove.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

/**
 * @author ancoka
 * @version V1.0
 * @package cn.tiger.massage.utils
 * @date 2020-02-24 10:18
 * @description
 */
public class FileUtil {
    public static InputStream readInputStream(String fileUrl) throws IOException {
        return FileUtil.readInputStream(fileUrl, 5);
    }

    public static InputStream readInputStream(String fileUrl, Integer timeout) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeout * 1000);
            conn.setReadTimeout(timeout * 1000);
            inputStream = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static String suffix(String fileUrl) {
        String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        if (StringUtils.isNotBlank(suffix)) {
            suffix = suffix.toLowerCase();
        }
        return suffix;
    }

    public static String renameFile(String fileUrl) {
        String suffix = FileUtil.suffix(fileUrl);
        return FileUtil.randomFileName(suffix);
    }

    public static String randomFileName() {
        return FileUtil.randomFileName(null);
    }

    public static String randomFileName(String suffix) {
        String fileName =  System.currentTimeMillis() + "_" + RandomStringUtils.random(5);
        if (suffix != null && !"".equals(suffix)) {
            fileName = fileName +  "." + suffix.toLowerCase();
        }
        return fileName;
    }

    /**
     * 兼容浏览器中文名称乱码
     * @param userAgent
     * @param fileName
     * @return
     */
    public static String fileNameForEveryBrower(String userAgent, String fileName) {
        String ret = fileName;
        try {
            if (userAgent.toLowerCase().indexOf("firefox") > 0) {
                ret = new String(ret.getBytes("GB2312"), "ISO-8859-1");
            } else {
                ret = URLEncoder.encode(ret, "UTF-8");
                ret = new String(ret.getBytes("UTF-8"), "GBK");
            }
        } catch (Exception ex) {
            // 不处理
        }
        return ret;
    }
}
