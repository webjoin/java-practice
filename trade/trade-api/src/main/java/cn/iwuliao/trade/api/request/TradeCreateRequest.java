package cn.iwuliao.trade.api.request;

import cn.iwuliao.base.request.AbstractRequest;
import cn.iwuliao.trade.api.enums.TradeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author elijah
 */
public class TradeCreateRequest extends AbstractRequest {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long voucherNo;

    private String payerId;

    @NotNull
    @Length(max = 16)
    private String productCode;

    private TradeType tradeType;

    @NotNull
    @Positive
    private BigDecimal tradeAmount;

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    public Long getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(Long voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

}
