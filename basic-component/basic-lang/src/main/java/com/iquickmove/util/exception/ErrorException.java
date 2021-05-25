package com.iquickmove.util.exception;

import com.iquickmove.base.enums.CodeEnum;
import com.iquickmove.base.enums.CodeMessage;
import com.iquickmove.base.enums.ReturnCode;

/**
 * @author elijah
 */
public class ErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 返回码 */
    private String code;

    public ErrorException(String message) {
        this(ReturnCode.SYSTEM_ERROR, message);
    }

    public ErrorException(CodeEnum code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public ErrorException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorException(Throwable e) {
        this(ReturnCode.SYSTEM_ERROR, e);
    }

    public ErrorException(CodeMessage codeMessage, Throwable e) {
        super(codeMessage.getMessage(), e);
        this.code = codeMessage.getCode();
    }

    public ErrorException(String message, Throwable e) {
        this(ReturnCode.SYSTEM_ERROR, message, e);
    }

    public ErrorException(CodeMessage codeMessage, String message, Throwable e) {
        super(message, e);
        this.code = codeMessage.getCode();
    }

    public String getCode() {
        return code;
    }

}
