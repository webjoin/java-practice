package cn.iwuliao.spring;

import cn.iwuliao.base.enums.ApplyState;
import cn.iwuliao.base.enums.ReturnCode;
import cn.iwuliao.base.response.CommonResponse;
import cn.iwuliao.util.exception.ErrorException;
import cn.iwuliao.util.exception.FailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Elijah
 */
@ControllerAdvice
public class ThrowableHandler {

    public static Logger logger = LoggerFactory.getLogger(ThrowableHandler.class);

    /**
     * 异常捕捉处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public CommonResponse errorHandler(Throwable e, HttpServletRequest request) {

        if (e instanceof FailException) {
            CommonResponse response = new CommonResponse();
            FailException e1 = (FailException)e;
            response.setCode(e1.getCode());
            response.setMessage(e1.getMessage());
            response.setApplyState(ApplyState.FAIL);
            response.setUnityReturnCode(e1.getUnityResultCode());
            logger.warn("{}", response);
            return response;
        } else if (e instanceof ErrorException) {
            CommonResponse response = new CommonResponse();
            ErrorException e1 = (ErrorException)e;
            response.setCode(e1.getCode());
            response.setMessage(e1.getMessage());
            response.setApplyState(ApplyState.ERROR);
            logger.error(response + "", e);
            return response;
        } else if (e instanceof MethodArgumentNotValidException) {
            CommonResponse response = new CommonResponse();
            MethodArgumentNotValidException e1 = (MethodArgumentNotValidException)e;
            response.setCode(ReturnCode.INVALID_PARAMETER.getCode());
            response.setMessage(getMessage(e1));
            response.setApplyState(ApplyState.FAIL);
            logger.warn("参数校验错误：{}", response);
            return response;
        } else {
            CommonResponse response = new CommonResponse();
            ErrorException e1 = new ErrorException(ReturnCode.SYSTEM_ERROR, e);
            response.setCode(e1.getCode());
            response.setMessage(e1.getMessage());
            response.setApplyState(ApplyState.ERROR);
            logger.error(response + "", e);
            return response;
        }

    }

    private String getMessage(MethodArgumentNotValidException e1) {
        List<FieldError> fieldErrors = e1.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage());
            break;
        }
        return sb.toString();
    }
}