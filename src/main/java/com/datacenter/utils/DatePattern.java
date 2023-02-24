package com.datacenter.utils;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.apache.commons.lang3.time.FastDateFormat;

public interface DatePattern {
    String MM = "MM";
    String DD = "dd";
    String MMdd = "MMdd";
    String MMdd_HYPHEN = "MM-dd";
    String MMdd_SLASH = "MM/dd";
    String MMdd_CN = "MM月dd日";
    String yyMMdd = "yyMMdd";
    String yyMMdd_HYPHEN = "yy-MM-dd";
    String MMddyy_HYPHEN = "MM-dd-yy";
    String yyMMdd_SLASH = "yy/MM/dd";
    String MMddyy_SLASH = "MM/dd/yy";
    String yyMMdd_CN = "yy年MM月dd日";
    String yyyyMM_HYPHEN = "yyyy-MM";
    String yyyyMM = "yyyyMM";
    String yyyy = "yyyy";
    String yyyyMMdd = "yyyyMMdd";
    String yyyy_MM_dd = "yyyy-MM-dd";
    String MMDDYYYY_HYPHEN = "MM-dd-yyyy";
    String yyyyMMdd_SLASH = "yyyy/MM/dd";
    String MMddyyyy_SLASH = "MM/dd/yyyy";
    String yyyyMMdd_CN = "yyyy年MM月dd日";
    String HHmm_COLON = "HH:mm";
    String HHmmss_COLON = "HH:mm:ss";
    String HHmmss_CN = "HH时mm分ss秒";
    String HHmmss = "HHmmss";
    String MMdd_CN_HHmm_COLON = "MM月dd日 HH:mm";
    String MMdd_CN_HHmm_CN = "MM月dd日HH时mm分";
    String yyMMddHH = "yyMMddHH";
    String yyMMddHHmm = "yyMMddHHmm";
    String yyMMddHHmmss = "yyMMddHHmmss";
    String yyMMdd_HYPHEN_HHmmss_COLON = "yy-MM-dd HH:mm:ss";
    String yyyyMMdd_HYPHEN_HHmm_COLON = "yyyy-MM-dd HH:mm";
    String yyyyMMdd_SLASH_HHmm_COLON = "yyyy/MM/dd HH:mm";
    String yyyyMMdd_CN_HHmm_COLON = "yyyy年MM月dd日 HH:mm";
    String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    String yyyyMMdd_HYPHEN_HHmmss_COLON = "yyyy-MM-dd HH:mm:ss";
    String yyyyMMdd_HYPHEN_HHmmssSSS_COLON = "yyyy-MM-dd HH:mm:ss:SSS";
    String yyyyMMdd_CN_HHmmss_CN = "yyyy年MM月dd日HH时mm分ss秒";
    String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    FastDateFormat yyyyMMdd_HYPHEN_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    FastDateFormat HHmm_COLON_FORMAT = FastDateFormat.getInstance("HH:mm");
    FastDateFormat HHmmss_COLON_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
    FastDateFormat yyyyMMdd_HYPHEN_HHmmss_COLON_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    FastDateFormat yyyyMMdd_CN_HHmmss_CN_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日HH时mm分ss秒");
    String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
}
