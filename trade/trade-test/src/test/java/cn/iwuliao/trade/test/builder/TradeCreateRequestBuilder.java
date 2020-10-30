package cn.iwuliao.trade.test.builder;

import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.test.constant.TestConstant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TradeCreateRequestBuilder implements TestConstant {
    private TradeCreateRequest request;

    public TradeCreateRequestBuilder() {
        this.request = new TradeCreateRequest();
    }

    public static TradeCreateRequestBuilder template() {
        return new TradeCreateRequestBuilder().clientId(CLIENT_ID).submitTime(LocalDateTime.now())
            .tradeAmount(TRADE_AMOUNT).noPayer().voucherNo(Long.parseLong("901" + System.currentTimeMillis()))
            .productCode(PRODUCT_CODE);
    }

    private TradeCreateRequestBuilder noPayer() {
        payerId(null);
        return this;
    }

    public TradeCreateRequestBuilder voucherNo(Long voucherNo) {
        request.setVoucherNo(voucherNo);
        return this;
    }

    public TradeCreateRequestBuilder payerId(String payerId) {
        request.setPayerId(payerId);
        return this;
    }

    public TradeCreateRequestBuilder productCode(String productCode) {
        request.setProductCode(productCode);
        return this;
    }

    public TradeCreateRequestBuilder clientId(String voucherNo) {
        request.setClientId(voucherNo);
        return this;
    }

    public TradeCreateRequestBuilder tradeAmount(BigDecimal tradeAmount) {
        request.setTradeAmount(tradeAmount);
        return this;
    }

    public TradeCreateRequestBuilder submitTime(LocalDateTime submitTime) {
        request.setSubmitTime(submitTime);
        return this;
    }


    public TradeCreateRequestBuilder putExtension(String key, String value) {
        if (request.getExtention() == null) {
            request.setExtention(new HashMap<>());
        }
        request.getExtention().put(key, value);
        return this;
    }

    public TradeCreateRequest build() {
        return request;
    }

}
