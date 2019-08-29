package com.project.entity;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class AuthUserEntity {
	private String userName;
	private String userPass; // md5 hashed
	private Date lastLoginTime;
	private SessionStatusEntity sessionStatusEntity = new SessionStatusEntity();

	protected Integer id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "user_pass")
	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	@Column(name = "last_login_time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Transient
	public SessionStatusEntity getSessionStatusEntity() {
		return sessionStatusEntity;
	}

	public void setSessionStatusEntity(SessionStatusEntity sessionStatus) {
		this.sessionStatusEntity = sessionStatus;
	}

}
