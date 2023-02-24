package com.datacenter.convert;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.datacenter.entity.ConvertValueBO;
import com.datacenter.entity.DateFormatBO;
import com.datacenter.entity.JsonConvertBO;
import com.datacenter.entity.JsonConvertTreeBO;
import com.datacenter.enums.JsonConvertExceptionCodeTypeEnum;
import com.datacenter.enums.TargetValueTypeEnum;
import com.datacenter.exception.ServiceException;
import com.datacenter.utils.BeanUtils;
import com.datacenter.utils.DateFormatUtils;
import com.datacenter.utils.RegexUtils;
import com.datacenter.utils.SortUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class JsonConvertUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonConvertUtils.class);
    private static ThreadPoolExecutor taskExecutor;

    public JsonConvertUtils() {
    }

    public static String jsonConvertByDict(String sourceJson, List<JsonConvertBO> jsonConvertBOList, Map<String, Map<String, Object>> dictMap) {
        log.info("开始字典功能Json映射");
        String targetJson = baseJsonConvert(jsonConvertBOList, sourceJson, dictMap);
        log.info("结束字典功能Json映射,targetJson:{}", targetJson);
        return targetJson;
    }

    public static String jsonConvert(String sourceJson, List<JsonConvertBO> jsonConvertBOList) {
        log.info("开始基本Json映射");
        String targetJson = baseJsonConvert(jsonConvertBOList, sourceJson, (Map)null);
        log.info("结束基本Json映射,targetJson:{}", targetJson);
        return targetJson;
    }

    private static String baseJsonConvert(List<JsonConvertBO> jsonConvertBOList, String sourceJson, Map<String, Map<String, Object>> dictMap) {
        Map<String, Object> sourcePathValueMap = getPathAndValueMap(sourceJson);
        if (CollectionUtils.isEmpty(sourcePathValueMap)) {
            log.warn("sourceJson is empty...");
            return "{}";
        } else {
            sourcePathValueMap = baseConvert(jsonConvertBOList, sourcePathValueMap, dictMap);
            Map<String, Object> targetPathValueMap = computeTargetPathValue(jsonConvertBOList, sourcePathValueMap);
            if (CollectionUtils.isEmpty(targetPathValueMap)) {
                log.warn("compute targetPathValueMap is null...");
                return "{}";
            } else {
                Set<String> keySet = targetPathValueMap.keySet();
                List<String> keyList = new ArrayList(keySet);
                Collections.sort(keyList, (o1, o2) -> {
                    return SortUtil.sortPathLengthDesc(o1, o2);
                });
                JsonConvertTreeBO targetJsonConvertTreeBO = convertToTree(keyList);
                String path = "$";
                String targetJson = "{}";
                targetJson = createTargetJson(targetJson, path, targetJsonConvertTreeBO);

                Object value;
                String addPath;
                String addKey;
                for(Iterator var10 = targetPathValueMap.entrySet().iterator(); var10.hasNext(); targetJson = JsonPathUtils.putValue(targetJson, addPath, addKey, value)) {
                    Entry<String, Object> entry = (Entry)var10.next();
                    String key = (String)entry.getKey();
                    value = entry.getValue();
                    addPath = StringUtils.substringBeforeLast(key, ".");
                    addKey = StringUtils.substringAfterLast(key, ".");
                }

                return targetJson;
            }
        }
    }

    public static Map<String, Object> getPathAndValueMap(String sJson) {
        List<String> pathList = JsonPathUtils.readPathList(sJson, "$..*");
        List<String> newPathList = new ArrayList();
        Iterator var3 = pathList.iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            s = s.replace("['", ".");
            s = s.replace("']", "");
            newPathList.add(s);
        }

        List<String> newPathSet = new ArrayList();
        Iterator var11 = newPathList.iterator();

        while(var11.hasNext()) {
            String s = (String)var11.next();
            AtomicInteger size = new AtomicInteger(0);
            Iterator var7 = newPathList.iterator();

            while(var7.hasNext()) {
                String path = (String)var7.next();
                String beforePath = StringUtils.substringBeforeLast(path, ".");
                if (s.equals(beforePath)) {
                    size.incrementAndGet();
                    break;
                }
            }

            if (size.get() != 1) {
                newPathSet.add(s);
            }
        }

        Map<String, Object> sourcePathValueMap = new HashMap();
        Iterator var13 = newPathSet.iterator();

        while(var13.hasNext()) {
            String path = (String)var13.next();
            Object value = JsonPathUtils.read(sJson, path);
            sourcePathValueMap.put(path, value);
        }

        return sourcePathValueMap;
    }

    private static Map<String, Object> baseConvert(List<JsonConvertBO> jsonConvertBOList, Map<String, Object> sourcePathValueMap, Map<String, Map<String, Object>> dictMap) {
        return CollectionUtils.isEmpty(dictMap) ? convertValue((List)jsonConvertBOList, (Map)sourcePathValueMap, (Map)null) : convertValue(jsonConvertBOList, sourcePathValueMap, dictMap);
    }

    private static Map<String, Object> convertValue(List<JsonConvertBO> jsonConvertBOList, Map<String, Object> sourcePathValueMap, Map<String, Map<String, Object>> dictMap) {
        log.info("开始准备数组内容转换！！");
        List<JsonConvertBO> convertBoList = (List)jsonConvertBOList.stream().filter((o) -> {
            return null != o.getTargetType();
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(convertBoList)) {
            convertBoList.forEach((o) -> {
                ConvertValueBO convertValueBO = (ConvertValueBO) BeanUtils.copyBean(o, ConvertValueBO.class);
                String sourcePath = convertValueBO.getSourcePath();
                if (sourcePath.contains("[]")) {
                    Set<String> arrPathSet = (Set)sourcePathValueMap.keySet().stream().filter((path) -> {
                        return path.contains("[") || path.contains("]");
                    }).collect(Collectors.toSet());
                    arrPathSet.forEach((arrPath) -> {
                        String path = arrPath.replaceAll("[0-9]+", "");
                        if (path.equals(sourcePath)) {
                            Object value = sourcePathValueMap.get(arrPath);
                            Object targetValue = convertValue(value, convertValueBO, dictMap);
                            sourcePathValueMap.put(arrPath, targetValue);
                        }

                    });
                } else {
                    Object value = sourcePathValueMap.get(sourcePath);
                    Object targetValue = convertValue(value, convertValueBO, dictMap);
                    sourcePathValueMap.put(sourcePath, targetValue);
                }

            });
        }

        log.info("结束数组内容转换！！");
        return sourcePathValueMap;
    }

    private static Object convertValue(Object sourceValue, ConvertValueBO convertValueBO, Map<String, Map<String, Object>> dictMap)  {
        if (null != sourceValue && !StringUtils.isBlank(sourceValue.toString())) {
            Integer type = convertValueBO.getTargetType();
            BigDecimal factor = convertValueBO.getConvertFactor();
            Integer precision = convertValueBO.getConvertPrecision();
            String dictCode = convertValueBO.getDictCode();
            DateFormatBO formatBO = DateFormatBO.convertBO(sourceValue, convertValueBO.getSTimeFormat(), convertValueBO.getTTimeFormat());
            Object targetValue = null;
            if (StringUtils.isNotBlank(dictCode)) {
                if (CollectionUtils.isEmpty(dictMap)) {
                    log.warn("jsonConvert with dict,sourcePath:{},字典编号:{},字典映射关系isEmpty", convertValueBO.getSourcePath(), dictCode);
                    throw new ServiceException(JsonConvertExceptionCodeTypeEnum.DICT_CODE_EMPTY);
                }

                Map<String, Object> dictValueMap = (Map)dictMap.get(dictCode);
                if (!CollectionUtils.isEmpty(dictValueMap)) {
                    Object value = dictValueMap.get(String.valueOf(sourceValue).trim());
                    if (null == value) {
                        log.error("jsonConvert with dict,未匹配到目标值,sourcePath:{},字典编号:{},sourceValue:{}", new Object[]{convertValueBO.getSourcePath(), dictCode, sourceValue});
                        throw new ServiceException(JsonConvertExceptionCodeTypeEnum.DICT_CODE_VALUE_EMPTY);
                    }

                    sourceValue = value;
                } else {
                    log.warn("jsonConvert with dict,sourcePath:{},未匹配到对应字典映射关系,字典编号:{}", convertValueBO.getSourcePath(), dictCode);
                }
            }

            String strValue = String.valueOf(sourceValue);
            strValue = convertNumericFactor(strValue, factor, precision);
            if (null != type) {
                TargetValueTypeEnum targetType = TargetValueTypeEnum.valueOf(type);
                if (null == targetType) {
                    throw new ServiceException(JsonConvertExceptionCodeTypeEnum.UN_SUPPORT_TYPE_CONVERT);
                }

                try {
                    switch(targetType) {
                        case INTEGER:
                            targetValue = Integer.valueOf(strValue);
                            break;
                        case LONG:
                            targetValue = Long.valueOf(strValue);
                            break;
                        case BOOLEAN:
                            targetValue = Boolean.valueOf(strValue);
                            break;
                        case BIG_DECIMAL:
                            BigDecimal targetBig = new BigDecimal(strValue);
                            if (null != precision && null == factor) {
                                targetBig = targetBig.setScale(precision, 1);
                            }

                            targetValue = targetBig;
                            break;
                        default:
                            targetValue = strValue;
                    }
                } catch (Exception var12) {
                    log.error("jsonConvert when mapping value error... sourcePath:{},sourceValue:{},targetType:{}...e:{}", new Object[]{convertValueBO.getSourcePath(), strValue, type, var12.getMessage()});
                    throw new ServiceException(JsonConvertExceptionCodeTypeEnum.UN_SUPPORT_TYPE_CONVERT);
                }
            }

            if (Objects.nonNull(formatBO)) {
                targetValue = DateFormatUtils.format(formatBO);
            }

            return targetValue;
        } else {
            return null;
        }
    }

    private static String convertNumericFactor(String numericValue, BigDecimal factor, Integer precision) {
        if (null != factor && StringUtils.isNotBlank(numericValue) && NumberUtils.isParsable(numericValue)) {
            log.info("start unit convert... sourceValue:{},convertFactor:{},convertPrecision:{}", new Object[]{numericValue, factor, precision});
            BigDecimal targetBig = NumberUtils.createBigDecimal(numericValue);
            targetBig = targetBig.multiply(factor);
            if (null != precision) {
                targetBig = targetBig.setScale(precision, 1);
            } else {
                targetBig = targetBig.setScale(6, 1);
            }

            return String.valueOf(targetBig);
        } else {
            return numericValue;
        }
    }

    private static Map<String, Object> computeTargetPathValue(List<JsonConvertBO> jsonConvertBOList, Map<String, Object> sourcePathValueMap) {
        Map<String, Object> targetPathValueMap = new ConcurrentHashMap();
        List<JsonConvertBO> objectEscapeList = (List)jsonConvertBOList.stream().filter((o) -> {
            return checkObject(o);
        }).collect(Collectors.toList());
        List<JsonConvertBO> symmetryArrEscapeList = (List)jsonConvertBOList.stream().filter((o) -> {
            return checkSymmetryArr(o);
        }).collect(Collectors.toList());
        List<JsonConvertBO> unSymmetryArrEscapeList = (List)jsonConvertBOList.stream().filter((o) -> {
            return StringUtils.contains(o.getTargetPath(), "[&]");
        }).collect(Collectors.toList());
        new ArrayList();
        if (!CollectionUtils.isEmpty(objectEscapeList)) {
            computeObjPathAndValue(targetPathValueMap, sourcePathValueMap, objectEscapeList);
        }

        if (!CollectionUtils.isEmpty(symmetryArrEscapeList)) {
            computeSymmetryArrPathAndValue(targetPathValueMap, sourcePathValueMap, symmetryArrEscapeList);
        }

        if (!CollectionUtils.isEmpty(unSymmetryArrEscapeList)) {
            computeUnSymmetryArrPathAndValue(targetPathValueMap, sourcePathValueMap, unSymmetryArrEscapeList, jsonConvertBOList);
        }

        return targetPathValueMap;
    }

    private static boolean computeUnSymmetryArrPathAndValue(Map<String, Object> targetPathValueMap, Map<String, Object> sourcePathValueMap, List<JsonConvertBO> unSymmetryArrEscapeList, List<JsonConvertBO> jsonConvertBOList) {
        Map<String, Object> copySourcePathValueMap = new HashMap();
        copySourcePathValueMap.putAll(sourcePathValueMap);
        Map<String, List<JsonConvertBO>> groupByGroupSignMap = (Map)jsonConvertBOList.stream().filter((o) -> {
            return o.getTargetPath().contains("[&]");
        }).collect(Collectors.groupingBy((o) -> {
            return StringUtils.substringBeforeLast(o.getTargetPath(), "[&]");
        }));
        Map<String, Map<String, Object>> sourcePathWithPatchValueMap = new HashMap();
        Iterator var7 = groupByGroupSignMap.entrySet().iterator();

        while(var7.hasNext()) {
            Entry<String, List<JsonConvertBO>> entry = (Entry)var7.next();
            List<JsonConvertBO> boList = (List)entry.getValue();
            Set<String> regexPathSet = new HashSet();
            boList.forEach((o) -> {
                String regexPath = RegexUtils.replaceAll(o.getSourcePath(), "\\[[0-9]*\\]", "[]");
                regexPathSet.add(regexPath);
            });
            List<String> pathList = (List)sourcePathValueMap.keySet().stream().filter((o) -> {
                return regexPathSet.contains(RegexUtils.replaceAll(o, "\\[[0-9]*\\]", "[]"));
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(pathList)) {
                Map<Integer, Set<String>> arrSizeWithPathSetMap = new HashMap();
                int count = computeArrSizeWithPathList(arrSizeWithPathSetMap, pathList);
                Set<String> maxPathSet = (Set)arrSizeWithPathSetMap.get(count);
                getMaxArrPathValues(sourcePathWithPatchValueMap, copySourcePathValueMap, maxPathSet);
                getPatchArrPathValues(sourcePathWithPatchValueMap, copySourcePathValueMap, maxPathSet, arrSizeWithPathSetMap, pathList, count);
            }
        }

        Map<String, List<JsonConvertBO>> map = (Map)unSymmetryArrEscapeList.stream().collect(Collectors.groupingBy((o) -> {
            return StringUtils.substringBeforeLast(o.getTargetPath(), "[&]");
        }));
        map.entrySet().forEach((entryx) -> {
            String currentPath = (String)entryx.getKey();
            List<JsonConvertBO> boList = (List)entryx.getValue();
            Map<Integer, List<JsonConvertBO>> arrSizeMap = (Map)boList.stream().collect(Collectors.groupingBy((o) -> {
                return RegexUtils.countMarcher(StringUtils.substringAfterLast(o.getTargetPath(), "[&]"), "\\[[0-9]*\\]");
            }));
            List<Integer> arrSizeList = new ArrayList(arrSizeMap.keySet());
            HashMap targetPathWithSourcePathDetail;
            if (arrSizeList.size() > 1) {
                targetPathWithSourcePathDetail = new HashMap();
                Iterator var8 = arrSizeList.iterator();

                while(true) {
                    while(var8.hasNext()) {
                        Integer arrSize = (Integer)var8.next();
                        List<JsonConvertBO> currentBOList = (List)arrSizeMap.get(arrSize);
                        Map<String, Object> currentPatchPathValueMap = computeCurrentPathValue(sourcePathWithPatchValueMap, currentBOList);
                        HashMap pathValueMap = new HashMap();;
                        Map targetPathWithSourcePathMap;
                        if (arrSize == 0) {
                            String targetBeforePathx = StringUtils.substringBeforeLast(currentPath, ".");
                            int countArr = RegexUtils.countMarcher(targetBeforePathx, "\\[[0-9]*\\]");
                            Set<String> pathSet = new HashSet();
                            if (countArr > 0) {
                                pathSet = getGroupBeforePathSet(targetBeforePathx, currentPatchPathValueMap.keySet());
                            } else {
                                ((Set)pathSet).add(targetBeforePathx);
                            }

                            Iterator var23 = ((Set)pathSet).iterator();

                            while(var23.hasNext()) {
                                String groupPath = (String)var23.next();

                                if (countArr > 0) {
                                    for (String str:currentPatchPathValueMap.keySet()){
                                        if ((str).contains(groupPath)) {
                                            pathValueMap.put(str,copySourcePathValueMap.get(str));
                                        }
                                    }

                                } else {
                                    pathValueMap.putAll(currentPatchPathValueMap);
                                }

                                targetPathWithSourcePathMap = computeGroupTargetPathAndValue(groupPath, currentBOList, pathValueMap, targetPathValueMap);
                                targetPathWithSourcePathDetail.putAll(targetPathWithSourcePathMap);
                            }
                        } else {
                            Map<String, List<String>> copyTargetPathWithSourcePathDetail = new HashMap();
                            copyTargetPathWithSourcePathDetail.putAll(targetPathWithSourcePathDetail);
                            targetPathWithSourcePathDetail.clear();
                            Iterator var13 = copyTargetPathWithSourcePathDetail.entrySet().iterator();

                            while(var13.hasNext()) {
                                Entry<String, List<String>> targetPathEntry = (Entry)var13.next();
                                String targetBeforePath = (String)targetPathEntry.getKey();
                                targetBeforePath = StringUtils.substringBeforeLast(targetBeforePath, ".");
                                List<String> sourcePathList = (List)targetPathEntry.getValue();
                                pathValueMap = new HashMap();
                                Iterator var18 = sourcePathList.iterator();

                                while(var18.hasNext()) {
                                    String s = (String)var18.next();

                                    for (String str:currentPatchPathValueMap.keySet()){
                                        if (str.contains(s)) {
                                            pathValueMap.put(str, currentPatchPathValueMap.get(str));
                                        }
                                    }

                                }

                                targetPathWithSourcePathMap = computeGroupTargetPathAndValue(targetBeforePath, currentBOList, pathValueMap, targetPathValueMap);
                                targetPathWithSourcePathDetail.putAll(targetPathWithSourcePathMap);
                            }
                        }
                    }

                    return;
                }
            } else {
                targetPathWithSourcePathDetail = new HashMap();
                boList.forEach((o) -> {
                    String sourcePath = o.getSourcePath();
                    Map<String, Object> currentPatchValueMap = (Map)sourcePathWithPatchValueMap.get(sourcePath);
                    if (null != currentPatchValueMap) {
                        targetPathWithSourcePathDetail.putAll(currentPatchValueMap);
                    }

                });
                computeUnSymmetryAndUnGroupArrPathAndValue(targetPathValueMap, targetPathWithSourcePathDetail, boList);
            }
        });
        return true;
    }

    private static Map<String, Object> computeCurrentPathValue(Map<String, Map<String, Object>> sourcePathWithPatchValueMap, List<JsonConvertBO> currentBOList) {
        Map<String, Object> currentPatchPathValueMap = new HashMap();
        currentBOList.forEach((o) -> {
            String sourcePath = o.getSourcePath();
            Map<String, Object> currentPatchValueMap = (Map)sourcePathWithPatchValueMap.get(sourcePath);
            if (null != currentPatchValueMap) {
                currentPatchPathValueMap.putAll(currentPatchValueMap);
            }

        });
        return currentPatchPathValueMap;
    }

    private static int computeArrSizeWithPathList(Map<Integer, Set<String>> arrSizeWithPathSetMap, List<String> pathList) {
        int count = 0;
        Iterator var3 = pathList.iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            String splitPath = StringUtils.substringBeforeLast(s, ".");
            int countArr = RegexUtils.countMarcher(splitPath, "\\[[0-9]*\\]");
            Set<String> pathSet = (Set)arrSizeWithPathSetMap.get(countArr);
            if (CollectionUtils.isEmpty((Collection)pathSet)) {
                pathSet = new HashSet();
            }

            ((Set)pathSet).add(splitPath);
            arrSizeWithPathSetMap.put(countArr, pathSet);
            if (countArr > count) {
                count = countArr;
            }
        }

        return count;
    }

    private static void getPatchArrPathValues(Map<String, Map<String, Object>> sourcePathWithPatchValueMap, Map<String, Object> copySourcePathValueMap, Set<String> maxPathSet, Map<Integer, Set<String>> arrSizeWithPathSetMap, List<String> pathList, int maxArrSize) {
        Set<Integer> patchArrSet = (Set)arrSizeWithPathSetMap.keySet().stream().filter((o) -> {
            return o < maxArrSize;
        }).collect(Collectors.toSet());
        patchArrSet.forEach((arrSize) -> {
            Set<String> countPathSet = (Set)arrSizeWithPathSetMap.get(arrSize);
            countPathSet.forEach((path) -> {
                computePatchPathAndValue(path, maxPathSet, pathList, copySourcePathValueMap, sourcePathWithPatchValueMap, maxArrSize - arrSize);
            });
        });
    }

    private static void getMaxArrPathValues(Map<String, Map<String, Object>> sourcePathWithPatchValueMap, Map<String, Object> copySourcePathValueMap, Set<String> maxPathSet) {
        Map<String, Map<String, Object>> sourceRegexPathMap = new HashMap();
        Map<String, List<Entry<String, Object>>> groupMap = (Map)copySourcePathValueMap.entrySet().stream().filter((o) -> {
            return maxPathSet.contains(StringUtils.substringBeforeLast((String)o.getKey(), "."));
        }).collect(Collectors.groupingBy((entry) -> {
            return ((String)entry.getKey()).replaceAll("\\[[0-9]*\\]", "[]");
        }));
        groupMap.entrySet().forEach((o) -> {
            String path = (String)o.getKey();
            List<Entry<String, Object>> values = (List)o.getValue();
            Map<String, Object> valuesMap = new HashMap();
            values.forEach((value) -> {
                valuesMap.put(value.getKey(), value.getValue());
            });
            sourceRegexPathMap.put(path, valuesMap);
        });
        sourcePathWithPatchValueMap.putAll(sourceRegexPathMap);
    }

    private static Set<String> getGroupBeforePathSet(String beforePath, Set<String> keySet) {
        Set<String> beforePathSet = new HashSet();
        Iterator var3 = keySet.iterator();

        while(var3.hasNext()) {
            String sourcePath = (String)var3.next();
            Matcher sMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(sourcePath);
            Matcher tMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(beforePath);
            StringBuffer targetPathSb = new StringBuffer();

            while(sMatcher.find() && tMatcher.find()) {
                String arrIndex = sMatcher.group();
                sMatcher.appendReplacement(targetPathSb, arrIndex);
            }

            beforePathSet.add(targetPathSb.toString());
        }

        return beforePathSet;
    }

    private static Map<String, List<String>> computeGroupTargetPathAndValue(String beforePath, List<JsonConvertBO> currentBoList, Map<String, Object> allPatchValueMap, Map<String, Object> targetPathValueMap) {
        Map<String, List<String>> targetPathWithSourcePathDetail = new HashMap();
        Map<String, List<String>> groupListMap = (Map)allPatchValueMap.keySet().stream().collect(Collectors.groupingBy((o) -> {
            return StringUtils.substringBeforeLast(o, ".");
        }));
        Map<String, Set<String>> valueAndMap = new HashMap();
        groupListMap.entrySet().forEach((entry) -> {
            String splitPath = (String)entry.getKey();
            List<String> detailPath = (List)entry.getValue();
            String pathValue = null;

            String joinKeyValue;
            for(Iterator var6 = detailPath.iterator(); var6.hasNext(); pathValue = StringUtils.joinWith("#", new Object[]{pathValue, joinKeyValue})) {
                String s = (String)var6.next();
                String key = StringUtils.substringAfterLast(s, ".");
                Object keyValue = allPatchValueMap.get(s);
                joinKeyValue = StringUtils.joinWith("@", new Object[]{key, String.valueOf(keyValue)});
            }

            Set<String> pathSet = (Set)valueAndMap.get(pathValue);
            if (CollectionUtils.isEmpty((Collection)pathSet)) {
                pathSet = new HashSet();
            }

            ((Set)pathSet).add(splitPath);
            valueAndMap.put(pathValue, pathSet);
        });
        int pathArrSize = 0;

        for(Iterator var8 = valueAndMap.values().iterator(); var8.hasNext(); ++pathArrSize) {

            final  int copyPathArrSize=pathArrSize;
            Set<String> value = (Set)var8.next();
            List<String> valueList = new ArrayList(value);
            currentBoList.forEach((o) -> {
                String targetKey = StringUtils.substringAfterLast(o.getTargetPath(), ".");
                String sourceKey = StringUtils.substringAfterLast(o.getSourcePath(), ".");
                String beforeTargetPath = StringUtils.substringBeforeLast(o.getTargetPath(), ".");
                beforeTargetPath = beforeTargetPath.replace("[&]", "[]");
                Matcher beforeMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(beforePath);
                Matcher tBeforeMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(beforeTargetPath);
                StringBuffer targetPathSb = new StringBuffer();

                String targetPath;
                while(beforeMatcher.find() && tBeforeMatcher.find()) {
                    targetPath = beforeMatcher.group();
                    tBeforeMatcher.appendReplacement(targetPathSb, targetPath);
                }

                tBeforeMatcher.appendTail(targetPathSb);
                targetPath = targetPathSb.toString().replace("[]", "[" + copyPathArrSize + "]");
                targetPath = StringUtils.joinWith(".", new Object[]{targetPath, targetKey});
                String sourceBeforePath = (String)valueList.get(0);
                String sourcePath = o.getSourcePath();
                String patchSourcePath = StringUtils.joinWith(".", new Object[]{sourceBeforePath, sourceKey});
                int sourcePatchSize = RegexUtils.countMarcher(sourceBeforePath, "\\[[0-9]*\\]");
                int sourceSize = RegexUtils.countMarcher(sourcePath, "\\[[0-9]*\\]");
                if (sourceSize != sourcePatchSize) {
                    String patchKey = StringUtils.join(new Serializable[]{"[", sourcePatchSize - sourceSize, "]", sourceKey});
                    patchSourcePath = StringUtils.joinWith(".", new Object[]{sourceBeforePath, patchKey});
                }

                Object targetValue = allPatchValueMap.get(patchSourcePath);
                if (null == targetValue) {
                    throw new ServiceException(JsonConvertExceptionCodeTypeEnum.SOURCE_PATH_VALUE_EMPTY);
                } else {
                    targetPathValueMap.put(targetPath, targetValue);
                    targetPathWithSourcePathDetail.put(targetPath, valueList);
                }
            });
        }

        return targetPathWithSourcePathDetail;
    }

    private static boolean computeUnSymmetryAndUnGroupArrPathAndValue(Map<String, Object> targetPathValueMap, Map<String, Object> allPatchValueMap, List<JsonConvertBO> currentBoList) {
        log.info("计算非对称非分组映射");
        currentBoList.forEach((o) -> {
            String sourceKey = StringUtils.substringAfterLast(o.getSourcePath(), ".");
            String sourceBeforePath = StringUtils.substringBeforeLast(o.getTargetPath(), ".");
            String targetPath = o.getTargetPath().replace("[&]", "[]");
            Map<String, Object> currentKeyPathValueMap = new HashMap();
            Iterator var7 = allPatchValueMap.entrySet().iterator();

            while(var7.hasNext()) {
                Entry<String, Object> entry = (Entry)var7.next();
                String sPath = (String)entry.getKey();
                Object sValue = entry.getValue();
                String key = StringUtils.substringAfterLast(sPath, ".");
                String beforePath = StringUtils.substringBeforeLast(sPath, ".");
                if (key.equals(sourceKey)) {
                    currentKeyPathValueMap.put(sPath, sValue);
                }

                if (key.contains(sourceKey) && !key.equals(sourceKey)) {
                    String regexSourcePath = RegexUtils.replaceAll(beforePath, "\\[[0-9]*\\]", "[]");
                    int patchArrSize = RegexUtils.countMarcher(regexSourcePath, "\\[\\]");
                    int arrSize = RegexUtils.countMarcher(sourceBeforePath, "\\[\\]");
                    String patchKey = StringUtils.join(new Serializable[]{"[", patchArrSize - arrSize, "]", sourceKey});
                    if (patchKey.equals(key)) {
                        currentKeyPathValueMap.put(sPath, sValue);
                    }
                }
            }

            currentKeyPathValueMap.entrySet().forEach((entryx) -> {
                String path = (String)entryx.getKey();
                Object value = entryx.getValue();
                String beforePath = StringUtils.substringBeforeLast(path, ".");
                Matcher sMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(beforePath);
                Matcher tMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(targetPath);
                StringBuffer targetPathSb = new StringBuffer();

                while(sMatcher.find() && tMatcher.find()) {
                    String arrIndex = sMatcher.group();
                    tMatcher.appendReplacement(targetPathSb, arrIndex);
                }

                tMatcher.appendTail(targetPathSb);
                targetPathValueMap.put(String.valueOf(targetPathSb), value);
            });
        });
        return true;
    }

    private static void computePatchPathAndValue(String path, Set<String> maxPathSet, List<String> pathList, Map<String, Object> copySourcePathValueMap, Map<String, Map<String, Object>> sourcePathWithPatchValueMap, int arrSize) {
        List<String> filterPath = (List)pathList.stream().filter((sourcePathx) -> {
            return checkPath(path, sourcePathx);
        }).collect(Collectors.toList());
        Iterator var7 = filterPath.iterator();

        while(var7.hasNext()) {
            String sourcePath = (String)var7.next();
            String regexSourcePath = sourcePath.replaceAll("\\[[0-9]*\\]", "[]");
            Map<String, Object> patchPathAndValueMap = (Map)sourcePathWithPatchValueMap.get(regexSourcePath);
            if (CollectionUtils.isEmpty((Map)patchPathAndValueMap)) {
                patchPathAndValueMap = new HashMap();
            }

            String splitPath = StringUtils.substringBeforeLast(sourcePath, ".");
            String splitAfterPath = StringUtils.substringAfterLast(sourcePath, ".");
            splitAfterPath = StringUtils.join(new Serializable[]{"[", arrSize, "]", splitAfterPath});
            Object value = copySourcePathValueMap.get(sourcePath);
            Iterator var14 = maxPathSet.iterator();

            while(var14.hasNext()) {
                String o = (String)var14.next();
                if (o.contains(splitPath) && !splitPath.equals(o)) {
                    String patchPath = StringUtils.joinWith(".", new Object[]{o, splitAfterPath});
                    ((Map)patchPathAndValueMap).put(patchPath, value);
                }
            }

            sourcePathWithPatchValueMap.put(regexSourcePath, patchPathAndValueMap);
        }

    }

    private static boolean checkUnSymmetryArrPath(String sourcePath, List<JsonConvertBO> boList) {
        boolean result = false;
        Iterator var3 = boList.iterator();

        while(var3.hasNext()) {
            JsonConvertBO bo = (JsonConvertBO)var3.next();
            String path = bo.getSourcePath();
            String regexPath = sourcePath.replaceAll("\\[[0-9]*\\]", "[]");
            if (path.equals(regexPath)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private static boolean computeSymmetryArrPathAndValue(Map<String, Object> targetPathValueMap, Map<String, Object> sourcePathValueMap, List<JsonConvertBO> symmetryArrEscapeList) {
        log.info("开始计算对称数组");
        symmetryArrEscapeList.forEach((o) -> {
            String sourcePath = o.getSourcePath();
            String targetPath = o.getTargetPath();
            sourcePathValueMap.entrySet().forEach((entry) -> {
                String key = (String)entry.getKey();
                String regKey = RegexUtils.replaceAll(key, "[0-9]+", "");
                if (regKey.equals(sourcePath)) {
                    Matcher sMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(key);
                    Matcher tMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(targetPath);
                    StringBuffer targetPathSb = new StringBuffer();

                    while(sMatcher.find() && tMatcher.find()) {
                        String arrIndex = sMatcher.group();
                        tMatcher.appendReplacement(targetPathSb, arrIndex);
                    }

                    tMatcher.appendTail(targetPathSb);
                    targetPathValueMap.put(String.valueOf(targetPathSb), sourcePathValueMap.get(key));
                }

            });
        });
        return true;
    }

    private static boolean computeObjPathAndValue(Map<String, Object> targetPathValueMap, Map<String, Object> sourcePathValueMap, List<JsonConvertBO> objectEscapeList) {
        log.info("开始计算对象映射");
        objectEscapeList.forEach((o) -> {
            String sourcePath = o.getSourcePath();
            String targetPath = o.getTargetPath();
            Object sourceValue = sourcePathValueMap.get(sourcePath);
            if (null != sourceValue) {
                targetPathValueMap.put(targetPath, sourceValue);
            } else {
                log.warn("sourcePath未获取到对应的value,sourcePath:{}", sourcePath);
            }

        });
        return true;
    }

    private static boolean checkObject(JsonConvertBO o) {
        return !o.getSourcePath().contains("[]") && !o.getTargetPath().contains("[]") && !o.getTargetPath().contains("[&]");
    }

    private static boolean checkPath(String s, String sourcePath) {
        String splitPath = StringUtils.substringBeforeLast(sourcePath, ".");
        return s.equals(splitPath);
    }

    private static boolean checkSymmetryArr(JsonConvertBO o) {
        String sourcePath = o.getSourcePath();
        String targetPath = o.getTargetPath();
        boolean redundancyFlag = StringUtils.contains(targetPath, "[&]");
        int sArrSize = StringUtils.countMatches(sourcePath, "[]");
        int tArrSize = StringUtils.countMatches(targetPath, "[]");
        boolean result = !redundancyFlag && sArrSize == tArrSize && sArrSize > 0 && tArrSize > 0;
        return result;
    }

    private static JsonConvertTreeBO convertToTree(List<String> keyList) {
        JsonConvertTreeBO result = new JsonConvertTreeBO();
        JsonConvertTreeBO<String> forest = new JsonConvertTreeBO("root");
        JsonConvertTreeBO<String> current = forest;

        for(Iterator var4 = keyList.iterator(); var4.hasNext(); current = current) {
            String jsonConvertTreeBO = (String)var4.next();
            String[] var7 = jsonConvertTreeBO.split("\\.");
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String data = var7[var9];
                current = current.child(data);
            }
        }

        List<JsonConvertTreeBO> childJsonConvertTreeBOSet = forest.getChildren();
        if (CollectionUtils.isEmpty(childJsonConvertTreeBOSet)) {
            return result;
        } else {
            List<JsonConvertTreeBO> jsonConvertTreeBOList = new ArrayList(childJsonConvertTreeBOSet);
            return (JsonConvertTreeBO)jsonConvertTreeBOList.get(0);
        }
    }

    private static String createTargetJson(String targetJson, String path, JsonConvertTreeBO targetJsonConvertTreeBO) {
        String key = targetJsonConvertTreeBO.getData().toString();
        List<JsonConvertTreeBO> childJsonConvertTreeBO = targetJsonConvertTreeBO.getChildren();
        if (CollectionUtils.isEmpty(childJsonConvertTreeBO)) {
            return targetJson;
        } else {
            if (!"$".equals(key)) {
                Matcher keyMatcher = Pattern.compile("\\[[0-9]*\\]").matcher(key);
                Object addValue = new JSONObject();
                if (keyMatcher.find()) {
                    String addKey = key.replaceAll("\\[[0-9]*\\]", "");
                    String arrAddPath = StringUtils.joinWith(".", new Object[]{path, addKey});
                    if (key.contains("[0]")) {
                        targetJson = JsonPathUtils.addNewObject(targetJson, path, addKey, new JSONArray());
                    }

                    targetJson = JsonPathUtils.addNewObjectToArr(targetJson, arrAddPath, new JSONObject());
                } else {
                    targetJson = JsonPathUtils.addNewObject(targetJson, path, key, addValue);
                }

                path = StringUtils.joinWith(".", new Object[]{path, key});
            }

            Collections.sort(childJsonConvertTreeBO, (o1, o2) -> {
                return SortUtil.sortByPathSizeDesc(o1, o2);
            });

            JsonConvertTreeBO child;
            for(Iterator var9 = childJsonConvertTreeBO.iterator(); var9.hasNext(); targetJson = createTargetJson(targetJson, path, child)) {
                child = (JsonConvertTreeBO)var9.next();
            }

            return targetJson;
        }
    }

    static {
        taskExecutor = new ThreadPoolExecutor(6, 10, 30L, TimeUnit.SECONDS, new LinkedBlockingDeque(5), Executors.defaultThreadFactory(), new AbortPolicy());
    }
}

