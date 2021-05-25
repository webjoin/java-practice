package com.iquickmove.base.enums;

public enum UserType implements CodeEnum {

    /**
     * weixin
     */
    WEI_XIN("weixin"),
    ;

    private String code;

    UserType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return null;
    }
}
