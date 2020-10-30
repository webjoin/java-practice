package cn.iwuliao.trade.api.request;

import cn.iwuliao.base.request.PageRequest;

import javax.validation.constraints.NotNull;

/**
 * @author elijah
 */
public class TradeQueryListRequest extends PageRequest {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String voucherNo;

}
