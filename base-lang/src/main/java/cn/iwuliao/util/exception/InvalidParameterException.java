package cn.iwuliao.util.exception;

import cn.iwuliao.base.enums.ReturnCode;

/**
 * @author elijah
 */
public class InvalidParameterException extends FailException {

    private static final long serialVersionUID = 1L;

    public InvalidParameterException(String message) {
        super(ReturnCode.BIZ_CHECK_FAIL, message);
    }
}