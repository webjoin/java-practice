package cn.iwuliao.ds;

import cn.iwuliao.ds.core.DsScannerConfigurer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

/**
 * @author tangyu
 * @since 2019-04-22 20:20
 */
//@Slf4j
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class MultiDsConfApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MultiDsConfApp.class, args);
        Map<String, SqlSessionFactoryBean> beansOfType = run.getBeansOfType(SqlSessionFactoryBean.class);
        Map<String, MapperScannerConfigurer> beansOfType1 = run.getBeansOfType(MapperScannerConfigurer.class);
        Map<String, DataSourceTransactionManager> beansOfType2 = run.getBeansOfType(DataSourceTransactionManager.class);
        DataSourceTransactionManager bean = run.getBean("dba" + DsScannerConfigurer.TRANSACTIONMANAGER, DataSourceTransactionManager.class);
        System.out.println(beansOfType);
        System.out.println(beansOfType1);
        System.out.println(beansOfType2);
        System.out.println(bean);
    }
}