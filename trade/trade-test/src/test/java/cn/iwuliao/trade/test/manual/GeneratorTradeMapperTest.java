package cn.iwuliao.trade.test.manual;

import org.junit.jupiter.api.Test;

import cn.iwuliao.mapper.generator.AbstractMapperTest;

public class GeneratorTradeMapperTest extends AbstractMapperTest {

    @Test
    public void trade_order() {
        // 表映射对象名称
        generateClass("trade_order");
    }
}
