package com.project.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "member_user_tbl")
public class MemberUserEntity extends AuthUserEntity implements Serializable {
    private UserEntity userEntity;
    private int userChannel;

    public static int USER_CHANNEL_NORMAL = 0;
    public static int USER_CHANNEL_SYSTEM = 1;
    public static int USER_CHANNEL_SYSTEM_MANUAL = 2;

    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Column(name = "user_channel", columnDefinition = "int default 0")
    public int getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(int userChannel) {
        this.userChannel = userChannel;
    }
}
