package cn.iwuliao.base.response;

import cn.iwuliao.base.enums.ApplyState;

import java.io.Serializable;

/**
 * @author elijah
 */
public class CommonResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 申请状态
     */
    private ApplyState applyState;

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

    public void setMessage(String message) {
        this.message = message;
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

    @Override
    public String toString() {
        return "CommonResponse{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", applyState="
            + applyState + ", unityReturnCode='" + unityReturnCode + '\'' + '}';
    }
}
