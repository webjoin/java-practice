package cn.iwuliao.trade.api.enums;

import cn.iwuliao.base.enums.CodeEnum;

/**
 * @author elijah
 */

public enum TradeStatus implements CodeEnum {

    /** 初始 */
    INIT("I", ""),

    /** 付款成功 */
    PAY_SUCCESS("PS", "")

    ;

    /**
     * code
     */
    private String code;
    /**
     * message
     */
    private String message;

    TradeStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
