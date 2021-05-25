package com.iquickmove.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ancoka
 * @version V1.0
 * @package net.wangxu.bm.config.third
 * @date 2020-07-26 17:59
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = com.iquickmove.base.config.AliyunSmsConfig.PREFIX)
public class AliyunSmsConfig {
    public static final String PREFIX = "aliyun.sms";

    private String regionId;

    private String accessKeyId;

    private String secret;
}
