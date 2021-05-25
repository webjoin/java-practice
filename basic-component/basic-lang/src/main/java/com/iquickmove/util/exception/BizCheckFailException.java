package com.iquickmove.util.exception;

import com.iquickmove.base.enums.ReturnCode;

public class BizCheckFailException extends FailException {

    private static final long serialVersionUID = 1L;

    public BizCheckFailException(String message) {
        super(ReturnCode.BIZ_CHECK_FAIL, message);
    }
}