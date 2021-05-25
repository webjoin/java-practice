package com.iquickmove.base.response;

import com.iquickmove.base.enums.ApplyState;
import com.iquickmove.base.enums.ReturnCode;

import java.io.Serializable;

/**
 * @author elijah
 */
public class CommonResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected String code;
    /**
     * 错误信息
     */
    protected String message;
    /**
     * 申请状态
     */
    protected ApplyState applyState= ApplyState.SUCCESS; //默认成功

    /**
     * 统一返回码
     */
    private String unityReturnCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public CommonResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public ApplyState getApplyState() {
        return applyState;
    }

    public void setApplyState(ApplyState applyState) {
        this.applyState = applyState;
    }

    public String getUnityReturnCode() {
        return unityReturnCode;
    }

    public void setUnityReturnCode(String unityReturnCode) {
        this.unityReturnCode = unityReturnCode;
    }

    public CommonResponse succ() {
        this.applyState = ApplyState.SUCCESS;
        return this;
    }

    public CommonResponse error() {
        this.applyState = ApplyState.ERROR;
        return this;
    }

    public CommonResponse fail() {
        this.applyState = ApplyState.FAIL;
        return this;
    }

    public CommonResponse fail(String message) {
        this.message = message;
        this.code = ReturnCode.SYSTEM_ERROR.getCode();
        this.applyState = ApplyState.FAIL;
        return this;
    }

    @Override
    public String toString() {
        return "CommonResponse{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", applyState="
            + applyState + ", unityReturnCode='" + unityReturnCode + '\'' + '}';
    }
}
