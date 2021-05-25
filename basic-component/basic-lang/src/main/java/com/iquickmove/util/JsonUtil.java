package com.iquickmove.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iquickmove.util.exception.JsonParseException;

import java.io.IOException;
import java.util.Optional;

/**
 * @author tangyu
 * @since 2019-04-06 04:52
 */
public class JsonUtil {

    private final static String LEFT_MID_BRACKET = "[";
    private final static String LEFT_BIG_BRACKET = "{";

    private final static String RIGHT_MID_BRACKET_RIGHT = "]";
    private final static String RIGHT_BIG_BRACKET_RIGHT = "}";

    private final static String BIG_BRACKET = LEFT_BIG_BRACKET + RIGHT_BIG_BRACKET_RIGHT;
    private final static String MID_BRACKET = LEFT_MID_BRACKET + RIGHT_MID_BRACKET_RIGHT;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(new JavaTimeModule());
    }

    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonObjStr(Object obj) {
        final String json1 = toJsonStr(obj);
        return Optional.ofNullable(json1).orElse(BIG_BRACKET);
    }

    public static String toJsonArr(Object obj) {
        return Optional.ofNullable(toJsonStr(obj)).orElse(MID_BRACKET);
    }

    public static String toJsonStr(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    /**
     * @param jsonStr
     *            字符串
     * @param objClass
     *            对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, objClass);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    /**
     * @param jsonStr
     *            字符串
     * @param typeReference
     *            对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static <T> T json2Bean2(Object jsonObject, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(toJsonArr(jsonObject), typeReference);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static JsonNode toJsonNode(String body) {
        try {
            return OBJECT_MAPPER.readTree(body);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e.getMessage());
        }
    }
}
