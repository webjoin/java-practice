package com.iquickmove.util;

import com.iquickmove.util.serializer.IdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * DesUtils加密和解密工具,可以对字符串进行加密和解密操作  。
 * @author 刘尧兴
 * <p>2009-12-5</p>
 */
@Slf4j
public class DesUtils {

    /** 字符串默认键值     */
    private static String strDefaultKey = "medical@19ds";

    public DesUtils() {
    }

    public static void main(String[] args) {
        // TODO Auto-generated method   stub
//        String source  = "7";
//        //加密结果
//        String result = encrypt(source);
//
//        //解密
////        System.out.println(decrypt("dGGIMb88tNM"));
//
//        int error = 0;
//        for (int i = 0; i < 100; i++) {
//            String result2 = encrypt(i + "", IdSerializer.KEY);
//                if(result2.indexOf("+") > -1) {
//                    System.out.println(i + ">>>>>" + result2);
//                }
//        }
        String decrypt = decrypt("DIsisJP2JQo", IdSerializer.KEY);
        System.out.println(decrypt);
    }

    public static String encrypt(String source) {
        return encrypt(source,strDefaultKey);
    }

    /**
     * DES加密操作
     * @param source 要加密的源
     * @param key    约定的密钥
     * @return
     */
    public static String encrypt(String source,String key){
        //强加密随机数生成器
        SecureRandom random = new SecureRandom();
        try {
            //创建密钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            //创建密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretKey =  keyFactory.generateSecret(keySpec);
            //加密对象
            Cipher cipher = Cipher.getInstance("DES");
            //初始化加密对象需要的属性
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //开始加密
            byte[] result = cipher.doFinal(source.getBytes());
            //Base64加密
            return  Base64.getEncoder().encodeToString(result) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptWithEncode(String source,String key){
        String s = encrypt(source, key);
        if(StringUtil.isNotBlank(s)) {
            try {
                s = URLEncoder.encode(s,"utf-8");
            } catch (Exception e) {  // todo 没有报错可以拿掉
                log.error("DesUtils.encryptWithEncode.s:{},err:{} ",s, ExceptionUtils.getStackTrace(e));
            }
        }
        return s;
    }



        public static String decrypt(String cryptograph){
        return decrypt(cryptograph,strDefaultKey);
    }
    /**
     * 解密
     * @param cryptograph 密文
     * @param key         约定的密钥
     * @return
     */
    public static String decrypt(String cryptograph,String key){
        //强加密随机生成器
        SecureRandom random  =  new SecureRandom();
        try {
            //定义私钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            //定义密钥工厂
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretkey = factory.generateSecret(keySpec);
            //创建加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretkey, random);
            //Base64对
            byte[] result = Base64.getDecoder().decode(cryptograph);
            return new String(cipher.doFinal(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
