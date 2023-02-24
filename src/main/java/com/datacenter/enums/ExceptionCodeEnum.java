package com.datacenter.enums;




//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.datacenter.exception.IBaseCodeType;

import java.util.HashMap;
import java.util.Map;

public enum ExceptionCodeEnum implements IBaseCodeType {
    SYSTEM_ERROR("95150000", "系统维护中，请稍后重试!"),
    DATE_FORMAT_ERROR("95150001", "时间格式化异常!"),
    SOURCE_DATE_EMPTY("95150002", "时间格式缺少sourceDate!"),
    SOURCE_FORMAT_EMPTY("95150003", "时间格式缺少sourceFormat!"),
    TARGET_FORMAT_EMPTY("95150004", "时间格式缺少targetFormat!"),
    PARAM_EMPTY_ERROR("95150005", "参数不能为空!"),
    PARAM_FORMAT_ERROR("95150006", "参数格式错误!");

    private static Map<String, ExceptionCodeEnum> pool = new HashMap();
    private String code;
    private String msg;

    private ExceptionCodeEnum(String code, String msg) {
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
        ExceptionCodeEnum[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            ExceptionCodeEnum each = var0[var2];
            ExceptionCodeEnum defined = (ExceptionCodeEnum)pool.get(each.getCode());
            if (null != defined) {
                throw new IllegalArgumentException(defined.toString() + " defined as same code with " + each.toString());
            }

            pool.put(each.getMsg(), each);
        }

    }
}
