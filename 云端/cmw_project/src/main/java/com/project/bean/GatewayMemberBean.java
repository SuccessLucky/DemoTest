package com.project.bean;

/**
 * Created by xieyanhao on 16/9/29.
 */
public class GatewayMemberBean {

    private int member_id;
    private String account;
    private String member_name;
    private int member_type; // 1 网关管理员, 2 网关成员
    private String image; // 1 网关管理员, 2 网关成员

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
