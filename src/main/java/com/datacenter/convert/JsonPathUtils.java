package com.datacenter.convert;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configuration.Defaults;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonPathUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonPathUtils.class);
    private static final String REGEX = "[\r\n]";
    private static final Configuration CONF;

    public JsonPathUtils() {
    }

    public static <T> T read(Object json, String jsonPath) {
        T result = null;
        String jsonStr;
        if (json instanceof LinkedHashMap) {
            jsonStr = JacksonUtils.toJson(json);
        } else {
            jsonStr = String.valueOf(json);
        }

        try {
            if (null != json) {
                result = JsonPath.read(jsonStr, jsonPath, new Predicate[0]);
            }
        } catch (Exception var5) {
        }

        return result;
    }

    public static String createOneKey(String fieldObject, String fieldCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        if (StringUtils.isNotBlank(fieldObject)) {
            sb.append(".");
            sb.append(fieldObject);
        }

        sb.append(".");
        sb.append(fieldCode);
        return sb.toString().trim();
    }

    public static String getNumberStr(String key) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(key);
        String result = m.replaceAll("").trim();
        return result;
    }

    public static boolean checkValue(Object sourceValue) {
        if (sourceValue instanceof LinkedHashMap) {
            if (CollectionUtils.isEmpty((LinkedHashMap)sourceValue)) {
                return true;
            }
        } else if (sourceValue instanceof JSONArray) {
            if (CollectionUtils.isEmpty((JSONArray)sourceValue)) {
                return true;
            }
        } else if (null == sourceValue) {
            return true;
        }

        return false;
    }

    public static List<String> readPathList(String sourceJson, String key) {
        List<String> resultList = new ArrayList();
        Configuration conf = Configuration.builder().options(new Option[]{Option.AS_PATH_LIST}).options(new Option[]{Option.DEFAULT_PATH_LEAF_TO_NULL}).options(new Option[]{Option.REQUIRE_PROPERTIES}).build();

        try {
            resultList = (List)JsonPath.using(conf).parse(sourceJson).read(key.trim(), new Predicate[0]);
            resultList = (List)resultList.stream().filter((o) -> {
                return StringUtils.isNotBlank(o);
            }).collect(Collectors.toList());
        } catch (Exception var5) {
            log.error("JsonPath readPathList error... e:{}", var5.getMessage(), var5);
        }

        return (List)resultList;
    }

    public static String addNewObjectOrArr(String resultJson, List<String> addPathList, String key, Object json) {
        String addPath;
        for(Iterator var4 = addPathList.iterator(); var4.hasNext(); resultJson = JsonPath.parse(resultJson).put(addPath, key, json, new Predicate[0]).jsonString()) {
            addPath = (String)var4.next();
        }

        if (StringUtils.isNotBlank(resultJson)) {
            resultJson = JacksonUtils.toJson(JacksonUtils.fromMap(resultJson));
        }

        return resultJson;
    }

    public static String addNewObject(String resultJson, String addPath, String key, Object json) {
        resultJson = JsonPath.parse(resultJson).put(addPath, key, json, new Predicate[0]).jsonString();
        if (StringUtils.isNotBlank(resultJson)) {
            resultJson = JacksonUtils.toJson(JacksonUtils.fromMap(resultJson));
        }

        return resultJson;
    }

    public static String addNewObjectToArr(String resultJson, String addPath, Object json) {
        resultJson = JsonPath.parse(resultJson).add(addPath, json, new Predicate[0]).jsonString();
        if (StringUtils.isNotBlank(resultJson)) {
            resultJson = JacksonUtils.toJson(JacksonUtils.fromMap(resultJson));
        }

        return resultJson;
    }

    public static void dynamicSplicePathKey(Set<String> targetFieldSet, StringBuilder targetPathSb, StringBuilder targetCheckPathKey, String targetField) {
        targetCheckPathKey.append(".");
        targetCheckPathKey.append(targetField);
        if (targetFieldSet.contains(targetPathSb.toString())) {
            targetCheckPathKey.append("[*]");
        }

    }

    public static String putValue(String resultJson, String addPath, String key, Object keyValue) {
        resultJson = JsonPath.parse(resultJson).put(addPath, key, keyValue, new Predicate[0]).jsonString();
        if (StringUtils.isNotBlank(resultJson)) {
            resultJson = JacksonUtils.toJson(JacksonUtils.fromMap(resultJson));
        }

        return resultJson;
    }

    public static String addValue(String resultJson, String addPath, Object value) {
        resultJson = JsonPath.parse(resultJson).add(addPath, value, new Predicate[0]).jsonString();
        if (StringUtils.isNotBlank(resultJson)) {
            resultJson = JacksonUtils.toJson(JacksonUtils.fromMap(resultJson));
        }

        return resultJson;
    }

    public static String createPathKey(String fieldObject, String fieldCode) {
        if (StringUtils.isBlank(fieldObject)) {
            return "";
        } else {
            String objectPath = createObjectPathKey(fieldObject);
            StringBuilder pathSb = new StringBuilder(objectPath);
            pathSb.append(".");
            pathSb.append(fieldCode);
            return pathSb.toString();
        }
    }

    public static String createObjectPathKey(String fieldObject) {
        if (StringUtils.isBlank(fieldObject)) {
            return "";
        } else {
            StringBuilder pathSb = new StringBuilder("$");
            String[] pathArr = fieldObject.split("\\.");
            List<String> pathList = Arrays.asList(pathArr);

            String path;
            for(Iterator var4 = pathList.iterator(); var4.hasNext(); pathSb.append(path)) {
                path = (String)var4.next();
                pathSb.append(".");
                if (path.contains("[]") || path.contains("[$]")) {
                    path = RegExUtils.replaceAll(path, "\\[\\]", "[*]");
                }
            }

            return pathSb.toString();
        }
    }

    public static boolean isExistPath(String resultJson, String checkPathKey) {
        List<String> result = readPathList(resultJson, checkPathKey);
        return !CollectionUtils.isEmpty(result);
    }

    public static String joinPathWithPathList(List<String> checkPathList) {
        if (CollectionUtils.isEmpty(checkPathList)) {
            return "";
        } else {
            String resultPath = "$";

            for(int i = 0; i < checkPathList.size(); ++i) {
                resultPath = StringUtils.joinWith(".", new Object[]{resultPath, checkPathList.get(i)});
            }

            return resultPath;
        }
    }

    public static String removeWithPath(String sourceJson, String removePath) {
        if (StringUtils.isBlank(removePath)) {
            return sourceJson;
        } else {
            String fatherPath = removePath.substring(0, removePath.lastIndexOf("."));
            String removeKey = removePath.substring(removePath.lastIndexOf(".") + 1);
            String[] pathArr = removePath.split("\\.");
            if (pathArr.length < 3) {
                sourceJson = JacksonUtils.remove(sourceJson, removeKey);
                log.info(sourceJson);
                return sourceJson;
            } else {
                String addPath = fatherPath.substring(0, fatherPath.lastIndexOf("."));
                String addKey = fatherPath.substring(fatherPath.lastIndexOf(".") + 1);
                Object father = read(sourceJson, fatherPath);
                if (null == father) {
                    return sourceJson;
                } else {
                    String fatherJson = JacksonUtils.toJson(father);
                    String removedJson = JacksonUtils.remove(fatherJson, removeKey);
                    if (StringUtils.isNotBlank(removedJson)) {
                        Map map = JacksonUtils.fromMap(removedJson);
                        sourceJson = putValue(sourceJson, addPath, addKey, map);
                    }

                    return sourceJson;
                }
            }
        }
    }

    public static String setValue(String setPath, Object setValue, String sourceJson) {
        Object oldValue = read(sourceJson, setPath);
        if (null == oldValue) {
            String putPath = setPath.substring(0, setPath.lastIndexOf("."));
            String putKey = setPath.substring(setPath.lastIndexOf(".") + 1);
            sourceJson = putValue(sourceJson, putPath, putKey, setValue);
        } else {
            sourceJson = JsonPath.using(CONF).parse(sourceJson).set(setPath, setValue, new Predicate[0]).jsonString();
        }

        if (StringUtils.isNotBlank(sourceJson)) {
            sourceJson = JacksonUtils.toJson(JacksonUtils.fromMap(sourceJson));
        }

        return sourceJson;
    }

    static {
        CONF = Configuration.builder().options(new Option[]{Option.AS_PATH_LIST}).build();
        Configuration.setDefaults(new Defaults() {
            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return this.jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return this.mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }
}

