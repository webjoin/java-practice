package cn.iwuliao.trade.api.response;

import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.trade.api.enums.TradeStatus;

public class TradeCreateResponse extends CommonResponse {
    private static final long serialVersionUID = 1L;

    private TradeStatus tradeStatus;
    private Long tradeId;

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }
}
