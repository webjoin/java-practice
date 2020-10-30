package cn.iwuliao.trade.api.enums;

import cn.iwuliao.base.enums.CodeEnum;

/**
 * @author elijah
 */

public enum TradeType implements CodeEnum {

    /** TS */
    TRADE_SIMPLE("TS", ""),

    /** TSA */
    TRADE_SIMPLE_A("TSA", "")

    ;

    /**
     * code
     */
    private String code;
    /**
     * message
     */
    private String message;

    TradeType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
