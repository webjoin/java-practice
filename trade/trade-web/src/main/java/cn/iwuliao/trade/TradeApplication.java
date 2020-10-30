package cn.iwuliao.trade;

import cn.iwuliao.ds.core.DsScannerConfigurer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

/**
 * @author elijah
 */
@EnableTransactionManagement
@SpringBootApplication(
    exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class},
    scanBasePackages = "cn.iwuliao")
@EnableFeignClients(basePackages = {"cn.iwuliao.trade.ext.integration"})
public class TradeApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TradeApplication.class, args);
        Map<String, SqlSessionFactoryBean> beansOfType = run.getBeansOfType(SqlSessionFactoryBean.class);
        Map<String, MapperScannerConfigurer> beansOfType1 = run.getBeansOfType(MapperScannerConfigurer.class);
        Map<String, DataSourceTransactionManager> beansOfType2 = run.getBeansOfType(DataSourceTransactionManager.class);
        DataSourceTransactionManager bean =
            run.getBean("dba" + DsScannerConfigurer.TRANSACTIONMANAGER, DataSourceTransactionManager.class);

        System.out.println(beansOfType);
        System.out.println(beansOfType1);
        System.out.println(beansOfType2);

        System.out.println(bean);
        TransactionTemplate bean1 =
            run.getBean("dba" + DsScannerConfigurer.TRANSACTION_TEMPLATE, TransactionTemplate.class);
        System.out.println(bean1);
    }
}
