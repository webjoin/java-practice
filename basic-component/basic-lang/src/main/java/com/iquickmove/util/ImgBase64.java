package com.iquickmove.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;


public class ImgBase64 {
    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = FileUtil.readInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(data);
    }

    public static String getImgStr(InputStream in) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
            return new BASE64Decoder().decodeBufferToByteBuffer(in).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }


}
