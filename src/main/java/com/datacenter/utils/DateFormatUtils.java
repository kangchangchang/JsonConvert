package com.datacenter.utils;

/**
 * @author pc
 * @date Create in  2023/2/24
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.datacenter.convert.JacksonUtils;
import com.datacenter.entity.DateFormatBO;
import com.datacenter.enums.ExceptionCodeEnum;
import com.datacenter.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormatUtils {
    private static final Logger log = LoggerFactory.getLogger(DateFormatUtils.class);

    public DateFormatUtils() {
    }

    public static String format(DateFormatBO mappingBO) {
        AssertUtils.notNull(mappingBO, ExceptionCodeEnum.DATE_FORMAT_ERROR);
        AssertUtils.isNotBlank(mappingBO.getSourceDateStr(), ExceptionCodeEnum.SOURCE_DATE_EMPTY);
        AssertUtils.isNotBlank(mappingBO.getSourcePattern(), ExceptionCodeEnum.SOURCE_FORMAT_EMPTY);
        AssertUtils.isNotBlank(mappingBO.getTargetPattern(), ExceptionCodeEnum.TARGET_FORMAT_EMPTY);
        log.info("convert dateFormat... param in:{}", JacksonUtils.toJson(mappingBO));
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern(mappingBO.getTargetPattern());
        String result = null;

        try {
            String sourceTime = mappingBO.getSourceDateStr().replaceAll(" ", " ");
            LocalDateTime datetime = LocalDateTimeUtils.parse(sourceTime, mappingBO.getSourcePattern());
            result = tFormatter.format(datetime);
            return result;
        } catch (Exception var5) {
            log.error("date format error... e:{}", var5.getMessage());
            throw new ServiceException(ExceptionCodeEnum.DATE_FORMAT_ERROR);
        }
    }
}
