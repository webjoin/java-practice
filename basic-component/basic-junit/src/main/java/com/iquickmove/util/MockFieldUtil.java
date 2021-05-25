package com.iquickmove.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class MockFieldUtil {

    public static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static long requestNo() {
        String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmSSS");
        String nextSeq = String.format("%03d", COUNTER.incrementAndGet());
        String seq = "7" + date + StringUtils.substring(nextSeq, nextSeq.length() - 3);
        return Long.parseLong(seq);
    }

    public static String stringNo() {
        String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmSSS");
        String nextSeq = String.format("%03d", COUNTER.incrementAndGet());
        return "10" + date + StringUtils.substring(nextSeq, nextSeq.length() - 3);
    }

    public static void main(String[] args) {
        System.out.println(requestNo());
    }

}
