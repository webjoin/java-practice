package com.iquickmove.base.enums;

public enum ReturnCode implements CodeMessage {
    /**
     * 系统异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),

    INVALID_PARAMETER("INVALID_PARAMETER", "无效参数"),

    UNAUTHORIZED("UNAUTHORIZED", "认证失败"),

    BIZ_CHECK_FAIL("BIZ_CHECK_FAIL", "业务校验失败"),

    NOT_EXIST("NOT_EXIST", "数据不存在"),

    IDEMPOTENCE_FAIL("IDEMPOTENCE_FAIL", "幂等失败"),

    DOCTOR_NOT_FOUND("10022", "没找到该医师"),
    DOCTOR_WEIXIN_INFO_ERROR("10023", "微信解密失败"),
    DOCTOR_WEIXIN_SUB_ERROR("10024", "微信关注失败"),
    DOCTOR_ACCOUNT_ERROR("10025", "未开通账户"),
    DOCTOR_ACCOUNT_BUZU("10026", "账户余额不足"),
    DOCTOR_ACCOUNT_STATUS_ERROR("10027", "该账户暂不可提现"),
    DOCTOR_QUALIFICATION_NO("10028", "未认证"),
    DOCTOR_WITHDRAW_LIMIT("10029", "每人每月最多提现10万"),


    TOKEN_HAS_EXPIRED("TOKEN_HAS_EXPIRED", "登陆token已失效"),

    ;

    ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
