package cn.iwuliao.member.request;

import cn.iwuliao.base.request.AbstractRequest;

/**
 * @author elijah
 */
public class MemeberQueryRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;

    private String memberId;

    public MemeberQueryRequest(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
