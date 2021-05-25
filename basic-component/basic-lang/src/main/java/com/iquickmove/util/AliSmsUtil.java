package com.iquickmove.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.iquickmove.base.config.AliyunSmsConfig;
import com.iquickmove.base.consts.SmsConst;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ancoka
 * @version V1.0
 * @package net.wangxu.bm.util
 * @date 2020-07-26 18:02
 * @description
 */
@Slf4j
@Component("AliSmsUtil")
@AllArgsConstructor
public class AliSmsUtil {

  private AliyunSmsConfig aliyunSmsConfig;

  /**
   * 发送短信
   * @param mobile
   * @param param  模板中参数 json
   */
  public void sendSms(String mobile, Map<String,String> param, String templateId) {
    DefaultProfile profile = DefaultProfile.getProfile(aliyunSmsConfig.getRegionId(), aliyunSmsConfig.getAccessKeyId(), aliyunSmsConfig.getSecret());
    IAcsClient client = new DefaultAcsClient(profile);

    CommonRequest request = new CommonRequest();
    request.setSysMethod(MethodType.POST);
    request.setSysDomain("dysmsapi.aliyuncs.com");
    request.setSysVersion("2017-05-25");
    request.setSysAction("SendSms");
    request.putQueryParameter("RegionId", aliyunSmsConfig.getRegionId());
    request.putQueryParameter("PhoneNumbers", mobile);
    request.putQueryParameter("SignName", SmsConst.SMS_SIGN);
    request.putQueryParameter("TemplateCode", templateId);
    request.putQueryParameter("TemplateParam", JsonUtil.toJsonArr(param));
    try {
      CommonResponse response = client.getCommonResponse(request);
      log.info("AliSmsUtil.sendSms.req:{},result:{}",JsonUtil.toJsonArr(request),JsonUtil.toJsonStr(response));
    } catch (Exception e) {
      log.error("AliSmsUtil.sendSms.req:{},err:{}", JsonUtil.toJsonArr(request), ExceptionUtils.getStackTrace(e));
    }
  }
}
