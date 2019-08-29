package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "session_status_tbl")
public class SessionStatusEntity extends DomainObject {
    private String sessionToken;
    private Date sessionExpireDate;
    private Integer authUserId;
    private AuthUserEntity authUserEntity;
    private Integer type;

    @Column(name = "access_type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "session_token")
    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Column(name = "session_expire_date")
    public Date getSessionExpireDate() {
        return sessionExpireDate;
    }

    public void setSessionExpireDate(Date sessionExpireDate) {
        this.sessionExpireDate = sessionExpireDate;
    }

    @Transient
    public AuthUserEntity getAuthUserEntity() {
        return authUserEntity;
    }

    public void setAuthUserEntity(AuthUserEntity authUserEntity) {
        this.authUserEntity = authUserEntity;
    }

    @Column(name = "auth_user_id")
    public Integer getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Integer authUserId) {
        this.authUserId = authUserId;
    }
}
