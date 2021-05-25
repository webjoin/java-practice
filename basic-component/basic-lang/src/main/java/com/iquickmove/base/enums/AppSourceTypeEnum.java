package com.iquickmove.base.enums;

/**
 * @Author pinghui
 * @Description
 * @Date Created in 2020-11-08 14:08
 */
public enum AppSourceTypeEnum {
    DOCTOR_SOURCE(1, "医师端"),
    PM_SOURCE(2, "项目经理端"),
    RESEARCH_SOURCE(3, "研究机构");


    AppSourceTypeEnum(Integer value, String desc) {
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
