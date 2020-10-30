package cn.iwuliao.trade.test.checker;

import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.trade.api.enums.TradeStatus;
import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.api.response.TradeCreateResponse;
import cn.iwuliao.trade.busiservice.repository.TradeOrderRepository;
import cn.iwuliao.trade.entity.TradeOrderEntity;
import cn.iwuliao.trade.test.util.MvcResultUitl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.Resource;
import java.time.temporal.ChronoField;

@Service
public class TradeOrderChecker {

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    public Checker create(Long tradeId) {
        return new Checker(tradeId);
    }

    public Checker create(TradeCreateRequest request, MvcResult mvcResult) {
        TradeCreateResponse createResponse =
            MvcResultUitl.toResponse(mvcResult, new TypeReference<TradeCreateResponse>() {});
        return new Checker(createResponse.getTradeId()).exists().request(request);
    }

    public class Checker {

        TradeOrderEntity order;

        public Checker(Long tradeId) {
            order = tradeOrderRepository.loadByVoucherNo(tradeId);
        }

        public Checker exists() {
            Assertions.assertNotNull(order);
            return this;
        }

        public Checker tradeStatus(TradeStatus tradeStatus) {
            Assertions.assertEquals(tradeStatus, order.getTradeStatus());
            return this;
        }

        public Checker extension(String key, String value) {
            if (key != null) {
                Assertions.assertNotNull(order.getExtension());
                Assertions.assertEquals(value, order.getExtension().get(key));
            }
            return this;
        }

        /**
         * 校验 请求数据和db数据是否一致
         */
        public Checker request(TradeCreateRequest request) {
            Assertions.assertEquals(request.getExtention(), order.getExtension());
            Assertions.assertEquals(request.getClientId(), order.getClientId());
            Assertions.assertEquals(request.getPayerId(), order.getPayerId());
            Assertions.assertEquals(request.getTradeAmount(), order.getTradeAmount());
            Assertions.assertEquals(request.getTradeType(), order.getTradeType());
            Assertions.assertEquals(request.getProductCode(), order.getProductCode());
            Assertions.assertEquals(request.getSubmitTime().with(ChronoField.MILLI_OF_SECOND, 0),
                order.getSubmitTime());
            Assertions.assertNotNull(order.getCreateTime());
            return this;
        }

        public Checker payerId(String payerId) {
            Assertions.assertEquals(payerId, order.getPayerId());
            return this;
        }
    }
}
