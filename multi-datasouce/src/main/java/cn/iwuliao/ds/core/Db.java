package cn.iwuliao.ds.core;

import java.util.Properties;

public class Db {

    private Properties master;
    private Properties slave;
    private Properties slave1;

    private String mapperLocations;
    private String typeHandlersPackage;
    private String mapperScannerPackage;

    public Properties getMaster() {
        return master;
    }

    public void setMaster(Properties master) {
        this.master = master;
    }

    public Properties getSlave() {
        return slave;
    }

    public void setSlave(Properties slave) {
        this.slave = slave;
    }

    public Properties getSlave1() {
        return slave1;
    }

    public void setSlave1(Properties slave1) {
        this.slave1 = slave1;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getTypeHandlersPackage() {
        return typeHandlersPackage;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public String getMapperScannerPackage() {
        return mapperScannerPackage;
    }

    public void setMapperScannerPackage(String mapperScannerPackage) {
        this.mapperScannerPackage = mapperScannerPackage;
    }
}