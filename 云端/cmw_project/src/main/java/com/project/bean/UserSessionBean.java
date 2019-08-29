package com.project.bean;

import com.project.entity.AuthUserEntity;

public class UserSessionBean {
	int userType;
	private AuthUserEntity user;

	public AuthUserEntity getUser() {
		return user;
	}

	public void setUser(AuthUserEntity user) {
		this.user = user;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	
}
