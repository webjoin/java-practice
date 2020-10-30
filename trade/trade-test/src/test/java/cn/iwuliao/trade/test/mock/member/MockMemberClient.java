package cn.iwuliao.trade.test.mock.member;

import cn.iwuliao.base.response.ObjectQueryResponse;
import cn.iwuliao.member.MemberResource;
import cn.iwuliao.member.domain.MemberInfo;
import cn.iwuliao.member.request.MemeberQueryRequest;
import cn.iwuliao.trade.ext.integration.MemberClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Primary
@Service
public class MockMemberClient implements MemberClient {

    @Resource
    private MemberResourceMocker memberResourceMocker;

    @Override
    public ObjectQueryResponse<MemberInfo> queryMember(MemeberQueryRequest memberId) {
        return memberResourceMocker.getObjectQueryResponse();
    }
}
