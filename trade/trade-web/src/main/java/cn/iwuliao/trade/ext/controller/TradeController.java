package cn.iwuliao.trade.ext.controller;

import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.trade.api.TradeResource;
import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.api.response.TradeCreateResponse;
import cn.iwuliao.trade.ext.processor.TradeProcessor;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author elijah
 */
@RestController
@AllArgsConstructor
public class TradeController implements TradeResource {

    private final TradeProcessor tradeProcessor;

    @Override
    public TradeCreateResponse createTradeOrder(@RequestBody @Validated TradeCreateRequest request) {
        return tradeProcessor.createOrder(request);
    }

}
