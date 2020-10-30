package cn.iwuliao.util.exception;

import cn.iwuliao.base.enums.ReturnCode;

public class BizCheckFailException extends FailException {

    private static final long serialVersionUID = 1L;

    public BizCheckFailException(String message) {
        super(ReturnCode.BIZ_CHECK_FAIL, message);
    }
}