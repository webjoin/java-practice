package com.iquickmove.util.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.iquickmove.util.DesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * ID加密后 反序列化(解密操作)
 *
 * @author elijah
 */
@Slf4j
public class IdDeserializer extends StdDeserializer<Integer> {

    public IdDeserializer() {
        this(Integer.class);
    }

    protected IdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String s = node.asText();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        if (IdSerializer.ENABLE) {
            s = s.indexOf("+") < 0 && s.indexOf("%") > -1 ? URLDecoder.decode(s,"utf-8") : s; // 为了兼容 url 后拼接参数导致编码问题，凡是进来的参数中没有加号(+) 且 有% 号 都decode
            try {
                return Integer.parseInt(Objects.requireNonNull(DesUtils.decrypt(s, IdSerializer.KEY)));
            } catch (Exception e) {
                log.error("IdDeserializer.deserialize >>> s:{},error:{}",s, ExceptionUtils.getStackTrace(e));  // todo
                throw e;
            }

        } else {
            return node.asInt();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println(DesUtils.decrypt(URLDecoder.decode("m%2b8ke3coVHo%3d","utf-8"), IdSerializer.KEY));
        System.out.println(URLDecoder.decode("m%2b8ke3coVHo%3d","utf-8"));
        System.out.println(DesUtils.decrypt("ITKymEkq2e4", IdSerializer.KEY));
    }
}
