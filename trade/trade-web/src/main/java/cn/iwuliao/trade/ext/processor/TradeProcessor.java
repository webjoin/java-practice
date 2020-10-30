package cn.iwuliao.trade.ext.processor;

import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.base.response.ObjectQueryResponse;
import cn.iwuliao.member.MemberState;
import cn.iwuliao.member.domain.MemberInfo;
import cn.iwuliao.member.request.MemeberQueryRequest;
import cn.iwuliao.trade.api.enums.TradeStatus;
import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.api.response.TradeCreateResponse;
import cn.iwuliao.trade.busiservice.service.TradeService;
import cn.iwuliao.trade.domain.TradeContext;
import cn.iwuliao.trade.entity.TradeOrderEntity;
import cn.iwuliao.trade.ext.integration.MemberClient;
import cn.iwuliao.util.exception.BizCheckFailException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TradeProcessor {

    private MemberClient memberClient;
    private TradeService tradeService;

    public TradeCreateResponse createOrder(TradeCreateRequest tradeCreateRequest) {
        // do something
        validateBusiness(tradeCreateRequest);
        // do something 01
        // do something 02
        // do something 03
        // do something 04
        // 构建订单上下文
        TradeOrderEntity orderEntity = buildOrder(tradeCreateRequest);
        TradeContext tradeContext = new TradeContext(orderEntity, null, null, null);
        tradeService.saveeOrder(tradeContext);
        return buildResponse(orderEntity);
    }

    private TradeCreateResponse buildResponse(TradeOrderEntity orderEntity) {
        TradeCreateResponse response = new TradeCreateResponse();
        response.succ();
        response.setTradeId(orderEntity.getTradeId());
        response.setTradeStatus(orderEntity.getTradeStatus());
        return response;
    }

    private void validateBusiness(TradeCreateRequest tradeRequst) {
        // 幂等校验 。。。
        // call member service
        ObjectQueryResponse<MemberInfo> queryResponse =
            memberClient.queryMember(new MemeberQueryRequest(tradeRequst.getPayerId()));
        if (queryResponse.getData() == null || !MemberState.OK.getCode().equals(queryResponse.getData().getStatus())) {
            throw new BizCheckFailException("会员状态为冻结");
        }
    }

    /**
     * 构建付款订单
     */
    private TradeOrderEntity buildOrder(TradeCreateRequest request) {
        TradeOrderEntity entity = new TradeOrderEntity();
        entity.setClientId(request.getClientId());
        entity.setCreateTime(LocalDateTime.now());
        entity.setExtension(request.getExtention());
        entity.setTradeType(request.getTradeType());
        entity.setPayerId(request.getPayerId());
        entity.setTradeStatus(TradeStatus.INIT);
        entity.setProductCode(request.getProductCode());
        entity.setVoucherNo(request.getVoucherNo());
        entity.setTradeAmount(request.getTradeAmount());
        entity.setSubmitTime(request.getSubmitTime());
        return entity;
    }

}
