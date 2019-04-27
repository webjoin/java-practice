package cn.iwuliao.ds.core;

/**
 * @author yangjunming
 */
public class DbContextHolder {

    public enum DbType {
        MASTER, SLAVE, SLAVE1
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        if (dbType == null) {
            throw new NullPointerException();
        }
        contextHolder.set(dbType);
    }

    public static DbType getDbType() {
        return contextHolder.get() == null ? DbType.MASTER : contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }

}