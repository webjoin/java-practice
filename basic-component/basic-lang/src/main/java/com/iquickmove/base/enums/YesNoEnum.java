package com.iquickmove.base.enums;

public enum YesNoEnum implements CodeEnum {

    /**
     *
     */
    YES("Y", true),

    NO("N", false);

    private final String code;
    private final boolean bool;

    public static YesNoEnum getByCode(String code) {
        YesNoEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            YesNoEnum type = var1[var3];
            if (type.getCode().equals(code)) {
                return type;
            }
        }

        return null;
    }

    YesNoEnum(String code, boolean bool) {
        this.code = code;
        this.bool = bool;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public boolean bool() {
        return this.bool;
    }
}
