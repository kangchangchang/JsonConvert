package com.datacenter.utils;

import com.datacenter.exception.IBaseCodeType;
import com.datacenter.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

/**
 * @author pc
 * @date Create in  2023/2/24
 */
public class AssertUtils {
    public AssertUtils() {
    }

    public static void isTrue(boolean expression, IBaseCodeType baseCodeType) {
        if (!expression) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void isFalse(boolean expression, IBaseCodeType baseCodeType) {
        if (expression) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void isNotBlank(@Nullable String text, IBaseCodeType baseCodeType) {
        if (StringUtils.isBlank(text)) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void isBlank(String str, IBaseCodeType baseCodeType) {
        if (StringUtils.isNotBlank(str)) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void isNull(@Nullable Object object, IBaseCodeType baseCodeType) {
        if (object != null) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void notNull(@Nullable Object object, IBaseCodeType baseCodeType) {
        if (object == null) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, IBaseCodeType baseCodeType) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(baseCodeType);
        }
    }

    public static void notEmpty(@Nullable Object[] array, IBaseCodeType baseCodeType) {
        if (ObjectUtils.isEmpty(array)) {
            throw new ServiceException(baseCodeType);
        }
    }
}
