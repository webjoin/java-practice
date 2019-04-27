package cn.iwuliao.ds.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Properties;

@Setter
@Getter
@ToString
public class Db {

    private Properties master;
    
    private Map<String, Properties> slaves;

    private String mapperLocations;
    private String typeHandlersPackage;
    private String mapperScannerPackage;
}