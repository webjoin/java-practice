package com.iquickmove.base.consts;

/**
 * @Author pinghui
 * @Description
 * @Date Created in 2020-11-07 23:39
 */
public interface WxRedisConst {

    String WX_ACCESS_TOKEN = "wxToken:";

    String WX_QR_CODE = "wxQrCodeList";
    /**
     * 获取用户信息
     */
    String WX_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
}
