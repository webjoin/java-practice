package cn.iwuliao.base.enums;

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
