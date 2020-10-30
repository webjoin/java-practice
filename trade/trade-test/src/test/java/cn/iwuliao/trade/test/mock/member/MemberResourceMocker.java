package cn.iwuliao.trade.test.mock.member;

import cn.iwuliao.base.enums.ReturnCode;
import cn.iwuliao.base.response.ObjectQueryResponse;
import cn.iwuliao.member.MemberState;
import cn.iwuliao.member.domain.MemberInfo;
import cn.iwuliao.trade.test.constant.TestConstant;
import cn.iwuliao.util.exception.ErrorException;
import org.springframework.stereotype.Service;

@Service
public class MemberResourceMocker implements TestConstant {

    private ObjectQueryResponse<MemberInfo> objectQueryResponse;
    private RuntimeException runtimeException;

    public void mockSucc() {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(PAYER_ID_001);
        memberInfo.setStatus(MemberState.OK.getCode());
        objectQueryResponse = new ObjectQueryResponse<MemberInfo>().succ(memberInfo);
    }

    public void mockLock() {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(PAYER_ID_001);
        memberInfo.setStatus(MemberState.LOCK.getCode());
        objectQueryResponse = new ObjectQueryResponse<MemberInfo>().succ(memberInfo);
    }

    public void mockError() {
        runtimeException = new ErrorException(ReturnCode.SYSTEM_ERROR, "mock系统异常");
    }

    public ObjectQueryResponse<MemberInfo> getObjectQueryResponse() {
        if (runtimeException != null) {
            throw runtimeException;
        }
        return objectQueryResponse;
    }
}
