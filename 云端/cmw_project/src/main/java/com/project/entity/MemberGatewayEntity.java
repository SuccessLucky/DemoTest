package com.project.entity;

import com.project.common.entity.DomainObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

/**
 * Created by xieyanhao on 16/9/28.
 */
@Entity
@Table(name = "member_gateway_tbl")
public class MemberGatewayEntity extends DomainObject {

    private MemberUserEntity memberUser;
    private GatewayEntity gateway;
    private int memberType;
    private String permissions; // 权限

    public static int MEMBER_TYPY_ADMIN = 1; // 管理员
    public static int MEMBER_TYPY_FAMILY = 2; // 家人

    public static int PERMISSION_SCENE = 1;
    public static int PERMISSION_DEVICE = 2;

    @ManyToOne
    @JoinColumn(name = "member_user_id", nullable = true)
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    public MemberUserEntity getMemberUser() {
        return memberUser;
    }

    public void setMemberUser(MemberUserEntity memberUser) {
        this.memberUser = memberUser;
    }

    @ManyToOne
    @JoinColumn(name = "gateway_id", nullable = true)
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    public GatewayEntity getGateway() {
        return gateway;
    }

    public void setGateway(GatewayEntity gateway) {
        this.gateway = gateway;
    }

    @Column(name = "member_type", columnDefinition = "tinyint default 0")
    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    @Column(name = "permissions", length=2000)
    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
