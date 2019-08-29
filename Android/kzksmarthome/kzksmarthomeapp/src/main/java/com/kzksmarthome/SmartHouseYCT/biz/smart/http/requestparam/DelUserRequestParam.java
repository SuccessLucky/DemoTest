package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: DelUserRequestParam
 * @Description: 删除家庭成员请求参数
 * @author laixj
 * @date 2016/10/10 20:26
 * @version V1.0
 */
public class DelUserRequestParam {

    /**
     * member_id : 2
     */

    int member_id;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    @Override
    public String toString() {
        return "DelUserRequestParam{" +
                "member_id=" + member_id +
                '}';
    }
}
