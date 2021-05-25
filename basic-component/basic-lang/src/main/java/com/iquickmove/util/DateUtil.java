package com.iquickmove.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;

/**
 */
public class DateUtil {

    public final static String HH_mm = "HH:mm";
    public final static String HH_mm_ss = "HH:mm:ss";
    public final static String HHmmss = "HHmmss";
    public static final String HH = "HH";
    public static final String HH_MM = "HH:mm";
    public static final String HHMM = "HHmm";
    public static final String MMDD = "MMdd";
    public static final String MM_DD = "MM-dd";
    public static final String MM_DD_CN = "MM月dd日";
    public final static String yyyy = "yyyy";
    public final static String yyyyMM = "yyyyMM";
    public final static String yyyy_MM = "yyyy-MM";
    public final static String yyyyMMdd = "yyyyMMdd";
    public final static String yyyy_d_MM_d_dd = "yyyy.MM.dd";
    public static final String YYYYMMDD_HH = "yyyy-MM-dd HH";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_CN = "yyyy年MM月dd日";
    public static final String MM_DD_HH_MM_CN = "MM月dd日 HH:mm";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHH_MM = "yyyyMMddHH:mm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDD_HH_MM = "yyyyMMdd HH:mm";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_CN = "yyyy年MM月dd日 HH:mm";
    public static final String YYYY_MM_DD_HH_MM_CN2 = "yyyy年MM月dd日 HH时mm分";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYYMMDD_HHMMSSSSS = "yyyyMMdd_HHmmssSSS";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public final static String YYYY_MM_DD1 = "yyyy/MM/dd";
    public static final String YYYY_MM_DDHH_MM = "yyyy/MM/dd HH:mm";



    /**
     * 根据表达式格式化日期
     *
     * @param date   时间
     * @param fomart 日式表达式
     * @return 格式化后的时间字符串
     */
    public static String format(LocalDateTime date, String fomart) {
        return date.format(DateTimeFormatter.ofPattern(fomart));
    }

    public static String format(LocalDate date, String fomart) {
        return date.format(DateTimeFormatter.ofPattern(fomart));
    }

    public static String getOrderDateStr(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(YYYYMMDD));
    }

    public static int getOrderDateInt(LocalDate date) {
        return Integer.parseInt(getOrderDateStr(date));
    }

    public static String format(LocalTime time, String fomart) {
        return time.format(DateTimeFormatter.ofPattern(fomart));
    }

    /**
     * @param dateStr 日期格式的数据  23:10
     * @param fomart  HH:mm
     * @return
     */
    public static LocalTime parse2Localtime(String dateStr, String fomart) {
        return LocalTime.parse(dateStr, DateTimeFormatter.ofPattern(fomart));
    }

    public static LocalDateTime parse2LocalDatetime(String dateStr, String fomart) {
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(fomart));
    }

    public static LocalDate parse2LocalDate(String dateStr, String fomart) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(fomart));
    }

    /**
     * 将就餐日期转化为LocalDate
     *
     * @param orderDate 就餐日期
     * @return
     */
    public static LocalDate parse2LocalDate4OrderDate(int orderDate) {
        return parse2LocalDate4OrderDate(String.valueOf(orderDate));
    }

    /**
     * 将就餐日期转化为LocalDate
     *
     * @param orderDate 就餐日期
     * @return
     */
    public static LocalDate parse2LocalDate4OrderDate(String orderDate) {
        return parse2LocalDate(orderDate, YYYYMMDD);
    }

    /**
     * 将就餐时间转化为LocalDate
     *
     * @param orderTime 就餐时间
     * @return
     */
    public static LocalTime parse2LocalDate4OrderTime(String orderTime) {
        return parse2Localtime(orderTime, HH_MM);
    }

    /**
     * @param orderDate 就餐日期
     * @param orderTime 就餐时间
     * @return
     */
    public static LocalDateTime parse2LocalDate4OrderDateTime(int orderDate, String orderTime) {
        LocalDate localDate = parse2LocalDate4OrderDate(orderDate);
        LocalTime localTime = parse2LocalDate4OrderTime(orderTime);
        return LocalDateTime.of(localDate, localTime);
    }

    /**
     * @param orderDate 就餐日期
     * @param orderTime 就餐时间
     * @return
     */
    public static LocalDateTime parse2LocalDate4OrderDateTime(String orderDate, String orderTime) {
        LocalDate localDate = parse2LocalDate4OrderDate(orderDate);
        LocalTime localTime = parse2LocalDate4OrderTime(orderTime);
        return LocalDateTime.of(localDate, localTime);
    }


    /**
     * 将时间格式的数据转化成另外一种格式 比如 英文格式转中文格式
     *
     * @param dateStr
     * @param oldFormat
     * @param newFormat
     * @return
     */
    public static String format2NewDateStr(String dateStr, String oldFormat, String newFormat) {
        LocalDate localDate = parse2LocalDate(dateStr, oldFormat);
        return format(localDate, newFormat);
    }

    /**
     * 将时间戳转成对应的格式时间
     *
     * @param milli  时间戳
     * @param fomart 格式化
     * @return
     */
    public static String format(long milli, String fomart) {
        LocalDateTime localDateTime = fromMilli(milli);
        return format(localDateTime, fomart);
    }

    /**
     * 根据字符串和表达式获取字符串表达日期为周几
     *
     * @param date     日期字符串
     * @param formater 日期字符串的表达式
     * @return the day-of-week, from 1 (Monday) to 7 (Sunday)
     */
    public static int getWeekFromString(String date, String formater) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formater);

        LocalDate localDate = LocalDate.from(dtf.parse(date));

        return localDate.getDayOfWeek().getValue();
    }

    /**
     * 获取营业开始时间  10:30:00.000
     *
     * @return
     */
    public static long getOpenStartTime() {
        int hour = 10, minute = 30, second = 0, nanoOfSecond = 0;
        return getTodayMilli4Spec(hour, minute, second, nanoOfSecond);
    }


    /**
     * 获取营业开始时间 21:30:00.000
     *
     * @return
     */
    public static long getOpenEndTime() {
        int hour = 21, minute = 30, second = 0, nanoOfSecond = 0;
        return getTodayMilli4Spec(hour, minute, second, nanoOfSecond);
    }

    public static LocalDate getToday() {
        return LocalDate.now();
    }

    /**
     * @return yyyyMMdd
     */
    public static int getTodayInt() {
        return Integer.parseInt(format(getToday(), YYYYMMDD));
    }

    /**
     * @return yyyyMMdd
     */
    public static String getTodayIntStr() {
        return format(getToday(), YYYYMMDD);
    }

    /**
     * @return yyyy-MM-dd
     */
    public static String getTodayStr() {
        return format(getToday(), YYYY_MM_DD);
    }

    private static long getTodayMilli4Spec(int hour, int minute, int second, int nanoOfSecond) {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.of(hour, minute, second, nanoOfSecond);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return toMilli(localDateTime);
    }

    /**
     * 时间戳获取 LocalDateTime
     *
     * @param milli
     * @return
     */
    public static LocalDateTime fromMilli(long milli) {
        //将10位时间转化为13位  php都是10位
        if (milli<9999999999L){
            milli = milli*1000;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    /**
     * 从 LocalDateTime 中获取时间戳
     *
     * @param localDateTime
     * @return
     */
    public static long toMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long toMilli(String dateStr, String fomart) {
        return toMilli(LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(fomart)));
    }

    public static long toMilli(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    /**
     * 根据 localDateTime 获取 Instant
     *
     * @param localDateTime
     * @return Instant
     */
    public static Instant fromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }


    /********************************************************
     * Date to String
     *******************************************************/



    /**
     * 转化当前时间为指定格式
     *
     * @param format
     * @return
     */
    public static String getNowDate2String(String format) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }



    /**
     * 转化指定时间为指定格式
     * <p>
     * localDateTime is null, 则返回""
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static String getDate2String(LocalDateTime localDateTime, String format) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /********************************************************
     * String to Date
     *******************************************************/

    /**
     * 转化指定时间为默认格式,默认格式:yyyy-MM-dd HH:mm:ss
     * <p>
     * 如果解析失败,则返回null
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getString2Date(String localDateTime) {
        return getString2Date(localDateTime, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 转化指定时间为指定格式
     * <p>
     * 如果解析失败,则返回null
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static LocalDateTime getString2Date(String localDateTime, String format) {
        try {

            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern(format));
        } catch (RuntimeException e) {
            return null;
        }
    }

    /********************************************************
     * 获取当前的时间值
     *******************************************************/

    /**
     * 获取当前秒数
     *
     * @return
     */
    public static long getSecondTime() {
        return Clock.systemDefaultZone().instant().getEpochSecond();
    }

    /**
     * 获取指定时间的秒数
     *
     * @param localDateTime
     * @return
     */
    public static long getSecondTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取当前时间毫秒数
     *
     * @return
     */
    public static long getMilliSecondsTime() {
        return Clock.systemDefaultZone().millis();
    }

    /**
     * 获取指定时间毫秒数
     *
     * @return
     */
    public static long getMilliSecondsTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /********************************************************
     * 获取开始结束的起始时间 - 天
     *******************************************************/

    /**
     * 获取当前开始时间,默认格式:yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTodayBegin2String() {
        return getTodayBegin().format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 获取当前结束时间,默认格式:yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTodayEnd2String() {
        return getTodayEnd().format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 获取当前开始时间
     *
     * @return
     */
    public static LocalDateTime getTodayBegin() {
        return getBegin4Date(LocalDateTime.now());
    }

    /**
     * 获取当前结束时间
     *
     * @return
     */
    public static LocalDateTime getTodayEnd() {
        return getEnd4Date(LocalDateTime.now());
    }

    /**
     * 获取指定时间的开始时间
     *
     * @param localDateTime
     * @return
     */
    public static String getBegin4Date2String(LocalDateTime localDateTime) {
        return getBegin4Date(localDateTime).format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 获取指定时间的结束时间
     *
     * @param localDateTime
     * @return
     */
    public static String getEnd4Date2String(LocalDateTime localDateTime) {
        return getEnd4Date(localDateTime).format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 获取指定时间的开始时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getBegin4Date(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }
    public static LocalDateTime getBegin4Date(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    /**
     * 获取指定时间的结束时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getEnd4Date(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }

    public static LocalDateTime getEnd4Date(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }

    /********************************************************
     * 获取开始结束的起始时间 - 月
     *******************************************************/

    /**
     * 获取当前月初 - yyyy-MM-dd
     *
     * @return
     */
    public static String getMonthBegin2String() {
        return getMonthBegin().format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    /**
     * 获取当前月末 - yyyy-MM-dd
     *
     * @return
     */
    public static String getMonthEnd2String() {
        return getMonthEnd().format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    /**
     * 获取当前月初 - yyyy-MM-dd
     *
     * @return
     */
    public static LocalDate getMonthBegin() {
        return getBegin4Month(LocalDate.now());
    }

    /**
     * 获取当前月末 - yyyy-MM-dd
     *
     * @return
     */
    public static LocalDate getMonthEnd() {
        return getEnd4Month(LocalDate.now());
    }

    /**
     * 获取指定日期月初 - yyyy-MM-dd
     *
     * @param localDate
     * @return
     */
    public static String getBegin4Month2String(LocalDate localDate) {
        return getBegin4Month(localDate).format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    /**
     * 获取指定日期月末 - yyyy-MM-dd
     *
     * @param localDate
     * @return
     */
    public static String getEnd4Month2String(LocalDate localDate) {
        return getEnd4Month(localDate).format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    /**
     * 获取当前月 - yyyy-MM
     *
     * @return
     */
    public static String getTodayMonth2String() {
        return getMonth2String(LocalDate.now());
    }

    /**
     * 获取指定月 - yyyy-MM
     *
     * @param localDate
     * @return
     */
    public static String getMonth2String(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(yyyy_MM));
    }

    /**
     * 获取指定日期月初 - yyyy-MM-dd
     *
     * @param localDate
     * @return
     */
    public static LocalDate getBegin4Month(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期月末 - yyyy-MM-dd
     *
     * @param localDate
     * @return
     */
    public static LocalDate getEnd4Month(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /********************************************************
     * 获取开始结束的起始时间 - 年
     *******************************************************/

    /**
     * 获取当前年初 - yyyy-MM
     *
     * @return
     */
    public static String getYearBegin2String() {
        return getYearBegin().format(DateTimeFormatter.ofPattern(yyyy_MM));
    }

    /**
     * 获取当前年末 - yyyy-MM
     *
     * @return
     */
    public static String getYearEnd2String() {
        return getYearEnd().format(DateTimeFormatter.ofPattern(yyyy_MM));
    }

    /**
     * 获取当前年 - yyyy
     *
     * @return
     */
    public static String getTodayYear2String() {
        return getYear2String(LocalDate.now());
    }

    /**
     * 获取指定年 - yyyy
     *
     * @param localDate
     * @return
     */
    public static String getYear2String(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(yyyy));
    }

    /**
     * 获取当前年初 - yyyy-MM
     *
     * @return
     */
    public static LocalDate getYearBegin() {
        return getBegin4Year(LocalDate.now());
    }

    /**
     * 获取当前年末 - yyyy-MM
     *
     * @return
     */
    public static LocalDate getYearEnd() {
        return getEnd4Year(LocalDate.now());
    }

    /**
     * 获取指定时间年初 - yyyy-MM
     *
     * @return
     */
    public static String getBegin4Year2String(LocalDate localDate) {
        return getBegin4Year(localDate).format(DateTimeFormatter.ofPattern(yyyy_MM));
    }

    /**
     * 获取指定时间年末 - yyyy-MM
     *
     * @return
     */
    public static String getEnd4Year2String(LocalDate localDate) {
        return getEnd4Year(localDate).format(DateTimeFormatter.ofPattern(yyyy_MM));
    }

    /**
     * 获取指定时间年初 - yyyy-MM
     *
     * @return
     */
    public static LocalDate getBegin4Year(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取指定时间年末 - yyyy-MM
     *
     * @return
     */
    public static LocalDate getEnd4Year(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }

    /********************************************************
     * 时间差
     *******************************************************/

    /**
     * 时间差,单位:天
     *  会有问题  见diffDay1
     * @param fromLocalDate
     * @param toLocalDate
     * @return
     */
    @Deprecated
    public static int diffDay(LocalDate fromLocalDate, LocalDate toLocalDate) {
        return (int) Duration.between(fromLocalDate, toLocalDate).toDays();
    }


    public static int diffDay1(LocalDate fromLocalDate, LocalDate toLocalDate) {
        return (int)fromLocalDate.until(toLocalDate, ChronoUnit.DAYS);
    }

    /**
     * 时间差,单位:小时
     *
     * @param fromLocalTime
     * @param toLocalTime
     * @return
     */
    public static int diffHour(LocalTime fromLocalTime, LocalTime toLocalTime) {
        return (int) Duration.between(fromLocalTime, toLocalTime).toHours();
    }

    /**
     * 时间差,单位:分钟
     *
     * @param fromLocalTime
     * @param toLocalTime
     * @return
     */
    public static int diffMin(LocalTime fromLocalTime, LocalTime toLocalTime) {
        return (int) Duration.between(fromLocalTime, toLocalTime).toMinutes();
    }

    /**
     * 时间差,单位:秒
     *
     * @param fromLocalTime
     * @param toLocalTime
     * @return
     */
    public static int diffSecond(LocalTime fromLocalTime, LocalTime toLocalTime) {
        return (int) ChronoUnit.SECONDS.between(fromLocalTime, toLocalTime);
    }

    /********************************************************
     * 其他使用
     *******************************************************/

    /**
     * 得到指定星期几(星期天返回7)
     *
     * @return
     */
    public static int getDayOfWeekCN(LocalDate localDate) {
        return localDate.getDayOfWeek().getValue();
    }

    /**
     * 按中国习惯，返回今天是星期几(星期天返回7)
     *
     * @return
     */
    public static int getDayOfWeekCN() {
        return getDayOfWeekCN(LocalDate.now());
    }

    /**
     * 得到指定日期是几号
     *
     * @param localDate
     * @return
     */
    public static int getDayOfMonth(LocalDate localDate) {
        return localDate.getDayOfMonth();
    }

    /**
     * 得到今天是几月
     *
     * @return
     */
    public static int getDayOfMonth() {
        return getDayOfMonth(LocalDate.now());
    }

    /**
     * 得到指定日期的月份
     *
     * @param localDate
     * @return
     */
    public static int getMonth(LocalDate localDate) {
        return localDate.getMonth().getValue();
    }

    /**
     * 获取当前日期的月份
     * @param
     * @return  形如 201901
     */
    public static String getFromateMonth(LocalDate date,String format) {
        return format(date,format);
    }

    /**
     * 得到今天是几月
     *
     * @return
     */
    public static int getMonth() {
        return getMonth(LocalDate.now());
    }

    /**
     * 得到指定日期的小时
     *
     * @param localTime
     * @return
     */
    public static int getHour(LocalTime localTime) {
        return localTime.getHour();
    }

    /**
     * 得到指定日期的分钟
     *
     * @param localTime
     * @return
     */
    public static int getMinute(LocalTime localTime) {
        return localTime.getMinute();
    }


    /********************************************************
     * 扩展
     *******************************************************/


    public LocalDateTime instant2LocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public Instant LocalDateTime2instant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime getLongToLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 时间戳(秒) 转化成  LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getLongToLocalDateTimeForSecond(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp * 1000);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }


    public static LocalDateTime timestampToDatetime(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime getNowLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now;
    }



    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }


    /**
     *  日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field){
        return time.minus(number,field);
    }


    public static Duration getBetween(LocalDateTime fromLocalDate, LocalDateTime toLocalDate) {
        return Duration.between(fromLocalDate, toLocalDate);
    }

    public static void main(String[] args) {
        System.out.println(getMilliSecondsTime());

        int from = getTodayInt();
        int to = DateUtil.getOrderDateInt(DateUtil.plus(LocalDateTime.now(), 3, ChronoUnit.DAYS).toLocalDate());
        System.out.println("to> " + to);
        int ca = diffDay1(DateUtil.parse2LocalDate4OrderDate(from), DateUtil.parse2LocalDate4OrderDate(to));
        System.out.println("ca> " + ca);

        String format = DateUtil.format(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)), DateUtil.YYYYMMDDHHMM);
        System.out.println(format);

    }
}
