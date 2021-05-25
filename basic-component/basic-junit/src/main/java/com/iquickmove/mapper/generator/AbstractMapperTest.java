package com.iquickmove.mapper.generator;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * @author tangyu
 * @since 2017-12-25 13:51
 */
public class AbstractMapperTest {

    protected final static String UNDERLINE = "_";
    protected final static String T_PREFIX = "qm_";

    protected void generateClass(String tableName) {
        generateClass(tableName, null, T_PREFIX);
    }

    /**
     *
     * @param tableName
     * @param tablePrefix
     *            可不填默认 qm_ ,
     */
    protected void generateClass(String tableName, String tablePrefix) {
        generateClass(tableName, null, tablePrefix);
    }

    /**
     * 生成类
     *
     * @param tableName
     * @param domainObjectName
     *            对应表的类名 ，可为空
     */
    protected void generateClass(String tableName, String domainObjectName, String tablePrefix) {
        // System.setProperty("dbName", getDbName());
        // 表映射对象名称
        String mapperName = null;
        if (StringUtils.isBlank(domainObjectName)) {
            String domainName = getClassName(tableName, tablePrefix);
            domainObjectName = domainName + "Entity";
            mapperName = domainName + "Mapper";
        }
        Objects.requireNonNull(mapperName);
        Objects.requireNonNull(domainObjectName);
        System.setProperty("tableName", tableName);
        System.setProperty("useActualColumnNames", "false");
        System.setProperty("domainObjectName", domainObjectName);
        System.setProperty("mapperName", mapperName);
        System.setProperty("primaryKey", "id");
    }

    protected static String getClassName(String tableName, String tablePrefix) {
        for (String s : tablePrefix.split(",")) {
            tableName = tableName.replaceFirst(s, "");
        }
        boolean contains = tableName.contains(UNDERLINE);
        StringBuilder sb = new StringBuilder();
        if (contains) {
            String lowerTableName = tableName.toLowerCase();
            String[] worlds = lowerTableName.split(UNDERLINE);
            for (String world : worlds) {
                char lowChar = world.charAt(0);
                world = world.replaceFirst(Character.toString(lowChar), getUpper(lowChar));
                sb.append(world);
            }
        } else {
            char lowChar = tableName.charAt(0);
            if (Character.isLowerCase(lowChar)) {
                tableName = tableName.replaceFirst(Character.toString(lowChar), getUpper(lowChar));
            }
            sb.append(tableName);
        }
        return sb.toString();
    }

    private static String getUpper(char lower) {
        return Character.toString((char)(lower - 32));
    }

    private static String getLower(char chaz) {
        return Character.toString((char)(chaz + 32));
    }

    @AfterEach
    public void after() throws URISyntaxException {

        System.err.println("--");
        System.err.println(
            "如果字段名包含了\"_\"请设置 System.setProperty(\"useActualColumnNames\", \"false\") 参考方法GeneratorRestaurantTest.testGenerateTRoleMenu");
        System.err.println("如果表逐渐字段名称非id 则请设置 主键字段名称 primaryKeyName 参考方法GeneratorRestaurantTest.testGenerateTRoleMenu");
        System.err.println("--");

        String absolutePath = "generatorConfig.xml";

        String[] args1 = {"-configfile", absolutePath, "-overwrite"};
        ShellRunnerExt.main(args1);

        System.out.println("Done!~~~ #:-)");
    }

    /**
     * 子类可以重写
     */
    public String getDbName() {
        return "media_test";
    }
}
