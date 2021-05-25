package com.iquickmove.ds.core;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author tangyu
 * @since 2019-04-06 03:13
 */
public class DatasourceUtil {

    /**
     * 获取 SqlsessionFactory 对象
     *
     * @param dataSource
     * @param location
     * @param handlersPackage
     * @return
     * @throws Exception
     */
    public static SqlSessionFactory getSqlSessionFactory(DataSource dataSource, String location, String handlersPackage) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(location));
        if (Objects.nonNull(handlersPackage)) {
            factory.setTypeHandlersPackage(handlersPackage);
        }
        return factory.getObject();
    }

    /**
     * 将Mapper 文件 注入到IOC 容器
     *
     * @param baseRestSlaveSqlSessionFactory
     * @param basePackage
     * @return
     */
    public static MapperScannerConfigurer getMapperScannerConfigurer(String baseRestSlaveSqlSessionFactory, String basePackage) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(basePackage);
        //使用实体类名/属性名作为表名/字段名
        return mapperScannerConfigurer;
    }


    /**
     * 动态获取数据源
     *
     * @param master
     * @param slave
     * @return
     */
    public static MasterSlaveRoutingDataSource getRoutingDataSource(DataSource master, DataSource slave) {

        MasterSlaveRoutingDataSource proxy = new MasterSlaveRoutingDataSource();

        Map<Object, Object> targetDataResources = new HashMap<>();
        targetDataResources.put(DbContextHolder.DbType.MASTER, master);
        targetDataResources.put(DbContextHolder.DbType.SLAVE, slave);

        proxy.setDefaultTargetDataSource(master);
        proxy.setTargetDataSources(targetDataResources);

        proxy.afterPropertiesSet();
        return proxy;
    }

    /**
     * 事物管理
     *
     * @param nowwarningDataSource
     * @return
     */
    public static PlatformTransactionManager getransactionManager(DataSource nowwarningDataSource) {

        return new DataSourceTransactionManager(nowwarningDataSource);
    }

}
