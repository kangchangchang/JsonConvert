package com.datacenter.utils;

/**
 * @author pc
 * @date Create in  2023/2/24
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanUtils extends org.springframework.beans.BeanUtils {
    public BeanUtils() {
    }

    public static <T> T copyBean(Object source, Class<T> targetClass) {
        return copyBean(source, targetClass, (String[])null);
    }

    public static <T> T copyBean(Object source, Class<T> targetClass, String... ignoreFields) {
        if (source == null) {
            return null;
        } else if (targetClass == null) {
            throw new IllegalArgumentException("parameter targetClass should not be null");
        } else {
            try {
                T target = targetClass.getDeclaredConstructor().newInstance();
                copyProperties(source, target, ignoreFields);
                return target;
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        } else {
            List<T> targetList = Lists.newArrayListWithCapacity(sourceList.size());
            Iterator var3 = sourceList.iterator();

            while(var3.hasNext()) {
                Object source = var3.next();
                T target = copyBean(source, targetClass);
                targetList.add(target);
            }

            return targetList;
        }
    }

    public static Map<String, Object> bean2Map(Object bean) {
        return bean2Map(bean, Object.class, false, (List)null);
    }

    public static <T> Map<String, T> bean2Map(Object bean, Class<T> clazz, boolean ignoreNullVal, List<String> ignoreFieldList) {
        if (bean == null) {
            return null;
        } else if (bean instanceof Map) {
            return (Map)bean;
        } else {
            Map<String, Field> fieldMap = ReflectUtils.getFieldFlatMap(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(bean.getClass());
            HashMap map = Maps.newHashMapWithExpectedSize(propertyDescriptors.length);

            try {
                PropertyDescriptor[] var7 = propertyDescriptors;
                int var8 = propertyDescriptors.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    PropertyDescriptor property = var7[var9];
                    String key = property.getName();
                    if (!"class".equals(key)) {
                        if (!fieldMap.containsKey(key)) {
                            key = StringUtils.capitalize(key);
                            if (!fieldMap.containsKey(key)) {
                                continue;
                            }
                        }

                        if ((ignoreFieldList == null || !ignoreFieldList.contains(key)) && (clazz.equals(Object.class) || clazz.isAssignableFrom(property.getPropertyType()) || clazz.equals(String.class))) {
                            Method getter = property.getReadMethod();
                            if (getter != null) {
                                Object value = getter.invoke(bean);
                                if (!ignoreNullVal || value != null) {
                                    if (clazz.equals(String.class) && !clazz.isAssignableFrom(property.getPropertyType()) && value != null) {
                                        String strVal = value.toString();
                                        if (value instanceof Date) {
                                            strVal = DateUtils.format((Date)value);
                                        } else if (property.getPropertyType().isArray()) {
                                            Object[] objArr = (Object[])((Object[])value);
                                            List<?> list = Arrays.asList(objArr);
                                            strVal = list.toString();
                                        }

                                        map.put(key, strVal);
                                    } else {
                                        map.put(key, value);
                                    }
                                }
                            }
                        }
                    }
                }

                return map;
            } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var17) {
                throw new RuntimeException(var17);
            }
        }
    }

    public static <T> T map2Bean(Map<String, ?> sourceMap, Class<T> targetClass) {
        if (sourceMap != null && targetClass != null) {
            Map fieldMap = ReflectUtils.getFieldFlatMap(targetClass);

            try {
                T bean = targetClass.getDeclaredConstructor().newInstance();
                Iterator var4 = sourceMap.keySet().iterator();

                while(var4.hasNext()) {
                    String key = (String)var4.next();
                    Object val = sourceMap.get(key);
                    if (val != null && fieldMap.containsKey(key)) {
                        ReflectUtils.setProperty(bean, (Field)fieldMap.get(key), key, val);
                    }
                }

                return bean;
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            return null;
        }
    }

    public static String[] getNullPropertyNames(Object source) {
        if (source == null) {
            return null;
        } else {
            BeanWrapper src = new BeanWrapperImpl(source);
            PropertyDescriptor[] pds = src.getPropertyDescriptors();
            Set<String> emptyNames = Sets.newHashSet();
            PropertyDescriptor[] var4 = pds;
            int var5 = pds.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                PropertyDescriptor pd = var4[var6];
                Object srcValue = src.getPropertyValue(pd.getName());
                if (srcValue == null) {
                    emptyNames.add(pd.getName());
                }
            }

            String[] result = new String[emptyNames.size()];
            return (String[])emptyNames.toArray(result);
        }
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        if (ObjectUtils.allNotNull(new Object[]{src, target})) {
            copyProperties(src, target, getNullPropertyNames(src));
        }

    }
}

