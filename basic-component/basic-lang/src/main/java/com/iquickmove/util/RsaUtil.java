package com.iquickmove.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class RsaUtil {

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";
    private static final Integer MAX_ENCRYPT_BLOCK = 100;
    private static final Integer MAX_DECRYPT_BLOCK = 512;

    /**
     * 生成公私钥对
     *
     * <pre>
     * {@code
     * ### 生成密钥
     * # cgs_uat.pem           密钥文件名称
     * # 4096                  密钥大小，至少2048
     * openssl genrsa -out cgs_uat.pem 4096
     *
     * ### 导出公钥
     * # cgs_uat.pem           上一步生成的密钥文件
     * # cgs_uat-public.pem    导出公钥文件名称
     * openssl rsa -in cgs_uat.pem -out cgs_uat-public.pem -pubout
     *
     * ### 导出Java可用的私钥
     * # cgs_uat.pem           第一步生成的密钥文件
     * # cgs_uat-private.pem   导出私钥文件名称
     * openssl pkcs8 -in cgs_uat.pem -topk8 -nocrypt -out cgs_uat-private.pem
     * }
     * </pre>
     * 
     * @param keySize
     *            密钥大小
     * @return 密钥对
     */
    public static RsaKeyPair generateKeyPair(int keySize) {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return new RsaKeyPair(
            org.apache.commons.codec.binary.Base64.encodeBase64String(keyPair.getPrivate().getEncoded()),
            org.apache.commons.codec.binary.Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
    }

    /**
     * 签名
     *
     * @param plain
     *            签名原文
     * @param privateKey
     *            私钥
     * @return 签名
     */
    public static byte[] sign(byte[] plain, PrivateKey privateKey) throws InvalidKeyException, SignatureException {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(plain);
            return signature.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 签名
     *
     * @param plain
     *            签名原文
     * @param charset
     *            原文字符集编码
     * @param privateKey
     *            私钥
     * @return base64(签名)
     */
    public static String sign(String plain, Charset charset, PrivateKey privateKey)
        throws SignatureException, InvalidKeyException {
        return Base64.encodeBase64String(sign(plain.getBytes(charset), privateKey));
    }

    /**
     * 签名
     *
     * @param plain
     *            签名原文
     * @param charset
     *            原文字符集编码
     * @param privateKey
     *            私钥串
     * @return base64(签名)
     */
    public static String sign(String plain, Charset charset, String privateKey)
        throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        return sign(plain, charset, getPrivateKey(privateKey));
    }

    /**
     * 验签
     *
     * @param plain
     *            签名原文
     * @param sign
     *            签名
     * @param publicKey
     *            公钥串
     * @return 是否验签通过
     */
    public static boolean verifySign(byte[] plain, byte[] sign, PublicKey publicKey)
        throws InvalidKeyException, SignatureException {
        Signature signature = null;
        try {
            signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(plain);
            return signature.verify(sign);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验签
     *
     * @param plain
     *            签名原文
     * @param sign
     *            base64(签名)
     * @param publicKey
     *            公钥
     * @return 是否验签通过
     */
    public static boolean verifySign(String plain, Charset charset, String sign, PublicKey publicKey)
        throws SignatureException, InvalidKeyException {
        return verifySign(plain.getBytes(charset), Base64.decodeBase64(sign), publicKey);
    }

    /**
     * 验签
     *
     * @param plain
     *            签名原文
     * @param sign
     *            base64(签名)
     * @param publicKey
     *            公钥串
     * @return 是否验签通过
     */
    public static boolean verifySign(String plain, Charset charset, String sign, String publicKey)
        throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        return verifySign(plain, charset, sign, getPublicKey(publicKey));
    }

    /**
     * 公钥加密
     *
     * @param data
     *            明文
     * @param publicKey
     *            公钥
     * @param rsaKeySize
     *            rsa密钥大小，典型的：2048，4096
     * @return 密文
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey, int rsaKeySize) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        int encryptBlockSize = calculateDecryptBlockSize(rsaKeySize) - 11;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > encryptBlockSize) {
                cache = cipher.doFinal(data, offSet, encryptBlockSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * encryptBlockSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 公钥加密
     *
     * @param data
     *            字符串明文
     * @param charset
     *            明文字符集编码
     * @param publicKey
     *            公钥
     * @param rsaKeySize
     *            rsa密钥大小，典型的：2048，4096
     * @return base64(密文)
     */
    public static String encrypt(String data, Charset charset, PublicKey publicKey, int rsaKeySize) throws Exception {
        return Base64.encodeBase64String(encrypt(data.getBytes(charset), publicKey, rsaKeySize));
    }

    /**
     * 公钥加密
     *
     * @param data
     *            字符串明文
     * @param charset
     *            明文字符集编码
     * @param publicKey
     *            公钥串
     * @param rsaKeySize
     *            rsa密钥大小，典型的：2048，4096
     * @return base64(密文)
     */
    public static String encrypt(String data, Charset charset, String publicKey, int rsaKeySize) throws Exception {
        return encrypt(data, charset, getPublicKey(publicKey), rsaKeySize);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData
     *            密文
     * @param privateKey
     *            私钥
     * @param rsaKeySize
     *            rsa密钥大小，典型的：2048，4096
     * @return 原文
     */
    public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey, int rsaKeySize) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        int decryptBlockSize = calculateDecryptBlockSize(rsaKeySize);
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > decryptBlockSize) {
                cache = cipher.doFinal(encryptedData, offSet, decryptBlockSize);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * decryptBlockSize;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 私钥解密
     *
     * @param encryptedData
     *            base64(密文)
     * @param charset
     *            明文字符集编码
     * @param privateKey
     *            私钥
     * @param rsaKeySize
     *            rsa密钥大小，典型的：2048，4096
     * @return 字符串明文
     */
    public static String decrypt(String encryptedData, Charset charset, PrivateKey privateKey, int rsaKeySize)
        throws Exception {
        return new String(decrypt(Base64.decodeBase64(encryptedData), privateKey, rsaKeySize), charset);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData
     *            base64(密文)
     * @param charset
     *            明文字符集编码
     * @param privateKey
     *            私钥串
     * @param rsaKeySize
     *            rsa密钥大小，典型的：2048，4096
     * @return 字符串明文
     */
    public static String decrypt(String encryptedData, Charset charset, String privateKey, int rsaKeySize)
        throws Exception {
        return decrypt(encryptedData, charset, getPrivateKey(privateKey), rsaKeySize);
    }

    private static int calculateDecryptBlockSize(int rsaKeySize) {
        return rsaKeySize / 8;
    }

    public static PublicKey getPublicKey(String publicKey) throws InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509KeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey getPrivateKey(String privateKey) throws InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static class RsaKeyPair {
        public RsaKeyPair(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        private final String privateKey;
        private final String publicKey;

        public String getPrivateKey() {
            return privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
        }
    }

    public static void main(String[] args) {
        // RsaKeyPair rsaKeyPair = generateKeyPair(512);
        // System.out.println(rsaKeyPair);
        String s =
            "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAiuPalYvBk4lb0KRzsBnFZ+4whmKP1pc6GdTcP4NQyKjf2yOwCLq25kjWCClpEqUQEHUb/gtyHlMvqjrGcxyw+QIDAQABAkAaVpNBALu4yreKnWUC5CA3UUgKc+5q90qhU2hg8voC0xvJxr04cRVcxtR059oKHAFtYbXMjL9RuowuirVG1X/BAiEAyxQ71gEFoTa/c2alfgKIJ2YHEyatknaMDK5I7ausO10CIQCvFXVC72+WwnvDx5ND7SKvnr9f99wv8GlVDsDofBLOTQIgPo5xEC8oaMzQlN/dwr9M6bYIH+IePau+4HkfhfcICxECIQChy6+gWooBhNpAsyapB/Qc3RO3SOMGviFoxAiLD1WFpQIhALry5bFxNfToJb9gcLcWOjhK6wR0pfI5HGAu+pMEvg+R";
        System.out.println("--");
        System.out.println(DesUtils.encrypt(s, "I5HGAu+pMEvg+R"));
        System.out.println("--");
    }

}