package com.iquickmove.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ancoka
 * @version V1.0
 * @package com.iquickmove.util
 * @date 2020/11/11 11:53 下午
 * @description
 */
public class Md5Util {
    public static String encrypt(String str) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(str.getBytes());
            byte[] b = bmd5.digest();

            int i;
            StringBuilder buf = new StringBuilder();
            for (byte aB : b) {
                i = aB;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String k = "061f449a73644770bdbe5a7598f2de74aa233864d1f9204ac3aee5d19969e9ba";
        String aesKey = encrypt(k).toUpperCase();
        String content = "name=王星星&mobile=15212345678&bankName=招商银行&bankNum=6214830100799652&idNumber=620402198709215456&extraParam=1234";
        String s = AESEncryptUtil.aesEncrypt(content, aesKey);
        System.out.println(s);
    }
}
