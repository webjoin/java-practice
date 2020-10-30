package cn.iwuliao.trade.test.checker;

import cn.iwuliao.base.enums.ApplyState;
import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.trade.api.enums.TradeStatus;
import cn.iwuliao.trade.api.response.TradeCreateResponse;
import cn.iwuliao.trade.test.util.MvcResultUitl;
import cn.iwuliao.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

@Service
public class CommonResponseChecker {

    public Checker create(MvcResult mvcResult) {
        return new Checker(mvcResult);
    }

    public class Checker {

        private TradeCreateResponse createResponse;

        public Checker(MvcResult mvcResult) {
            createResponse = MvcResultUitl.toResponse(mvcResult, new TypeReference<TradeCreateResponse>() {});
        }

        public Checker success() {
            Assertions.assertEquals(ApplyState.SUCCESS, createResponse.getApplyState());
            Assertions.assertNull(createResponse.getCode());
            Assertions.assertNull(createResponse.getMessage());
            Assertions.assertNull(createResponse.getUnityReturnCode());
            return this;
        }

        public Checker fail(String code, String message, String unityReturnCode) {
            Assertions.assertEquals(ApplyState.FAIL, createResponse.getApplyState());
            Assertions.assertEquals(code, createResponse.getCode());
            Assertions.assertEquals(message, createResponse.getMessage());
            Assertions.assertEquals(unityReturnCode, createResponse.getUnityReturnCode());
            return this;
        }

        public Checker error() {
            Assertions.assertEquals(ApplyState.ERROR, createResponse.getApplyState());
            return this;
        }

        public Checker tradeStatus(TradeStatus tradeStatus) {
            Assertions.assertEquals(tradeStatus, createResponse.getTradeStatus());
            return this;
        }

        public Checker notNullTradeId() {
            Assertions.assertNotNull(createResponse.getTradeId());
            return this;
        }
    }
}
