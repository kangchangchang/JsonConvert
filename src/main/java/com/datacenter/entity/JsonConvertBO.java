package com.datacenter.entity;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class JsonConvertBO {
    @NotNull
    private String sourcePath;
    @NotNull
    private String targetPath;
    private String dictCode;
    private Integer targetType;
    private BigDecimal convertFactor;
    private Integer convertPrecision;
    private String sTimeFormat;
    private String tTimeFormat;

    public JsonConvertBO() {
    }

    @NotNull
    public String getSourcePath() {
        return this.sourcePath;
    }

    @NotNull
    public String getTargetPath() {
        return this.targetPath;
    }

    public String getDictCode() {
        return this.dictCode;
    }

    public Integer getTargetType() {
        return this.targetType;
    }

    public BigDecimal getConvertFactor() {
        return this.convertFactor;
    }

    public Integer getConvertPrecision() {
        return this.convertPrecision;
    }

    public String getSTimeFormat() {
        return this.sTimeFormat;
    }

    public String getTTimeFormat() {
        return this.tTimeFormat;
    }

    public void setSourcePath(@NotNull final String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setTargetPath(@NotNull final String targetPath) {
        this.targetPath = targetPath;
    }

    public void setDictCode(final String dictCode) {
        this.dictCode = dictCode;
    }

    public void setTargetType(final Integer targetType) {
        this.targetType = targetType;
    }

    public void setConvertFactor(final BigDecimal convertFactor) {
        this.convertFactor = convertFactor;
    }

    public void setConvertPrecision(final Integer convertPrecision) {
        this.convertPrecision = convertPrecision;
    }

    public void setSTimeFormat(final String sTimeFormat) {
        this.sTimeFormat = sTimeFormat;
    }

    public void setTTimeFormat(final String tTimeFormat) {
        this.tTimeFormat = tTimeFormat;
    }
}

