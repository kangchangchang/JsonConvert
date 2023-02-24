//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.datacenter.entity;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class DateFormatBO {
    private String sourceDateStr;
    private String sourcePattern;
    private String targetPattern;

    public static DateFormatBO convertBO(Object sourceValue, String sTimeFormat, String tTimeFormat) {
        return !Objects.isNull(sourceValue) && !StringUtils.isEmpty(sTimeFormat) && !StringUtils.isEmpty(tTimeFormat) ? (new DateFormatBO()).setSourceDateStr(String.valueOf(sourceValue)).setSourcePattern(sTimeFormat).setTargetPattern(tTimeFormat) : null;
    }

    public String getSourceDateStr() {
        return this.sourceDateStr;
    }

    public String getSourcePattern() {
        return this.sourcePattern;
    }

    public String getTargetPattern() {
        return this.targetPattern;
    }

    public DateFormatBO setSourceDateStr(final String sourceDateStr) {
        this.sourceDateStr = sourceDateStr;
        return this;
    }

    public DateFormatBO setSourcePattern(final String sourcePattern) {
        this.sourcePattern = sourcePattern;
        return this;
    }

    public DateFormatBO setTargetPattern(final String targetPattern) {
        this.targetPattern = targetPattern;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DateFormatBO)) {
            return false;
        } else {
            DateFormatBO other = (DateFormatBO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$sourceDateStr = this.getSourceDateStr();
                    Object other$sourceDateStr = other.getSourceDateStr();
                    if (this$sourceDateStr == null) {
                        if (other$sourceDateStr == null) {
                            break label47;
                        }
                    } else if (this$sourceDateStr.equals(other$sourceDateStr)) {
                        break label47;
                    }

                    return false;
                }

                Object this$sourcePattern = this.getSourcePattern();
                Object other$sourcePattern = other.getSourcePattern();
                if (this$sourcePattern == null) {
                    if (other$sourcePattern != null) {
                        return false;
                    }
                } else if (!this$sourcePattern.equals(other$sourcePattern)) {
                    return false;
                }

                Object this$targetPattern = this.getTargetPattern();
                Object other$targetPattern = other.getTargetPattern();
                if (this$targetPattern == null) {
                    if (other$targetPattern != null) {
                        return false;
                    }
                } else if (!this$targetPattern.equals(other$targetPattern)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DateFormatBO;
    }


}
