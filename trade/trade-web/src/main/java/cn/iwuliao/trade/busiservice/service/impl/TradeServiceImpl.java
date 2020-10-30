package cn.iwuliao.trade.busiservice.service.impl;

import cn.iwuliao.trade.busiservice.TradeConstant;
import cn.iwuliao.trade.busiservice.repository.TradeOrderRepository;
import cn.iwuliao.trade.busiservice.service.TradeService;
import cn.iwuliao.trade.domain.TradeContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author elijah
 */
@Service
public class TradeServiceImpl implements TradeService {

    @Resource(name = TradeConstant.TRANSACTION_TEMPLATE_DBA)
    private TransactionTemplate transactionTemplate;

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Override
    public void saveeOrder(TradeContext tradeContext) {
        // call...
        // save order
        transactionTemplate.execute(new SaveOrderTransaction(tradeContext));
    }

    /**
     * 复杂的保存可以这样写 如果简单一条保存则直接tradeOrderRepository.save 不需要使用事务
     */
    class SaveOrderTransaction extends TransactionCallbackWithoutResult {
        private final TradeContext tradeContext;

        SaveOrderTransaction(TradeContext tradeContext) {
            this.tradeContext = tradeContext;
        }

        @Override
        protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
            tradeOrderRepository.save(tradeContext.getOrderEntity());
            // ... save other object
        }
    }
}
