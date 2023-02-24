package com.datacenter.enums;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.datacenter.exception.IBaseCodeType;

import java.util.HashMap;
import java.util.Map;

public enum JsonConvertExceptionCodeTypeEnum implements IBaseCodeType {
    COMPUTE_TARGET_PATH_VALUE_ERROR("95001000", "计算目标节点及节点内容出现异常"),
    SOURCE_PATH_VALUE_EMPTY("95001001", "sourcePath未查询到对应的value，请确认配置信息！"),
    DICT_CODE_VALUE_EMPTY("95001002", "字典映射关系中未匹配到sourceValue映射关系"),
    DICT_CODE_EMPTY("95001003", "根据字典code未配置到字典映射关系"),
    UN_SUPPORT_TYPE_CONVERT("95001004", "当前不支持该类型转译");

    private static Map<String, JsonConvertExceptionCodeTypeEnum> pool = new HashMap();
    private String code;
    private String msg;

    private JsonConvertExceptionCodeTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    static {
        JsonConvertExceptionCodeTypeEnum[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            JsonConvertExceptionCodeTypeEnum each = var0[var2];
            JsonConvertExceptionCodeTypeEnum defined = (JsonConvertExceptionCodeTypeEnum)pool.get(each.getCode());
            if (null != defined) {
                throw new IllegalArgumentException(defined.toString() + " defined as same code with " + each.toString());
            }

            pool.put(each.getMsg(), each);
        }

    }
}
