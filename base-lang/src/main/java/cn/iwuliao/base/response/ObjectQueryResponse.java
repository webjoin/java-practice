package cn.iwuliao.base.response;

/**
 * @author elijah
 */
public class ObjectQueryResponse<T> extends CommonResponse {

    private static final long serialVersionUID = 1L;

    public ObjectQueryResponse<T> succ(T data) {
        setData(data);
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
