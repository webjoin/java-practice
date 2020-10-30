package cn.iwuliao.trade.test;


import cn.iwuliao.trade.BaseTradeTest;
import cn.iwuliao.trade.test.checker.CommonResponseChecker;
import cn.iwuliao.trade.test.checker.TradeOrderChecker;
import cn.iwuliao.trade.test.constant.TestConstant;
import cn.iwuliao.trade.test.mock.member.MemberResourceMocker;
import org.junit.jupiter.api.BeforeEach;

import javax.annotation.Resource;

public class AutoTest extends BaseTradeTest implements TestConstant {

    @Resource
    protected TradeOrderChecker tradeOrderChecker;
    @Resource
    protected CommonResponseChecker commonResponseChecker;
    @Resource
    protected MemberResourceMocker memberResourceMocker;

    @BeforeEach
    public void init(){
        memberResourceMocker.mockLock();
    }
}
