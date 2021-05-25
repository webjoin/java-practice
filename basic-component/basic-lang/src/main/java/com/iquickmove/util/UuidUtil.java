package com.iquickmove.util;

import java.util.UUID;

public final class UuidUtil {

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}