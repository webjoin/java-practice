package com.iquickmove.base.response;

import com.iquickmove.base.enums.ApplyState;
import com.iquickmove.base.enums.CodeEnum;
import com.iquickmove.base.enums.ReturnCode;

/**
 * @author elijah
 */
public class DataResult<T> extends CommonResponse {

    private static final long serialVersionUID = 1L;

    public DataResult<T> succ(T data) {
        this.code = "0";
        this.message = "请求成功";
        this.applyState = ApplyState.SUCCESS;
        setData(data);
        return this;
    }

    @Override
    public DataResult<T> fail(String message) {
        this.message = message;
        this.code = ReturnCode.SYSTEM_ERROR.getCode();
        this.applyState = ApplyState.FAIL;
        return this;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
