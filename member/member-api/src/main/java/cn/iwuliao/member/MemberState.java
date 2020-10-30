package cn.iwuliao.member;

import cn.iwuliao.base.enums.CodeEnum;

/**
 * @author elijah
 */

public enum MemberState implements CodeEnum {

    /**
     * 正常
     */
    OK("O", "正常"),

    LOCK("L", "冻结"),

    ;

    private String code;
    private String message;

    MemberState(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ;

    @Override
    public String getCode() {
        return code;
    }
}
