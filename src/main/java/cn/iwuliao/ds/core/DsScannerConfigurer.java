package cn.iwuliao.ds.core;

import cn.iwuliao.ds.core.yml.PropertiesToYamlConverter;
import cn.iwuliao.ds.core.yml.YamlConversionResult;
import cn.iwuliao.ds.util.JsonUtil;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.parsing.DefaultsDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.json.YamlJsonParser;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
@Component
public class DsScannerConfigurer implements BeanFactoryAware, Ordered, ApplicationContextAware {

    private BeanFactory beanFactory;

    private static final String PREFIX = "druid2.";
    private static final String DRUID_DS_PROPS_PREFIX = "druid.";
    public static final String TRANSACTIONMANAGER = "TransactionManager";
    private static final String MAPPERSCANNERCONFIGURER = "MapperScannerConfigurer";
    public static final String SQLSESSIONFACTORY = "SqlSessionFactory";


    @Autowired
    private DbConf dbConf;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        try {
            registryDs(registry);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getOrder() {
        return -100;
    }


    private void registryDs(BeanDefinitionRegistry registry) throws ClassNotFoundException {
        String poolType = dbConf.getPoolType();
        if (Objects.nonNull(poolType)) {

            Map<String, Db> dbs = dbConf.getDbs();
            Set<Map.Entry<String, Db>> entries = dbs.entrySet();
            for (Map.Entry<String, Db> entry : entries) {
                String dbName = entry.getKey();
                Db value = entry.getValue();
                String mapperLocations = value.getMapperLocations();
                String mapperScannerPackage = value.getMapperScannerPackage();
                String typeHandlersPackage = value.getTypeHandlersPackage();
                Properties masterProperties = getDsProps(value.getMaster());
                Map<String, Properties> slaves = value.getSlaves();
                List<Properties> salves1 = new ArrayList<>();
                if (Objects.nonNull(slaves)) {
                    slaves.entrySet().forEach(x -> salves1.add(getDsProps(slaves.get(x))));
                }
                DruidDataSource master = new DruidDataSource();
                master.setConnectProperties(masterProperties);
                List<DruidDataSource> slaveDatasources = new ArrayList<>();
                if (!salves1.isEmpty()) {
                    salves1.forEach(x -> {
                        DruidDataSource ds = new DruidDataSource();
                        ds.setConnectProperties(x);
                        slaveDatasources.add(ds);
                    });
                }

                //获取动态数据源
                registryDynamicDs(master, slaveDatasources, dbName, registry);
                //事物
                registryDynamicDsTrancationManager(dbName, registry);

                //获取SqlSessionFactory
                registrySqlSessionFactoryBean(mapperLocations, typeHandlersPackage, dbName, registry);
                //scanMapper
                registryMapperScan(mapperScannerPackage, dbName, registry);

            }
        }
    }


    private Properties getDsProps(Properties master) {
        Properties props = new Properties();
        Enumeration<?> enumeration = master.propertyNames();
        while (enumeration.hasMoreElements()) {
            String k = (String) enumeration.nextElement();
            Object val = master.get(k);
            props.put(DRUID_DS_PROPS_PREFIX + k, val);
        }
        return props;
    }

    private void registrySqlSessionFactoryBean(String mapperLocations, String typeHandlersPackage, String dbName, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(SqlSessionFactoryBean.class)
                .addPropertyValue("dataSource", new RuntimeBeanReference(dbName))
                .addPropertyValue("mapperLocations", mapperLocations)
                .addPropertyValue("typeHandlersPackage", typeHandlersPackage);

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
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

        //构建参数
        beanDefinition.setBeanClass(DataSourceTransactionManager.class);
        //依赖数据构建
        beanDefinition.getPropertyValues().addPropertyValue("dataSource", new RuntimeBeanReference(dbName));

        beanDefinition.setDependsOn(dbName);
        //注册IOC
        registry.registerBeanDefinition(dbName + TRANSACTIONMANAGER, beanDefinition);
    }

    private void registryDynamicDs(DruidDataSource master, List<DruidDataSource> slaves, String dbName, BeanDefinitionRegistry registry) {

        Map<Object, Object> targetDataResources = new HashMap<>();
        targetDataResources.put(DbContextHolder.DbType.MASTER, master);
        if (Objects.nonNull(slaves) && !slaves.isEmpty()) {
            targetDataResources.put(DbContextHolder.DbType.SLAVE, slaves.get(0));
        }


        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MasterSlaveRoutingDataSource.class);
        beanDefinition.getPropertyValues().add("defaultTargetDataSource", master);
        beanDefinition.getPropertyValues().add("targetDataSources", targetDataResources);

        registry.registerBeanDefinition(dbName, beanDefinition);
    }


    /**
     * 目前无效代码了
     *
     * @param properties
     * @return
     */
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
}

