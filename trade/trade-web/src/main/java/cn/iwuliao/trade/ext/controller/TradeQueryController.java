package cn.iwuliao.trade.ext.controller;

import cn.iwuliao.base.response.ObjectQueryResponse;
import cn.iwuliao.base.response.PageResponse;
import cn.iwuliao.trade.api.TradeQueryFeign;
import cn.iwuliao.trade.api.request.TradeQueryListRequest;
import cn.iwuliao.trade.api.request.TradeQueryRequest;
import cn.iwuliao.trade.api.response.TradeOrderInfo;
import cn.iwuliao.trade.ext.processor.TradeProcessor;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author elijah
 */
@RestController
@AllArgsConstructor
public class TradeQueryController implements TradeQueryFeign {

    private TradeProcessor tradeProcessor;

    @Override
    @PostMapping("/queryTradeOrder")
    public ObjectQueryResponse<TradeOrderInfo> queryTradeOrder(@RequestBody TradeQueryRequest tradeQueryRequest) {
        return null;
    }

    @Override
    @PostMapping("/queryTradeOrderList")
    public PageResponse<TradeOrderInfo> queryTradeOrderList(@RequestBody TradeQueryListRequest tradeQueryListRequest) {
        return null;
    }

}
