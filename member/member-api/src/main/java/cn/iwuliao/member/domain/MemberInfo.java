package cn.iwuliao.member.domain;

import java.time.LocalDate;

public class MemberInfo {

    private String memberId;
    private String status;
    private String mamberName;
    private LocalDate validTime;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMamberName() {
        return mamberName;
    }

    public void setMamberName(String mamberName) {
        this.mamberName = mamberName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getValidTime() {
        return validTime;
    }

    public void setValidTime(LocalDate validTime) {
        this.validTime = validTime;
    }
}
