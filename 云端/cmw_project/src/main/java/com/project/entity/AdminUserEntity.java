package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin_user_tbl")
public class AdminUserEntity extends AuthUserEntity {

	private int adminType = 0;
	private String permission;

	@Column(name = "permission", nullable = true)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Column(name = "admin_type", columnDefinition = "int default 1")
	public int getAdminType() {
		return adminType;
	}

	public void setAdminType(int adminType) {
		this.adminType = adminType;
	}
}
