package com.iquickmove.common.util;

import java.util.Random;

/**
 * 16/11/2017 15:20.
 *
 * @author yancha
 * @since 1.0
 */
public class RandomUtil {

    private static final Random random = new Random(System.currentTimeMillis());
    static String resource = "0123456789";
    static char[] rs = resource.toCharArray();

    public static String randString(int i) {
        char[] buf = new char[i];
        int rnd;
        for (int j = 0; j < buf.length; j++) {
            int l = random.nextInt();
            rnd = Math.abs(l) % rs.length;
            buf[j] = rs[rnd];
        }
        return new String(buf);
    }


    public static Random getRandomSed() {
        return random;
    }

    public static String generateDefaultRandomSed() {
        return IdConversion.convertToString(random.nextLong());
    }

    public static String generateRandomSed(String str) {
        return str + "getData" + IdConversion.convertToString(random.nextLong());
    }

    private static class IdConversion {
        static String convertToString(long id) {
            return Long.toHexString(id);
        }

        public static long convertToLong(String lowerHex) {
            int length = lowerHex.length();
            if (length >= 1 && length <= 32) {
                int beginIndex = length > 16 ? length - 16 : 0;
                return convertToLong(lowerHex, beginIndex);
            } else {
                throw isntLowerHexLong(lowerHex);
            }
        }

        static long convertToLong(String lowerHex, int index) {
            long result = 0L;

            for (int endIndex = Math.min(index + 16, lowerHex.length()); index < endIndex; ++index) {
                char c = lowerHex.charAt(index);
                result <<= 4;
                if (c >= 48 && c <= 57) {
                    result |= (long) (c - 48);
                } else {
                    if (c < 97 || c > 102) {
                        throw isntLowerHexLong(lowerHex);
                    }

                    result |= (long) (c - 97 + 10);
                }
            }

            return result;
        }

        static NumberFormatException isntLowerHexLong(String lowerHex) {
            throw new NumberFormatException(lowerHex + " should be a 1 to 32 character lower-hex string with no prefix");
        }
    }


    public static long randomNumByRang(Integer max){
        return randomNumByRang(0,max);
    }

    public static long randomNumByRang(Integer min, Integer max){
        return random.nextInt(max-min)+min;
    }

    public static String randomNum(Integer size){
        return randomString(size,1);
    }
    public static String randomLetter(Integer size){
        return randomString(size,2);
    }
    public static String randomNumAndLetter(Integer size){
        return randomString(size,3);
    }
    private static String randomString(Integer size,int type){
        String nums = "0123456789";
        String letters = "abcdefghijkmnpqrstuvwxyz";//去掉了o与l  容易与数字混淆的字母  所以这里只有24个字母
        StringBuffer sb = new StringBuffer();
        //只有数字
        if(type==1){
            for (int i = 0; i < size; i++) {
                sb.append(nums.charAt((int)(Math.random() * nums.length())));
            }
        }
        //只有字母
        if(type==2){
            for (int i = 0; i < size; i++) {
                sb.append(letters.charAt((int)(Math.random() * letters.length())));
            }
        }
        //数字字母混合
        if(type==3){
            String temp = nums+letters;
            for (int i = 0; i < size; i++) {
                sb.append(temp.charAt((int)(Math.random() * temp.length())));
            }
        }
        return sb.toString();
    }

}
