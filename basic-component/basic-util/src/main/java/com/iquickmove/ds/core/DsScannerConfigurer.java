package com.iquickmove.ds.core;

import java.util.*;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.iquickmove.ds.core.yml.PropertiesToYamlConverter;
import com.iquickmove.ds.core.yml.YamlConversionResult;
import com.iquickmove.util.JsonUtil;

/**
 * com.zaxxer.hikari.HikariDataSource
 */
@Component
public class DsScannerConfigurer {
    private static final Logger log = LoggerFactory.getLogger(DsScannerConfigurer.class);

    private static final String PREFIX = "appDs.";
    private static final String DRUID_DS_PROPS_PREFIX = "";
    public static final String TRANSACTIONMANAGER = "TransactionManager";
    public static final String TRANSACTION_TEMPLATE = "TransactionTemplate";
    private static final String MAPPERSCANNERCONFIGURER = "MapperScannerConfigurer";
    public static final String SQLSESSIONFACTORY = "SqlSessionFactory";

    @Bean
    public BeanDefinitionRegistryPostProcessor factory(final StandardEnvironment environment) {

        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                MutablePropertySources propertySources = environment.getPropertySources();
                Properties properties = new Properties();
                propertySources.forEach(x -> {
                    if (x.getSource() instanceof LinkedHashMap || x.getName().contains(".yml")) {
                        Map props = (Map) x.getSource();
                        props.forEach((k, v) -> {
                            String key = k.toString();
                            boolean druid2 = key.startsWith(PREFIX);
                            if (druid2) {
                                properties.put(key, v);
                            }
                        });
                    }
                });
                boolean empty = properties.isEmpty();
                if (!empty) {
                    DbConf dbConf1 = getDsConfBean(properties);
                    try {
                        registryDs(dbConf1, registry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

            }
        };
    }

    private void registryDs(DbConf properties, BeanDefinitionRegistry registry) {
        String poolType = properties.getPoolType();
        if (Objects.nonNull(poolType)) {
            Map<String, Db> dbs = properties.getDbs();
            Set<Map.Entry<String, Db>> entries = dbs.entrySet();
            for (Map.Entry<String, Db> entry : entries) {
                String dbName = entry.getKey();
                Db value = entry.getValue();
                String mapperLocations = value.getMapperLocations();
                String mapperScannerPackage = value.getMapperScannerPackage();
                String configLocation = value.getConfigLocation();
                Properties masterProperties = getDsProps(value.getMaster());
                Properties slaveProperties = getDsProps(value.getSlave());

                HikariDataSource master = new HikariDataSource(new HikariConfig(masterProperties));
                HikariDataSource slave = null;
                if (slaveProperties != null) {
                    slave = new HikariDataSource(new HikariConfig(slaveProperties));
                }
                log.info("数据源【{}】启动成功", dbName);

                // 获取动态数据源
                registryDynamicDs(master, slave, dbName, registry);
                // 事物
                registryDynamicDsTrancationManager(dbName, registry);

                // 获取SqlSessionFactory
                registrySqlSessionFactoryBean(mapperLocations, configLocation, dbName, registry);
                // scanMapper
                registryMapperScan(mapperScannerPackage, dbName, registry);

            }
        }
    }

    private Properties getDsProps(Properties master) {
        if (master == null) {
            return null;
        }
        Properties props = new Properties();
        Enumeration<?> enumeration = master.propertyNames();
        while (enumeration.hasMoreElements()) {
            String k = (String) enumeration.nextElement();
            Object val = master.get(k);
            props.put(DRUID_DS_PROPS_PREFIX + k, val);
        }
        return props;
    }

    private void registrySqlSessionFactoryBean(String mapperLocations, String configLocation, String dbName,
                                               BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(SqlSessionFactoryBean.class)
                .addPropertyValue("dataSource", new RuntimeBeanReference(dbName))
                .addPropertyValue("configLocation", configLocation)
                .addPropertyValue("mapperLocations", mapperLocations.split(","));

        registry.registerBeanDefinition(dbName + SQLSESSIONFACTORY, beanDefinitionBuilder.getBeanDefinition());
    }

    private void registryMapperScan(String mapperScannerPackage, String dbName, BeanDefinitionRegistry registry) {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(MapperScannerConfigurer.class)
                .addPropertyValue("basePackage", mapperScannerPackage)
                .addPropertyValue("sqlSessionFactoryBeanName", dbName + SQLSESSIONFACTORY);

        registry.registerBeanDefinition(dbName + MAPPERSCANNERCONFIGURER, beanDefinitionBuilder.getBeanDefinition());

    }

    private void registryDynamicDsTrancationManager(String dbName, BeanDefinitionRegistry registry) {

        {
            // register DataSourceTransactionManager
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            // 构建参数
            beanDefinition.setBeanClass(DataSourceTransactionManager.class);
            // 依赖数据构建
            beanDefinition.getPropertyValues()
                    .addPropertyValue("dataSource", new RuntimeBeanReference(dbName));
            beanDefinition.setDependsOn(dbName);
            // 注册IOC
            registry.registerBeanDefinition(dbName + TRANSACTIONMANAGER, beanDefinition);
        }
        {
            // register TransactionTemplate
            GenericBeanDefinition beanDefinition01 = new GenericBeanDefinition();
            // 构建参数
            beanDefinition01.setBeanClass(TransactionTemplate.class);
            // 依赖数据构建
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            argumentValues.addGenericArgumentValue(new RuntimeBeanReference(dbName + TRANSACTIONMANAGER));
            beanDefinition01.setConstructorArgumentValues(argumentValues);
            // 注册IOC
            registry.registerBeanDefinition(dbName + TRANSACTION_TEMPLATE, beanDefinition01);
        }
    }

    private void registryDynamicDs(DataSource master, DataSource slave, String dbName,
                                   BeanDefinitionRegistry registry) {

        Map<Object, Object> targetDataResources = new HashMap<>();
        targetDataResources.put(DbContextHolder.DbType.MASTER, master);
        if (slave != null) {
            targetDataResources.put(DbContextHolder.DbType.SLAVE, slave);
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MasterSlaveRoutingDataSource.class);
        beanDefinition.getPropertyValues().add("defaultTargetDataSource", master);
        beanDefinition.getPropertyValues().add("targetDataSources", targetDataResources);

        registry.registerBeanDefinition(dbName, beanDefinition);
    }

    private DbConf getDsConfBean(Properties properties) {
        PropertiesToYamlConverter converter = new PropertiesToYamlConverter();

        StringBuilder sb = new StringBuilder();
        properties.forEach((k, v) -> sb.append(k.toString().replace(PREFIX, "")).append("=").append(v).append("\n"));

        YamlConversionResult convert = converter.convert(sb.toString());

        String docment = convert.getYaml();

        PropertyUtils propUtils = new PropertyUtils();
        propUtils.setAllowReadOnlyProperties(true);
        propUtils.setSkipMissingProperties(true);
        Representer repr = new Representer();
        repr.setPropertyUtils(propUtils);

        Yaml yaml = new Yaml(new Constructor(), repr);

        Map ymalMap = yaml.loadAs(docment, Map.class);
        String s = JsonUtil.toJsonStr(ymalMap);
        DbConf dbConf11 = JsonUtil.json2Bean(s, DbConf.class);
        return dbConf11;
    }

    public static void main(String[] args) {
        Map ymalMap = new HashMap();
        ymalMap.put("poolType", "wwwww");
        DbConf dbConf1 = new DbConf();
        BeanMap beanMap = BeanMap.create(dbConf1);
        beanMap.putAll(ymalMap);

        String s = JsonUtil.toJsonStr(ymalMap);
        DbConf dbConf11 = JsonUtil.json2Bean(s, DbConf.class);

        System.out.println(dbConf11);

    }

}
