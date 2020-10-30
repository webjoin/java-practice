package cn.iwuliao.trade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import cn.iwuliao.trade.api.enums.TradeStatus;
import cn.iwuliao.trade.api.enums.TradeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* Created by Mybatis Generator on 2020/10/30
*/
@Setter
@Getter
@ToString
public class TradeOrderEntity {
    /**
     * 交易ID
     */
    private Long tradeId;

    /**
     * 交易凭证号
     */
    private Long voucherNo;

    /**
     * 付款人ID
     */
    private String payerId;

    /**
     * 交易类型 T、P 
     */
    private TradeType tradeType;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 产品码
     */
    private String productCode;

    /**
     * 交易状态
     */
    private TradeStatus tradeStatus;

    /**
     * 调用客户端
     */
    private String clientId;

    /**
     * 扩展参数
     */
    private Map<String, String> extension;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
}