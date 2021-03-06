package cn.iwuliao.util.exception;

import cn.iwuliao.base.enums.ReturnCode;

/**
 * @author tangyu
 * @since 2019-04-08 23:06
 */
public class JsonParseException extends ErrorException {

    private static final long serialVersionUID = 1L;

    public JsonParseException(String message) {
        super(ReturnCode.SYSTEM_ERROR, message);
    }
}
