package cn.iwuliao.trade.busiservice.repository.impl;

import cn.iwuliao.trade.busiservice.mapper.dba.TradeOrderMapper;
import org.springframework.stereotype.Component;

import cn.iwuliao.trade.busiservice.repository.TradeOrderRepository;
import cn.iwuliao.trade.entity.TradeOrderEntity;

import javax.annotation.Resource;

@Component
public class TradeOrderRepositoryImpl implements TradeOrderRepository {

    @Resource
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public void save(TradeOrderEntity entity) {
        tradeOrderMapper.insertSelective(entity);
    }

    @Override
    public TradeOrderEntity loadByVoucherNo(Long tradeId) {
        return tradeOrderMapper.selectByPrimaryKey(tradeId);
    }
}
