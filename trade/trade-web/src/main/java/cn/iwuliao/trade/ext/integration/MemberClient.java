package cn.iwuliao.trade.ext.integration;

import org.springframework.cloud.openfeign.FeignClient;

import cn.iwuliao.member.MemberResource;

@FeignClient(name = "member", path = "/", url = "http://www.baidu.com/", primary = false)
public interface MemberClient extends MemberResource {

}
