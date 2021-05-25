package com.iquickmove.util;


import com.iquickmove.base.consts.Cons;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since: 12/3/15.
 * @author: yangjunming
 */
public class StringUtil extends StringUtils {

    public final static Pattern p1 = Pattern.compile("[\t|\r|\n]");
    public final static Pattern p2 = Pattern.compile("[\"|\\\\]");

    final static Pattern SLASH = Pattern.compile("[\"|\\\\]");

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public static final char UNDERLINE = '_';

    private final static String NULL_STR = "null";

    private final static String DEFAULT_APPEND = "*";


    /**
     * 去除制定开始的字符(同C#的trimStart)
     *
     * @param source
     * @param characters
     * @return
     */
    public static String trimStart(String source, Character characters) {
        return source.replaceAll("^[" + characters + "]+", "");
    }

    public static String trimEnd(String source, Character characters) {
        return source.replaceAll("[" + characters + "]+$", "");
    }

    /**
     * 左填充
     *
     * @param str
     * @param length
     * @param ch
     * @return
     */
    public static String leftPad(String str, int length, char ch) {
        if (str.length() >= length) {
            return str;
        }
        char[] chs = new char[length];
        Arrays.fill(chs, ch);
        char[] src = str.toCharArray();
        System.arraycopy(src, 0, chs, length - src.length, src.length);
        return new String(chs);
    }


    /**
     * 获取驼峰名称
     *
     * @param tableName
     * @return
     */
    public static String getCamelName(String tableName) {

        boolean contains = tableName.contains(Cons.Chars.SHORTLINE);
        StringBuilder sb = new StringBuilder();
        if (contains) {
            String lowerTableName = tableName.toLowerCase();
            String[] worlds = lowerTableName.split(Cons.Chars.SHORTLINE);
            for (String world : worlds) {
                if (!worlds[0].equals(world)) {
                    char lowChar = world.charAt(0);
                    world = world.replaceFirst(Character.toString(lowChar), getUpper(lowChar));
                }
                sb.append(world);
            }
        } else {
            sb.append(tableName);
        }
        return sb.toString();
    }

    public static String camelToUnderline(String camel) {
        if (camel == null || "".equals(camel.trim())) {
            return "";
        }
        int len = camel.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = camel.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(getLower(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String getUpper(char lower) {
        return Character.toString((char) (lower - 32));
    }

    public static String getLower(char chaz) {
        return Character.toString((char) (chaz + 32));
    }


    /**
     * 右填充
     *
     * @param str
     * @param length
     * @param ch
     * @return
     */
    public static String rightPad(String str, int length, char ch) {
        if (str.length() >= length) {
            return str;
        }
        char[] chs = new char[length];
        Arrays.fill(chs, ch);
        char[] src = str.toCharArray();
        System.arraycopy(src, 0, chs, 0, src.length);
        return new String(chs);
    }

    public static Integer[] split2Ints(String delimiter, String string) {
        if (Objects.isNull(string) || string.isEmpty()) {
            return null;
        }
        String[] split = string.split(delimiter);
        Integer[] rs = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            rs[i] = Integer.valueOf(split[i]);
        }
        return rs;
    }


    public static boolean isPhoneNum(String value) {
        Pattern PHONE_PATTERN = Pattern.compile("1[3456789][0-9]{9}");
        Matcher matcher = PHONE_PATTERN.matcher(value);
        return matcher.matches();
    }


    public static String getValue(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str;
    }

    public static String getValue(Object str) {
        if (str == null) {
            return "";
        }

        if (StringUtils.isBlank(str.toString())) {
            return "";
        }
        return str.toString();
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    public static boolean isBlank(Object str) {
        return StringUtils.isBlank(getValue(str));
    }

    public static String addSlashes(String src) {
        if (src == null) {
            return "";
        }
        return src.replace("'", "\\'").replace("\"", "\\\"");
    }

    public static String urlencode(String src) {
        if (src == null) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return src;
    }

    public static String urldecode(String src) {
        if (src == null) {
            return "";
        }
        try {
            return java.net.URLDecoder.decode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return src;
    }

    final static Pattern REG_BLANK = Pattern.compile("[\t|\r|\n]");

    public static String replaceBlank(String str) {
        String dest = "";
        if (!StringUtils.isEmpty(str)) {
//            Matcher m = REG_BLANK.matcher(str);
//            dest = m.replaceAll("");
            dest = RegExUtils.removeAll(str," ");
        }
        return dest;
    }


    public static String replaceQuota(String str) {
        String dest = "";
        if (StringUtils.isNotEmpty(str)) {
            Matcher m = SLASH.matcher(str);
            dest = m.replaceAll("'").replace("''", "'");
        }
        return dest;
    }


    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 查询一个字符在字符串中出现的次数
     *
     * @param string
     * @param subString
     * @return
     */
    public static int getCount(String string, String subString) {
        int index = 0;
        int count = 0;
        if (string != null && subString != null && string.length() > 0 && subString.length() > 0) {
            while ((index = string.indexOf(subString, index)) != -1) {
                index = index + subString.length();
                count++;
            }
        }
        return count;
    }

    /**
     * 字符串转集合
     *
     * @param param 含有逗号的字符串
     * @return 数字集合
     */
    public static List<Integer> getListFromString(String param) {
        List<Integer> list = null;
        if (StringUtils.isNotBlank(param)) {
            list = collstringtointegerlst(Arrays.asList(param.split(Cons.Chars.COMMA)));

        }
        return Optional.ofNullable(list).orElse(new ArrayList<>());
    }


    public static List<Long> getLongListFromString(String param) {
        List<Long> list = null;
        if (StringUtils.isNotBlank(param)) {
            list = collstringtoLonglst(Arrays.asList(param.split(Cons.Chars.COMMA)));

        }
        return Optional.ofNullable(list).orElse(new ArrayList<>());
    }

    /**
     * @param param 通过逗号隔开
     * @return
     */
    public static Set<Integer> getSetFromString(String param) {
        Set<Integer> set = null;
        if (StringUtils.isNotBlank(param)) {
            set = collstring2IntegerSet(Arrays.asList(param.split(Cons.Chars.COMMA)));

        }
        return Optional.ofNullable(set).orElse(new HashSet<>());
    }

    /**
     * 将字符串类型的集合转换为Integer类型的集合
     *
     * @param inList 字符串集合
     * @return 数字集合
     */
    public static List<Integer> collstringtointegerlst(List<String> inList) {
        List<Integer> iList = new ArrayList<>(inList.size());
        for (String anInList : inList) {
            iList.add(Integer.parseInt(anInList));
        }
        return iList;
    }

    public static List<Long> collstringtoLonglst(List<String> inList) {
        List<Long> iList = new ArrayList<>(inList.size());
        for (String anInList : inList) {
            iList.add(Long.parseLong(anInList));
        }
        return iList;
    }

    public static Set<Integer> collstring2IntegerSet(List<String> inList) {
        Set<Integer> set = new HashSet<>(inList.size());
        for (String anInList : inList) {
            set.add(Integer.parseInt(anInList));
        }
        return set;
    }

    /**
     * 对 format 一种封装
     *
     * @param pattern
     * @param args
     * @return
     */
    public static String format(String pattern, Object... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = Objects.toString(args[i], Cons.Chars.EMPTY);
        }
        return MessageFormat.format(pattern, args);
    }

    /**
     * @param destText 订单已取消！商家取消了您在${shopName}的预订订单，点我看详情
     * @param param    {"shopName","广阔天地店"}
     * @return 订单已取消！商家取消了您在广阔天地店的预订订单，点我看详情
     */
    public static String format(String destText, Map<String, Object> param) {

        if (Objects.nonNull(param) && !param.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = param.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                destText = destText.replace("${" + entry.getKey() + "}", Objects.toString(entry.getValue(), Cons.Chars.EMPTY));
            }
        }
        return destText;
    }

    /**
     * 对 stirng.join 一种封装
     *
     * @param delimiter
     * @param elements
     * @return
     */
    public static String join(String delimiter, Collection elements) {
        List<String> list = new ArrayList<>(elements.size());
        for (Object element : elements) {
            list.add(Objects.toString(element, Cons.Chars.EMPTY));
        }
        return String.join(delimiter, list);
    }

    /**
     * 对 string.join 一种封装
     *
     * @param elements
     * @return
     */
    public static String join(List elements) {
        return join(Cons.Chars.COMMA, elements);
    }

    public static String join(Set elements) {
        return join(Cons.Chars.COMMA, elements);
    }

    public static String join(String... elements) {
        return String.join(Cons.Chars.COMMA, elements);
    }


    /**
     * 包含
     *
     * @param stringList 示例 2,3,4,5,55,66,11
     * @param dest       1 -> false
     *                   11-> true
     * @return
     */
    public static boolean contains(String stringList, String dest) {
        if (Objects.isNull(stringList) || stringList.isEmpty()) {
            return false;
        }
        return Arrays.asList(stringList.split(Cons.Chars.COMMA)).contains(dest);
    }


    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static String nullToNULL(String str) {
        if (equals(NULL_STR, str)) {
            return null;
        }

        return str;
    }

    /**
     * 将id集合转成字符串用;拼接
     *
     * @param ids
     * @return
     */
    public static String formatIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (Integer insertedId : ids) {
            sb.append(insertedId).append(";");
        }
        if (ids.size() >= 1) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    /**
     * 判断字符串列表中是否含有指定的字符串
     *
     * @param search
     * @param results
     * @return
     */
    public static boolean in(String search, String... results) {
        if (isEmpty(search)) {
            return true;
        }

        for (String str : results) {
            if (equals(search, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 固定长度输出
     *
     * @param str
     * @param length
     * @return
     */
    public static String fixedLength(String str, int length) {
        return fixedLength(str, length, false);
    }

    /**
     * 固定长度输出
     *
     * @param str
     * @param length
     * @param begin
     * @return
     */
    public static String fixedLength(String str, int length, boolean begin) {
        return fixedLength(str, length, DEFAULT_APPEND, begin);
    }

    /**
     * 固定长度输出
     *
     * @param str
     * @param length
     * @param append
     * @return
     */
    public static String fixedLength(String str, int length, String append, boolean begin) {
        if (isEmpty(str)) {
            return str;
        }

        if (str.length() > length) {
            return str.substring(0, length - 1);
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length - str.length(); i++) {
                sb.append(append);
            }

            if (begin) {
                return str + sb.toString();
            }
            return sb.toString() + str;
        }
    }

    /**
     * 去掉尾部的0
     *
     * @param s
     * @return
     */
    public static String replaceDoubleZero(String s) {
        if (null != s && s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    public static String replaceBetween(String sourceStr, int begin, int end, String replacement) {
        if (sourceStr == null) {
            return "";
        }
        if (replacement == null) {
            replacement = "*";
        }
        int replaceLength = end - begin;
        if (StringUtils.isNotBlank(sourceStr) && replaceLength > 0) {
            StringBuilder sb = new StringBuilder(sourceStr);
            sb.replace(begin, end, StringUtils.repeat(replacement, replaceLength));
            return sb.toString();
        } else {
            return sourceStr;
        }
    }

    public static String toOneHundredThousand(double number) {
//        if (number >= 100000) {
//            double value = number / 10000;
//            return NumberUtil.formatNumber1(value, "#,###.00") + "万";
//        }

        return NumberUtil.formatNumber1(number, "#.00");

    }


    public static String formateMobile(String mobile) {
        String formatMobile = mobile;
        if(isNotBlank(mobile) && mobile.length() == 11) {
            formatMobile = mobile.substring(0, 3) + " " + mobile.substring(3, 7) + " " + mobile.substring(7);
        }
        return formatMobile;
    }

    /**
     * 获取尾号
     * @param mobile
     * @return
     */
    public static String getMobileWeiHao(String mobile) {
        String formatMobile = mobile;
        if(isNotBlank(mobile) && mobile.length() > 3) {
            formatMobile = mobile.substring(mobile.length() - 4 );
        }
        return formatMobile;
    }

    public static String formateXingMobile(String mobile) {
        String formatMobile = mobile;
        if(isNotBlank(mobile) && mobile.length() == 11) {
            formatMobile = mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return formatMobile;
    }

    public static String formateXingIdCard(String card) {
        String formatMobile = card;
        if(isNotBlank(card) && card.length() > 15) {
            formatMobile = card.substring(0, 7) + "****" + card.substring(card.length() - 5);
        }
        return formatMobile;
    }


}
