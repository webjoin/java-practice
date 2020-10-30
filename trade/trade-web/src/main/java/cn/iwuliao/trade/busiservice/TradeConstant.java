package cn.iwuliao.trade.busiservice;

import cn.iwuliao.ds.core.DsScannerConfigurer;

public interface TradeConstant {

    /**
     * 值来源于：appDs.dbs.dba <br/>
     * 常量中的dba同配置的dba <br/>
     */
    String TRANSACTION_TEMPLATE_DBA = "dba" + DsScannerConfigurer.TRANSACTION_TEMPLATE;
}
