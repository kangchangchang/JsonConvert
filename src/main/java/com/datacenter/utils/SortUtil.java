package com.datacenter.utils;

/**
 * @author pc
 * @date Create in  2023/2/23
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.datacenter.entity.JsonConvertTreeBO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class SortUtil {
    public SortUtil() {
    }

    public static int sortByStrLength(String o1, String o2) {
        if (StringUtils.isBlank(o1)) {
            return -1;
        } else if (StringUtils.isBlank(o2)) {
            return 1;
        } else if (o1.equals(o2)) {
            return 0;
        } else if (o1.length() == o2.length()) {
            return 0;
        } else {
            return o1.length() > o2.length() ? 1 : -1;
        }
    }

    public static int sortByPathLength(String o1, String o2) {
        if (StringUtils.isBlank(o1)) {
            return -1;
        } else if (StringUtils.isBlank(o2)) {
            return 1;
        } else {
            String[] field1Arr = o1.split("\\.");
            List<String> field1List = Arrays.asList(field1Arr);
            String[] field2Arr = o2.split("\\.");
            List<String> field2List = Arrays.asList(field2Arr);
            int o1Size = field1List.size();
            int o2Size = field2List.size();
            if (o1Size == o2Size) {
                return 0;
            } else {
                return o1Size > o2Size ? 1 : -1;
            }
        }
    }

    public static int sortPathLengthDesc(String o1, String o2) {
        if (StringUtils.isBlank(o1)) {
            return 1;
        } else if (StringUtils.isBlank(o2)) {
            return -1;
        } else {
            String[] field1Arr = o1.split("\\.");
            List<String> field1List = Arrays.asList(field1Arr);
            String[] field2Arr = o2.split("\\.");
            List<String> field2List = Arrays.asList(field2Arr);
            int o1Size = field1List.size();
            int o2Size = field2List.size();
            if (o1Size == o2Size) {
                return 0;
            } else {
                return o1Size > o2Size ? -1 : 1;
            }
        }
    }

    public static int sortByNumber(Integer pos, Integer pos1) {
        if (null == pos && null == pos1) {
            return 0;
        } else if (null == pos && null != pos1) {
            return 1;
        } else if (null != pos && null == pos1) {
            return -1;
        } else if (pos.equals(pos1)) {
            return 0;
        } else {
            return pos > pos1 ? 1 : -1;
        }
    }

    public static int sortByPathSizeDesc(JsonConvertTreeBO o1, JsonConvertTreeBO o2) {
        String data1 = o1.getData().toString();
        String data2 = o2.getData().toString();
        String regEx = "[^0-9]";
        String num1 = RegexUtils.replaceAll(data1, regEx, "").trim();
        String num2 = RegexUtils.replaceAll(data2, regEx, "").trim();
        if (StringUtils.isBlank(num1) && StringUtils.isBlank(num2)) {
            return 0;
        } else if (StringUtils.isBlank(num1) && StringUtils.isNotBlank(num2)) {
            return 1;
        } else {
            return StringUtils.isNotBlank(num1) && StringUtils.isBlank(num2) ? -1 : sortByNumber(Integer.valueOf(num1), Integer.valueOf(num2));
        }
    }

    public static String mapSortedByKey(Map<String, Object> param) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> keyList = new ArrayList(param.keySet());
        Collections.sort(keyList);

        for(int i = NumberUtils.INTEGER_ZERO; i < keyList.size(); ++i) {
            String key = (String)keyList.get(i);
            if (i == keyList.size() - 1) {
                stringBuilder.append(key).append("=").append(param.getOrDefault(key, ""));
            } else {
                stringBuilder.append(key).append("=").append(param.getOrDefault(key, "")).append("&");
            }
        }

        return stringBuilder.toString();
    }
}
