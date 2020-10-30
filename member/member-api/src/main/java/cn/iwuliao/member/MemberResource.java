package cn.iwuliao.member;

import cn.iwuliao.base.response.ObjectQueryResponse;
import cn.iwuliao.member.domain.MemberInfo;
import cn.iwuliao.member.request.MemeberQueryRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MemberResource {

    @PostMapping("/queryMember")
    ObjectQueryResponse<MemberInfo> queryMember(@RequestBody MemeberQueryRequest request);

}
