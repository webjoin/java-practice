package com.iquickmove.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ancoka
 * @version V1.0
 * @package com.iquickmove.util
 * @date 2020/11/14 9:32 下午
 * @description 数据脱敏类
 */
public class MaskUtil {

    /**
     * 手机号脱敏，隐藏中间4位
     * @param mobile
     * @return
     */
    public static String mobileMask(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 截取手机哈
     * @param mobile
     * @return
     */
    public static String mobileCutOut(String mobile) {
        if (StringUtils.isEmpty(mobile) || mobile.length() < 4) {
            return mobile;
        }
        int length = mobile.length();
        return mobile.substring(length-4, length);
    }
}
