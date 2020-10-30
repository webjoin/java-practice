package cn.iwuliao.trade.test.auto.ordercreation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.iwuliao.base.enums.ReturnCode;
import jdk.nashorn.internal.ir.ReturnNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import cn.iwuliao.trade.api.enums.TradeStatus;
import cn.iwuliao.trade.api.request.TradeCreateRequest;
import cn.iwuliao.trade.test.AutoTest;
import cn.iwuliao.trade.test.builder.TradeCreateRequestBuilder;
import cn.iwuliao.trade.test.mock.member.MemberResourceMocker;
import cn.iwuliao.util.JsonUtil;

import java.time.LocalDateTime;

@DisplayName("交易下单校验测试")
class TradeCreateValidateTest extends AutoTest {

    @Autowired
    private MockMvc mockMvc;

    TradeCreateRequest request;
    MvcResult mvcResult;

    @Test
    @Order(1)
    @DisplayName("交易下单校验基本参数")
    void test_validateBaseParmeterCreateOrder() throws Exception {
        // 构建请求参数
        request = TradeCreateRequestBuilder.template().submitTime(LocalDateTime.now().plusDays(1)).payerId(PAYER_ID_002)
            .putExtension("hi", "Alice").build();
        // 请求rest接口
        memberResourceMocker.mockLock();
        mvcResult = mockMvc.perform(post("/createTradeOrder").content(JsonUtil.toJsonStr(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        // 校验接口返回数据
        commonResponseChecker.create(mvcResult).tradeStatus(null).fail(ReturnCode.INVALID_PARAMETER.getCode(),
            "submitTime:must be a date in the past or in the present", null);
    }

    @Test
    @Order(2)
    @DisplayName("交易下单校验测试")
    void test_validate_createOrder() throws Exception {
        // 构建请求参数
        request = TradeCreateRequestBuilder.template().payerId(PAYER_ID_002).putExtension("hi", "Alice").build();
        // 请求rest接口
        memberResourceMocker.mockLock();
        mvcResult = mockMvc.perform(post("/createTradeOrder").content(JsonUtil.toJsonStr(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        // 校验接口返回数据
        commonResponseChecker.create(mvcResult).tradeStatus(null).fail(ReturnCode.BIZ_CHECK_FAIL.getCode(), "会员状态为冻结",
            null);
    }
}