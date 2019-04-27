package cn.iwuliao.ds.core;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "druid2")
public class DbConf {

    private String poolType;

    private Properties base;

    private Map<String, Db> dbs;


}