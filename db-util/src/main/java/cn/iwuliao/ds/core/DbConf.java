package cn.iwuliao.ds.core;

import java.util.Map;
import java.util.Properties;

public class DbConf {

    private String poolType;

    private Properties base;

    private Map<String, Db> dbs;

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public Properties getBase() {
        return base;
    }

    public void setBase(Properties base) {
        this.base = base;
    }

    public Map<String, Db> getDbs() {
        return dbs;
    }

    public void setDbs(Map<String, Db> dbs) {
        this.dbs = dbs;
    }
}