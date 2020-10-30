package cn.iwuliao.trade.api.request;

import cn.iwuliao.base.request.AbstractRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author elijah
 */
public class TradeQueryRequest extends AbstractRequest {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String voucherNo;

}
