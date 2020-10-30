package cn.iwuliao.trade.api.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author elijah
 */
public class TradeOrderInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long voucherNo;

    /**
     * 付款人ID
     */
    private String payerId;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 产品码
     */
    private String productCode;

    private String clientid;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 扩展参数
     */
    private String extension;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
