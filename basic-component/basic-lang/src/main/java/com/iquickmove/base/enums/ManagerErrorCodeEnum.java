package com.iquickmove.base.enums;

/**
 * @Author pinghui
 * @Description
 * @Date Created in 2020-11-21 19:02
 */
public enum ManagerErrorCodeEnum implements CodeMessage{

    CAPTCHA_ERROR("100001", "验证码错误"),

    USER_PASSWORD_NOT_MATCH("100002", "帐号密码不匹配"),
    USER_NOT_FOUND("100003", "用户不存在"),
    GET_CAPTCHA_ERROR("100004", "获取验证码错误"),
    HAS_NO_AUTH_ERROR("100005", "暂无权限查看"),
    MENU_NAME_REPEAT("100006", "菜单名称重复"),
    MENU_PATH_PREFIX_ERROR("100007", "菜单路径不对"),
    MENU_PARENT_ERROR("100008", "父级菜单路径不对"),
    MENU_HAS_CHILD("100009", "存在子菜单,不允许删除"),
    MENU_HAS_ROLE("100010", "菜单已经分配,不允许删除"),

    ;

    ManagerErrorCodeEnum(String code, String message) {
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
