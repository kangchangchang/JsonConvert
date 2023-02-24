package com.datacenter.entity;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.math.BigDecimal;

public class ConvertValueBO {
    private String sourcePath;
    private String targetPath;
    private String dictCode;
    private Integer targetType;
    private BigDecimal convertFactor;
    private Integer convertPrecision;
    private String sTimeFormat;
    private String tTimeFormat;

    public ConvertValueBO() {
    }

    public String getSourcePath() {
        return this.sourcePath;
    }

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

    public void setSourcePath(final String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setTargetPath(final String targetPath) {
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

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ConvertValueBO)) {
            return false;
        } else {
            ConvertValueBO other = (ConvertValueBO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label107: {
                    Object this$targetType = this.getTargetType();
                    Object other$targetType = other.getTargetType();
                    if (this$targetType == null) {
                        if (other$targetType == null) {
                            break label107;
                        }
                    } else if (this$targetType.equals(other$targetType)) {
                        break label107;
                    }

                    return false;
                }

                Object this$convertPrecision = this.getConvertPrecision();
                Object other$convertPrecision = other.getConvertPrecision();
                if (this$convertPrecision == null) {
                    if (other$convertPrecision != null) {
                        return false;
                    }
                } else if (!this$convertPrecision.equals(other$convertPrecision)) {
                    return false;
                }

                Object this$sourcePath = this.getSourcePath();
                Object other$sourcePath = other.getSourcePath();
                if (this$sourcePath == null) {
                    if (other$sourcePath != null) {
                        return false;
                    }
                } else if (!this$sourcePath.equals(other$sourcePath)) {
                    return false;
                }

                label86: {
                    Object this$targetPath = this.getTargetPath();
                    Object other$targetPath = other.getTargetPath();
                    if (this$targetPath == null) {
                        if (other$targetPath == null) {
                            break label86;
                        }
                    } else if (this$targetPath.equals(other$targetPath)) {
                        break label86;
                    }

                    return false;
                }

                label79: {
                    Object this$dictCode = this.getDictCode();
                    Object other$dictCode = other.getDictCode();
                    if (this$dictCode == null) {
                        if (other$dictCode == null) {
                            break label79;
                        }
                    } else if (this$dictCode.equals(other$dictCode)) {
                        break label79;
                    }

                    return false;
                }

                label72: {
                    Object this$convertFactor = this.getConvertFactor();
                    Object other$convertFactor = other.getConvertFactor();
                    if (this$convertFactor == null) {
                        if (other$convertFactor == null) {
                            break label72;
                        }
                    } else if (this$convertFactor.equals(other$convertFactor)) {
                        break label72;
                    }

                    return false;
                }

                Object this$sTimeFormat = this.getSTimeFormat();
                Object other$sTimeFormat = other.getSTimeFormat();
                if (this$sTimeFormat == null) {
                    if (other$sTimeFormat != null) {
                        return false;
                    }
                } else if (!this$sTimeFormat.equals(other$sTimeFormat)) {
                    return false;
                }

                Object this$tTimeFormat = this.getTTimeFormat();
                Object other$tTimeFormat = other.getTTimeFormat();
                if (this$tTimeFormat == null) {
                    if (other$tTimeFormat != null) {
                        return false;
                    }
                } else if (!this$tTimeFormat.equals(other$tTimeFormat)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ConvertValueBO;
    }




}
