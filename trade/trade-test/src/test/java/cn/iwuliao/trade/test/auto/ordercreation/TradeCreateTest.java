package cn.iwuliao.trade.test.auto.ordercreation;

import cn.iwuliao.trade.api.enums.TradeStatus;
import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.test.AutoTest;
import cn.iwuliao.trade.test.builder.TradeCreateRequestBuilder;
import cn.iwuliao.trade.test.mock.member.MemberResourceMocker;
import cn.iwuliao.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("交易类测试")
class TradeCreateTest extends AutoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberResourceMocker memberResourceMocker;

    TradeCreateRequest request;
    MvcResult mvcResult;

    @Test
    @Order(1)
    @DisplayName("交易下单测试")
    void test_createOrder() throws Exception {
        // 构建请求参数
        request = TradeCreateRequestBuilder.template().payerId(PAYER_ID_002).putExtension("hi", "Alice").build();
        // 请求rest接口
        memberResourceMocker.mockSucc();
        mvcResult = mockMvc.perform(post("/createTradeOrder").content(JsonUtil.toJsonStr(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        // 校验接口返回数据
        commonResponseChecker.create(mvcResult).tradeStatus(TradeStatus.INIT).notNullTradeId().success();
        // 校验表数据
        tradeOrderChecker.create(request, mvcResult).tradeStatus(TradeStatus.INIT).payerId(PAYER_ID_002).extension("hi", "Alice");
    }

    @Test
    @Order(2)
    @DisplayName("Hi测试")
    void test_hi() throws Exception {
        mockMvc.perform(get("/hi/hi")).andDo(print()).andExpect(status().isOk())
            .andExpect(content().string("hia:1<-->hib:1"));
    }
}