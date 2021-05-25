package com.iquickmove.base.enums;

/**
 * 收入类型  1访视   2知情、入组、初审
 */
public enum  IncomeTypeEnum {

    VISIT(1,"访视",1),
    INCLUSION(2,"知情、入组、初审",1),
    WITHDRAW_RETURN(3,"提现返还",1),



    WITHDRAW(11,"提现",2)
    ;

    /**
     * code
     */
    private int type;
    /**
     * message
     */
    private String desc;

    /**
     * 1:进项(+) 2:出项(-)
     */
    private int inOrOut;

    IncomeTypeEnum(int type, String desc,int inOrOut) {
        this.type = type;
        this.desc = desc;
        this.inOrOut = inOrOut;
    }

    public int getType() {
        return type;
    }

    public int getInOrOut() {
        return inOrOut;
    }

    public String getDesc() {
        return desc;
    }

    public static String getInOrOutType(int type) {
        IncomeTypeEnum[] values = IncomeTypeEnum.values();
        for (IncomeTypeEnum value : values) {
            if (value.type == type) {
                return value.desc;
            }
        }
        return null;
    }

    public static IncomeTypeEnum getInOrOut(int type) {
        IncomeTypeEnum[] values = IncomeTypeEnum.values();
        for (IncomeTypeEnum value : values) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }
}
