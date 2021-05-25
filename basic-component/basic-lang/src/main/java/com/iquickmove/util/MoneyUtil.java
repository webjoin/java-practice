package com.iquickmove.util;

import java.math.BigDecimal;

/**
 * @Author echo
 * @Description  金钱工具类
 * @Date 2019-09-12
 */

public class MoneyUtil {

    /**
     * 分转元
     * @param fen
     * @return
     */
    public static String fenToYuan(int fen) {
        String divide = NumberUtil.divide(fen, 100);
        return divide;
    }

    /**
     * 元转分
     * @param yuan
     * @return
     */
    public static int yuanToFen(String yuan) {
        int fen = BigDecimal.valueOf(Double.parseDouble(yuan)).multiply(BigDecimal.valueOf(100)).intValue();
        return fen;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return  string
     */
    public static String replace(String s){
        if(null != s && s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static String fenToYuanWithout00(int fen) {
        String fenToYuan = fenToYuan(fen);
        String yuan = fenToYuan.endsWith(".00") || fenToYuan.endsWith(".0") ? fenToYuan.substring(0,fenToYuan.indexOf(".")) : fenToYuan;
        return yuan;
    }



    public static void main(String[] args) {
        String s = fenToYuanWithout00(1);
        System.out.println(s);
    }

}
