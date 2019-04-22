package cn.iwuliao.ds.util;

import cn.iwuliao.cons.Cons;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author tangyu
 * @since 2019-04-06 04:52
 */
public class JsonUtil {
    private static ObjectMapper objectMapper;

    /**
     * 启动服务的时候调用
     *
     * @param objectMapper1
     */
    public static void init(ObjectMapper objectMapper1) {
        if (Objects.isNull(objectMapper1)) {
            if (Objects.isNull(objectMapper)) {
                synchronized (JsonUtil.class) {
                    if (Objects.isNull(objectMapper)) {
                        objectMapper = new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .registerModule(new JavaTimeModule());
                    }
                }
            }
        } else {
            synchronized (JsonUtil.class) {
                objectMapper = objectMapper1;
            }
        }
    }

    /**
     * 通常只有本地测试的时候使用
     */
    private static void init1() {
        init(null);
    }


    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonObjStr(Object obj) {
        final String json1 = toJsonStr(obj);
        return Optional.ofNullable(json1).orElse(Cons.Chars.BIG_BRACKET);
    }


    public static String toJsonArr(Object obj) {
        final String json = toJsonStr(obj);
        return Optional.ofNullable(json).orElse(Cons.Chars.MID_BRACKET);
    }

    public static String toJsonStr(Object obj) {
        final String json;
        try {
            init1();
            json = objectMapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    /**
     * @param jsonStr  字符串
     * @param objClass 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        try {
            init1();
            return objectMapper.readValue(jsonStr, objClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }


    /**
     * @param jsonStr       字符串
     * @param typeReference 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, TypeReference<T> typeReference) {
        try {
            init1();
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static <T> T json2Bean2(Object jsonObject, TypeReference<T> typeReference) {
        try {
            init1();

            return objectMapper.readValue(toJsonArr(jsonObject), typeReference);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }


}
