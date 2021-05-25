package com.iquickmove.base.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author pinghui
 * @Description
 * @Date Created in 2020-11-08 13:12
 */
@Getter
@Setter
public class WxMiniJsCodeRequest {
    private String jsCode;

    /**
     * 1 医师
     * 2 项目经理
     * 3 机构中心
     */
    private int sourceType = 1;

    private int memberId;

    /**
     * 项目经理cdc 用户 传2
     */
    private int userType = 1;
}
