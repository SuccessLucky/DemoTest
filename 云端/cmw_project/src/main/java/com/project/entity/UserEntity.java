package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Table(name = "user_tbl", uniqueConstraints = { @UniqueConstraint(columnNames = { "use_uid" }) })
public class UserEntity extends DomainObject {
	private String userUid;
	private int client;
	private String clientChannel;
	private Date createTime = new Date();
	private int accountStatus; //0.默认; 1.激活; 2.冻结; 3.注销
	private String email;
	private String mobile;
	private String nickname = "";
	private String image;
	private int smsCount = 10;

	@Column(name = "use_uid", nullable = false)
	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	@Column(name = "client", nullable = true, columnDefinition = "tinyint default 0")
	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	@Column(name = "client_channel", nullable = true)
	public String getClientChannel() {
		return clientChannel;
	}

	public void setClientChannel(String clientChannel) {
		this.clientChannel = clientChannel;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "account_status", nullable = false, columnDefinition = "tinyint default 0")
	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "nickname")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "image")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "sms_count", columnDefinition = "int default 10")
	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}
}
