//package com.iquickmove.util;
//
//import io.jsonwebtoken.*;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.sql.Date;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author ancoka
// * @version V1.0
// * @package com.iquickmove.util
// * @date 2020/11/11 10:32 下午
// * @description
// */
//public class JwtUtils {
//    public static final int JWT_LIVE_TIME = 7 * 24 * 60 * 60 * 1000;
//    public static final int JWT_LONG_LIVE_TIME = 30 * 24 * 60 * 60 * 1000;
//
//    public static String createJWT(Integer userId, String username) {
//        return createJWT(String.valueOf(userId), username, JWT_LIVE_TIME);
//    }
//
//    /**
//     * 签发JWT
//     *
//     * @param id
//     * @param subject   可以是JSON数据 尽可能少
//     * @param ttlMillis
//     * @return String
//     */
//    public static String createJWT(String id, String subject, long ttlMillis) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        SecretKey secretKey = generalKey();
//        JwtBuilder builder = Jwts.builder()
//                .setId(id)
//                // 主题
//                .setSubject(subject)
//                // 签发者
//                .setIssuer("user")
//                // 签发时间
//                .setIssuedAt(now)
//                // 签名算法以及密匙
//                .signWith(signatureAlgorithm, secretKey);
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date expDate = new Date(expMillis);
//            // 过期时间
//            builder.setExpiration(expDate);
//        }
//        return builder.compact();
//    }
//
//    /**
//     * 验证JWT
//     *
//     * @param jwtStr
//     * @return
//     */
//    public static Map<String, Object> validateJWT(String jwtStr) {
//        Map<String, Object> checkResult = new HashMap<>();
//        Claims claims;
//        try {
//            claims = parseJWT(jwtStr);
//            checkResult.put("succues", true);
//            checkResult.put("claims", claims);
//        } catch (ExpiredJwtException e) {
//            checkResult.put("code", "JWT_ERRCODE_EXPIRE");
//            checkResult.put("succues", false);
//        } catch (SignatureException e) {
//            checkResult.put("code", "JWT_ERRCODE_FAIL");
//            checkResult.put("succues", false);
//        } catch (Exception e) {
//            checkResult.put("code", "JWT_ERRCODE_FAIL");
//            checkResult.put("succues", false);
//        }
//        return checkResult;
//    }
//
//    public static SecretKey generalKey() {
//        byte[] encodedKey = Base64.getDecoder().decode("7786df7fc3a34e26a61c034d5ec8245d");
//        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
//        return key;
//    }
//
//    /**
//     * 解析JWT字符串
//     *
//     * @param jwt
//     * @return
//     * @throws Exception
//     */
//    public static Claims parseJWT(String jwt) throws Exception {
//        SecretKey secretKey = generalKey();
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(jwt)
//                .getBody();
//    }
//}
