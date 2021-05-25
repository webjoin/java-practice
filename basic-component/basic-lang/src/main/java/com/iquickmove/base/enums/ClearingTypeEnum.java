package com.iquickmove.base.enums;

public enum ClearingTypeEnum {

    ONLINE_PAY(1, "线上结算"),
    OFFLINE_PAY(2, "线下结算");


    ClearingTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private Integer value;

    private String desc;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
