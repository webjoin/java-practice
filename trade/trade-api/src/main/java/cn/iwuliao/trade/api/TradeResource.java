package cn.iwuliao.trade.api;

import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.api.response.TradeCreateResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface TradeResource {

    @PostMapping("/createTradeOrder")
    TradeCreateResponse createTradeOrder(@RequestBody TradeCreateRequest request);
}
