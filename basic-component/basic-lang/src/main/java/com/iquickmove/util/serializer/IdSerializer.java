package com.iquickmove.util.serializer;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.iquickmove.util.DesUtils;

/**
 * ID加密 序列化(加密操作)
 *
 * @author elijah
 */
public class IdSerializer extends StdSerializer<Integer> {

    public static final String KEY = "hi1234567890";
    public static final String ID_ENCRYPT = "id.encrypt";
    public static final boolean ENABLE = System.getProperties().containsKey(ID_ENCRYPT);

    public IdSerializer() {
        super(Integer.class);
    }

    @Override
    public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value == null) {
            return;
        }
        if (ENABLE) {
            jgen.writeString(DesUtils.encrypt(Objects.toString(value), KEY));
        } else {
            jgen.writeNumber(value);
        }
    }
}
