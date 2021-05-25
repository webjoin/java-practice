package com.iquickmove.util.exception;

import com.iquickmove.base.enums.ReturnCode;

/**
 * @author ty
 */
public class InvalidParameterException extends FailException {

    private static final long serialVersionUID = 1L;

    public InvalidParameterException(String message) {
        super(ReturnCode.BIZ_CHECK_FAIL, message);
    }
}