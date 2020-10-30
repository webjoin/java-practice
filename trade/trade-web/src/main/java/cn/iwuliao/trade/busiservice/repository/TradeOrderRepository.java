package cn.iwuliao.trade.busiservice.repository;

import cn.iwuliao.trade.entity.TradeOrderEntity;

public interface TradeOrderRepository {

    void save(TradeOrderEntity entity);

    TradeOrderEntity loadByVoucherNo(Long voucherNo);
}
