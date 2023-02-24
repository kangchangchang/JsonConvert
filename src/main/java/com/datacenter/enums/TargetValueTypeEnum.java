package com.datacenter.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.HashMap;
import java.util.Map;

public enum TargetValueTypeEnum {
    STRING(1, "String"),
    INTEGER(2, "Integer"),
    LONG(3, "Long"),
    BOOLEAN(4, "boolean"),
    BIG_DECIMAL(5, "BigDecimal");

    private static Map<Integer, TargetValueTypeEnum> pool = new HashMap();
    private Integer code;
    private String value;

    private TargetValueTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static TargetValueTypeEnum valueOf(Integer code) {
        return (TargetValueTypeEnum)pool.get(code);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    static {
        TargetValueTypeEnum[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            TargetValueTypeEnum each = var0[var2];
            pool.put(each.getCode(), each);
        }

    }
}
