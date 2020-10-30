package cn.iwuliao.trade.api;

import cn.iwuliao.base.response.ObjectQueryResponse;
import cn.iwuliao.base.response.PageResponse;
import cn.iwuliao.trade.api.response.TradeOrderInfo;
import cn.iwuliao.trade.api.request.TradeQueryListRequest;
import cn.iwuliao.trade.api.request.TradeQueryRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface TradeQueryFeign {


    @PostMapping("/queryTradeOrder")
    ObjectQueryResponse<TradeOrderInfo> queryTradeOrder(@RequestBody TradeQueryRequest tradeQueryRequest);

    @PostMapping("/queryTradeOrderList")
    PageResponse<TradeOrderInfo> queryTradeOrderList(@RequestBody TradeQueryListRequest tradeQueryListRequest);
}
