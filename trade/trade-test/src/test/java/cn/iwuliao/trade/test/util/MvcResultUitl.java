package cn.iwuliao.trade.test.util;

import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class MvcResultUitl {
    public static <T> T toResponse(MvcResult mvcResult,TypeReference<T> typeReference) {
        try {
            return JsonUtil.json2Bean(mvcResult.getResponse().getContentAsString(), typeReference);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("解析响应异常");
        }

    }
}
