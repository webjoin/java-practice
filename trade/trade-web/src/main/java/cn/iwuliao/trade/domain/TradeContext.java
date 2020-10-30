package cn.iwuliao.trade.domain;

import cn.iwuliao.trade.entity.TradeOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author elijah
 */
@Setter
@Getter
@AllArgsConstructor
public class TradeContext {
    private TradeOrderEntity orderEntity;
    // ... other object
    private Object args1;
    private Object args2;
    private Object args3;
}
