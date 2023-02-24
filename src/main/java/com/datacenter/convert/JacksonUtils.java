package com.datacenter.convert;

/**
 * @author pc
 * @date Create in  2023/2/22
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper.Builder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class JacksonUtils {
    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
    private static ObjectMapper mapper;
    private static final String TRUE = "true";
    private static final String FROM_ERROR = "jackson from error";
    private static final String CONFIG_ERROR = "jackson config error";
    private static final String CONVERSION_ERROR = "jackson conversion error";
    private static final String REMOVE_ERROR = "jackson remove json error";
    private static final String UPDATE_ERROR = "jackson update json error";
    private static final String FORMAT_ERROR = "jackson format json error";
    private static Set<JsonReadFeature> JSON_READ_FEATURES_ENABLED;

    public JacksonUtils() {
    }

    public static ObjectMapper initMapper() {
        Builder builder = JsonMapper.builder().enable((JsonReadFeature[])JSON_READ_FEATURES_ENABLED.toArray(new JsonReadFeature[0]));
        return initMapperConfig(builder.build());
    }

    public static ObjectMapper initMapperConfig(ObjectMapper objectMapper) {
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        objectMapper.setDateFormat(new SimpleDateFormat(dateTimeFormat));
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(new Feature[]{Feature.IGNORE_UNKNOWN});
        objectMapper.enable(new Feature[]{Feature.WRITE_BIGDECIMAL_AS_PLAIN});
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateTimeFormat);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dtf)).addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dtf));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public static String toJson(Object obj) {
        if (obj instanceof String) {
            return (String)obj;
        } else {
            try {
                String result = mapper.writeValueAsString(obj);
                return result;
            } catch (JsonProcessingException var2) {
                throw new JacksonException("jackson conversion error", var2.getMessage());
            }
        }
    }

    /**
     * 去转义
     * @param obj
     * @return
     */
    public static String unescapeToJson(Object obj) {
        try {
            String result = mapper.writeValueAsString(obj);
            result = StringEscapeUtils.unescapeJava(result);
            return result;
        } catch (JsonProcessingException var2) {
            throw new JacksonException("jackson conversion error", var2.getMessage());
        }
    }

    public static <V> V from(URL url, Class<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> V from(URL url, TypeReference<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> List<V> fromList(URL url, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return (List)mapper.readValue(url, collectionType);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> V from(InputStream inputStream, Class<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> V from(InputStream inputStream, TypeReference<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> List<V> fromList(InputStream inputStream, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return (List)mapper.readValue(inputStream, collectionType);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> V from(File file, Class<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> V from(File file, TypeReference<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> List<V> fromList(File file, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return (List)mapper.readValue(file, collectionType);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> V from(String json, Class<V> type) {
        return from((String)json, (Type)type);
    }

    public static <V> V from(String json, TypeReference<V> type) {
        return from(json, type.getType());
    }

    public static <V> V from(String json, Type type) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                JavaType javaType = mapper.getTypeFactory().constructType(type);
                return mapper.readValue(json, javaType);
            } catch (IOException var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static <V> List<V> fromList(String json, Class<V> type) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
                return (List)mapper.readValue(json, collectionType);
            } catch (IOException var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static Map<String, Object> fromMap(String json) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
                return (Map)mapper.readValue(json, mapType);
            } catch (IOException var2) {
                throw new JacksonException("jackson from error", var2.getMessage());
            }
        }
    }

    public static Map<String, Object> toMap(Object obj) {
        MapType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);

        try {
            Map<String, Object> resultMap = (Map)mapper.convertValue(obj, javaType);
            return resultMap;
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <V> String to(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException var2) {
            throw new JacksonException("jackson from error", var2.getMessage());
        }
    }

    public static <V> String to(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException var2) {
            throw new JacksonException("jackson from error", var2.getMessage());
        }
    }

    public static <V> void toFile(String path, List<V> list) {
        try {
            Writer writer = new FileWriter(new File(path), true);
            Throwable var3 = null;

            try {
                mapper.writer().writeValues(writer).writeAll(list);
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (writer != null) {
                    if (var3 != null) {
                        try {
                            writer.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        writer.close();
                    }
                }

            }

        } catch (Exception var15) {
            throw new JacksonException("jackson from error", var15.getMessage());
        }
    }

    public static <V> void toFile(String path, V v) {
        try {
            Writer writer = new FileWriter(new File(path), true);
            Throwable var3 = null;

            try {
                mapper.writer().writeValues(writer).write(v);
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (writer != null) {
                    if (var3 != null) {
                        try {
                            writer.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        writer.close();
                    }
                }

            }

        } catch (Exception var15) {
            throw new JacksonException("jackson from error", var15.getMessage());
        }
    }

    public static String getAsString(String json, String key) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                return null == jsonNode ? null : getAsString(jsonNode);
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    private static String getAsString(JsonNode jsonNode) {
        return jsonNode.isTextual() ? jsonNode.textValue() : jsonNode.toString();
    }

    public static int getAsInt(String json, String key) {
        if (isEmpty(json)) {
            return 0;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return 0;
                } else {
                    return jsonNode.isInt() ? jsonNode.intValue() : Integer.parseInt(getAsString(jsonNode));
                }
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static long getAsLong(String json, String key) {
        if (isEmpty(json)) {
            return 0L;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return 0L;
                } else {
                    return jsonNode.isLong() ? jsonNode.longValue() : Long.parseLong(getAsString(jsonNode));
                }
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static double getAsDouble(String json, String key) {
        if (isEmpty(json)) {
            return 0.0D;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return 0.0D;
                } else {
                    return jsonNode.isDouble() ? jsonNode.doubleValue() : Double.parseDouble(getAsString(jsonNode));
                }
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static BigInteger getAsBigInteger(String json, String key) {
        if (isEmpty(json)) {
            return new BigInteger(String.valueOf(0.0D));
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return new BigInteger(String.valueOf(0.0D));
                } else {
                    return jsonNode.isBigInteger() ? jsonNode.bigIntegerValue() : new BigInteger(getAsString(jsonNode));
                }
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static BigDecimal getAsBigDecimal(String json, String key) {
        if (isEmpty(json)) {
            return new BigDecimal("0.00");
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return new BigDecimal("0.00");
                } else {
                    return jsonNode.isBigDecimal() ? jsonNode.decimalValue() : new BigDecimal(getAsString(jsonNode));
                }
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static byte[] getAsBytes(String json, String key) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return null;
                } else {
                    return jsonNode.isBinary() ? jsonNode.binaryValue() : getAsString(jsonNode).getBytes();
                }
            } catch (Exception var3) {
                throw new JacksonException("jackson from error", var3.getMessage());
            }
        }
    }

    public static <V> V getAsObject(String json, String key, Class<V> type) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return null;
                } else {
                    JavaType javaType = mapper.getTypeFactory().constructType(type);
                    return from((String)getAsString(jsonNode), (Type)javaType);
                }
            } catch (Exception var5) {
                throw new JacksonException("jackson get list error", var5.getMessage());
            }
        }
    }

    public static <V> List<V> getAsList(String json, String key, Class<V> type) {
        if (isEmpty(json)) {
            return null;
        } else {
            try {
                JsonNode jsonNode = getAsJsonObject(json, key);
                if (null == jsonNode) {
                    return null;
                } else {
                    CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
                    return (List)from((String)getAsString(jsonNode), (Type)collectionType);
                }
            } catch (Exception var5) {
                throw new JacksonException("jackson get list error", var5.getMessage());
            }
        }
    }

    public static JsonNode getAsJsonObject(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            return null == node ? null : node.get(key);
        } catch (IOException var3) {
            throw new JacksonException("jackson from error", var3.getMessage());
        }
    }

    public static <V> String add(String json, String key, V value) {
        try {
            JsonNode node = mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException var4) {
            throw new JacksonException("jackson from error", var4.getMessage());
        }
    }

    private static <V> void add(JsonNode jsonNode, String key, V value) {
        if (value instanceof String) {
            ((ObjectNode)jsonNode).put(key, (String)value);
        } else if (value instanceof Short) {
            ((ObjectNode)jsonNode).put(key, (Short)value);
        } else if (value instanceof Integer) {
            ((ObjectNode)jsonNode).put(key, (Integer)value);
        } else if (value instanceof Long) {
            ((ObjectNode)jsonNode).put(key, (Long)value);
        } else if (value instanceof Float) {
            ((ObjectNode)jsonNode).put(key, (Float)value);
        } else if (value instanceof Double) {
            ((ObjectNode)jsonNode).put(key, (Double)value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode)jsonNode).put(key, (BigDecimal)value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode)jsonNode).put(key, (BigInteger)value);
        } else if (value instanceof Boolean) {
            ((ObjectNode)jsonNode).put(key, (Boolean)value);
        } else if (value instanceof byte[]) {
            ((ObjectNode)jsonNode).put(key, (byte[])((byte[])value));
        } else {
            ((ObjectNode)jsonNode).put(key, to(value));
        }

    }

    public static String remove(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode)node).remove(key);
            return node.toString();
        } catch (IOException var3) {
            throw new JacksonException("jackson remove json error", var3.getMessage());
        }
    }

    public static <V> String update(String json, String key, V value) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode)node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException var4) {
            throw new JacksonException("jackson update json error", var4.getMessage());
        }
    }

    public static String format(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException var2) {
            throw new JacksonException("jackson format json error", var2.getMessage());
        }
    }

    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    private static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static JsonNode readTree(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException var2) {
            throw new JacksonException("jackson conversion error", var2.getMessage());
        }
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        if (map == null) {
            return null;
        } else {
            T obj = beanClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            Field[] var4 = fields;
            int var5 = fields.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Field field = var4[var6];
                int mod = field.getModifiers();
                if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                    field.setAccessible(true);
                    field.set(obj, map.get(field.getName()));
                }
            }

            return obj;
        }
    }

    static {
        try {
            JSON_READ_FEATURES_ENABLED = new CopyOnWriteArraySet();
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_JAVA_COMMENTS);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_SINGLE_QUOTES);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_TRAILING_COMMA);
            JSON_READ_FEATURES_ENABLED.add(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
            mapper = initMapper();
        } catch (Exception var1) {
            throw new JacksonException("jackson config error", var1.getMessage());
        }
    }
}
