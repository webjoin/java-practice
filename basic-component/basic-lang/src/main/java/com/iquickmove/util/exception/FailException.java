package com.iquickmove.util.exception;

import com.iquickmove.base.enums.CodeEnum;
import com.iquickmove.base.enums.CodeMessage;

public class FailException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /**
     * 返回码
     */
    final String code;

    /**
     * 同意返回码
     */
    final String unityResultCode;

    public FailException(CodeMessage codeMessage) {
        super(codeMessage.getMessage());
        this.code = codeMessage.getCode();
        this.unityResultCode = null;
    }


    public FailException(CodeEnum code, String message) {
        super(message);
        this.code = code.getCode();
        this.unityResultCode = null;
    }

    public FailException(String code, String message) {
        super(message);
        this.code = code;
        this.unityResultCode = null;
    }

    public FailException(CodeEnum code, CodeMessage unityResultCode) {
        super(unityResultCode.getMessage());
        this.code = code != null ? code.getCode() : null;
        this.unityResultCode = unityResultCode.getCode();
    }

    public FailException(CodeEnum code, CodeEnum unityResultCode, String message) {
        super(message);
        this.code = code != null ? code.getCode() : null;
        this.unityResultCode = unityResultCode.getCode();
    }

    public FailException(String code, String unityResultCode, String message) {
        super(message);
        this.code = code;
        this.unityResultCode = unityResultCode;
    }

    public String getCode() {
        return code;
    }

    public String getUnityResultCode() {
        return unityResultCode;
    }
}