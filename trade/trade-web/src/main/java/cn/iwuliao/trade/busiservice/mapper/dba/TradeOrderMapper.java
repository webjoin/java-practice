package cn.iwuliao.trade.busiservice.mapper.dba;

import cn.iwuliao.trade.entity.TradeOrderEntity;

/**
* Created by Mybatis Generator on 2020/10/30
* 
*/
public interface TradeOrderMapper {
    int deleteByPrimaryKey(Long tradeId);

    int insert(TradeOrderEntity record);

    int insertSelective(TradeOrderEntity record);

    TradeOrderEntity selectByPrimaryKey(Long tradeId);

    int updateByPrimaryKeySelective(TradeOrderEntity record);

    int updateByPrimaryKey(TradeOrderEntity record);
}