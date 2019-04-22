package cn.iwuliao.cons;

public class Cons {


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
//        String a = "#";
//        String a = "$";
//        String a = "%";
//        String a = "^";
        String AND = "&";
//        String a = "*";


    }


}
