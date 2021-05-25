package com.iquickmove.base.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangyu
 * @since 2017-11-16 20:07
 * <p/>
 * <b>
 * 作为常量 ,不能修改。除非你自己清楚知道
 * </b>
 */
public class Cons {

    /**
     * 客服电话
     */
    public interface KEFU{
        String PHONE="4006550285";
    }


    public interface Http {
        /**
         * 连接超时时间
         */
        int CONNECT_TIME_OUT = 5_000;
        /**
         * 读取超时时间
         */
        int READ_TIME_OUT = 5_000;

        /**
         * 连接超时时间
         */
        int CONNECT_LONG_TIME_OUT_LONG = 15_000;
        /**
         * 读取超时时间
         */
        int READ_LONG_TIME_OUT = 15_000;

        /**
         * 连接超时时间
         */
        int CONNECT_LONG_TIME_OUT_LONG_60 = 60_000;
        /**
         * 读取超时时间
         */
        int READ_LONG_TIME_OUT_60 = 60_000;
        String POST = "POST";

        String CONTENT_TYPE = "application/json";
    }

    public interface B_PHONE {
        /**
         * 10000000000
         */
        String ONE_ZERO = "10000000000";
        /**
         * 11111111111
         */
        String ONE_ONE = "11111111111";
    }


    public interface DataType {
        /**
         * Map 默认初始化大
         */
        int HASH_INITIALCAPACITY = 16;
    }

    public interface Wold {
    }

    /**
     * 业务字符
     */
    public interface Service {




        /**
         * 短信规则
         */
        String PHONE_REGX = "1[3456789][0-9]{9}";




    }

    /**
     * 符号专用类
     */
    public interface Chars {
        /**
         * "" 空字符
         */
        String EMPTY = "";
        String QUESTION = "?";
        String NULL = "null";
        /**
         * , 符号
         */
        String COMMA = ",";

        /*** | 符号 */
        String VERTICAL = "|";
        /**
         * " " 空格字符
         */
        String BLANK = " ";
        /**
         * ; 分号
         */
        String SEMICOLON = ";";
        /**
         * :
         * 冒号
         */
        String COLON = ":";
        /**
         * " 引号
         */
        String QUOTES = "\"";

        /**
         * + 加号
         */
        String PLUS = "+";

        /**
         * - 减号
         */
        String MINUS = "-";

        String DIAGONAL = "/";
        String NEW_LINE = "\n";
        String LEFT_BRACKET = "(";
        String LEFT_MID_BRACKET = "[";
        String LEFT_BIG_BRACKET = "{";
        String LEFT_ANGLE_BRACKET = "<";

        String RIGHT_BRACKET = ")";
        String RIGHT_MID_BRACKET_RIGHT = "]";
        String RIGHT_BIG_BRACKET_RIGHT = "}";
        String RIGHT_ANGLE_BRACKET_RIGHT = ">";

        String BIG_BRACKET = LEFT_BIG_BRACKET + RIGHT_BIG_BRACKET_RIGHT;
        String MID_BRACKET = LEFT_MID_BRACKET + RIGHT_MID_BRACKET_RIGHT;
        String BRACKET = LEFT_BRACKET + RIGHT_BRACKET;

        String EQUA = "=";
        String EXCLAMATION_MARK = "!";
        String AT = "@";
        String UNDERLINE = "_";
        String SHORTLINE = "-";
        String JING = "#";
//        String a = "$";
//        String a = "%";
//        String a = "^";
        String AND = "&";
//        String a = "*";
        String RMB = "￥";


    }





    /**
     * 预订包厢
     */
    public static Map<Integer, String> position = new HashMap<>();
    /**
     * 数字转汉字
     */
    public static Map<Integer, Character> VOICE_NUM = new HashMap<>();

    /**
     * 文件后缀对应MIME
     */
    public static String[][] MIME_TABLE = {
            {".bmp", "image/bmp"},
            {".gif", "image/gif"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".png", "image/png"},
            {".txt", "text/plain"},
            {".css", "text/css"},
            {".csv", "text/csv"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".xml", "text/xml"},
            {".mp3", "audio/mpeg"},
            {".wma", "audio/x-ms-wma"},
            {".mp4", "video/mp4"},
            {".avi", "video/x-msvideo"},
            {".3gp", "video/3gpp"},
            {".3gpp", "video/3gpp"},
            {".mov", "video/quicktime"},
            {".flv", "video/x-flv"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".wmv", "video/x-ms-wmv"},
            {".rmvb", "application/vnd.rn-realmedia-vbr"},
            {".rm", "application/vnd.rn-realmedia"},
            {".swf", "application/x-shockwave-flash"},
            {".7z", "application/x-7z-compressed"},
            {".apk", "application/vnd.android.package-archive"},
            {".ipa", "application/x-itunes-ipa"},
            {".pdf", "application/pdf"},
            {".tgz", "application/x-compressed"},
            {".gz", "application/x-gzip"},
            {".zip", "application/zip"},
            {".rar", "application/x-rar-compressed"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".tar", "application/x-tar"}
    };

    /**
     * 管理后台固定密码盐值
     */
    public static String PASSWORD_SALT = "666888";
}
