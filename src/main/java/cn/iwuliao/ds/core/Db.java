package cn.iwuliao.ds.core;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Properties;

@Setter
@Getter
@ToString
public class Db {

//    private DruidDataSource master;
//    private DruidDataSource slave;
//    private DruidDataSource slave1;
//
    private Properties master;
    private Properties slave;
    private Properties slave1;

    private String mapperLocations;
    private String typeHandlersPackage;
    private String mapperScannerPackage;
}