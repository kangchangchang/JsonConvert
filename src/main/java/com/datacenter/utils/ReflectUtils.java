package com.datacenter.utils;

import com.datacenter.convert.JacksonUtils;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
public class ReflectUtils {
    public ReflectUtils() {
    }

    public static void setProperty(Object bean, Field field, String name, Object value) {
        try {
            if (bean != null && value != null) {
                if (field != null) {
                    field.setAccessible(true);
                    if (!field.getType().isAssignableFrom(value.getClass()) && field.getType().equals(Date.class)) {
                        Date date = DateUtils.parse(value.toString());
                        field.set(bean, date);
                    } else {
                        if (field.getType().equals(value.getClass())) {
                            field.set(bean, value);
                        } else {
                            BeanUtils.setProperty(bean, name, value);
                        }

                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException var5) {
            throw new RuntimeException(var5);
        }
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            try {
                field.set(obj, value);
            } catch (IllegalAccessException var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(fieldName, "fieldName can't be blank", new Object[0]);
        Class superClass = obj.getClass();

        while(superClass != Object.class) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Map<String, Field> getFieldFlatMap(final Class<?> clazz) {
        Map<String, Field> fieldMap = Maps.newHashMap();

        for(Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            Field[] var4 = fields;
            int var5 = fields.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Field field = var4[var6];
                if (!fieldMap.containsKey(field.getName())) {
                    fieldMap.put(field.getName(), field);
                }
            }
        }

        return fieldMap;
    }

    public static Object getFieldValue(Object obj, String key) throws Exception {
        Field field = getAccessibleField(obj, key);
        if (field == null) {
            throw new NoSuchFieldException("获取filed值没有该字段, obj=" + JacksonUtils.toJson(obj) + ", key=" + key);
        } else {
            return field.get(obj);
        }
    }
}

