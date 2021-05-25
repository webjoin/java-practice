package com.iquickmove.base.consts;

/**
 * Created on 2019/4/6.
 */
public interface SmsConst {



  /**
   *  短信发送模板
   */
  String SMS_TEMPID_REGISTER_CHECK_CODE = "";

  /**
   * 实名认证验证码  验证码${code}，您正在进行身份验证，打死不要告诉别人哦！
   */
  String SMS_TEMPID_REAL_NAME_CHECK_CODE = "SMS_205300749";

  /**
   * 您正在财务审核，验证码${code}，请在五分钟内提交验证码，切匆将验证码泄漏与他人。
   */
  String SMS_TEMPID_FINANCE_AUDIT_CHECK_CODE = "SMS_205456464";

  String VISIT_NOTICE_SMS_TEMPLATE = ""; // todo

  /**
   * 签名
   */
  String SMS_SIGN = "快募";


}
