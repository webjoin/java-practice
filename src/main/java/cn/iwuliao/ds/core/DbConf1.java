package cn.iwuliao.ds.core;

import lombok.*;

import java.util.Map;
import java.util.Properties;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DbConf1 {

    private String poolType;

    private Properties base;

    private Map<String, Db> dbs;


}