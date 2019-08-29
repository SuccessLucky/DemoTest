package com.kzksmarthome.common.module.device;

import com.kzksmarthome.common.base.BaseRecyclerItemData;

public class DeviceInfo extends BaseRecyclerItemData {

	public static final int VIEW_TYPE_SEARCH = 2;

	public String nickname;

	public String deviceName;

	public String deviceAddress;

	public String sn;

	/**
	 * 0未验证，1正品，2赝品
	 */
	public int verification;

	public boolean isAdded;

	public String extend_1;

	public String extend_2;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getVerification() {
		return verification;
	}

	public void setVerification(int verification) {
		this.verification = verification;
	}

	public String getExtend_1() {
		return extend_1;
	}

	public void setExtend_1(String extend_1) {
		this.extend_1 = extend_1;
	}

	public String getExtend_2() {
		return extend_2;
	}

	public void setExtend_2(String extend_2) {
		this.extend_2 = extend_2;
	}

	@Override
	public String toString() {
		return "DeviceInfo [nickname=" + nickname + ", deviceName=" + deviceName + ", deviceAddress=" + deviceAddress
				+ ", sn=" + sn + ", verification=" + verification + ", isAdded=" + isAdded + ", extend_1=" + extend_1
				+ ", extend_2=" + extend_2 + "]";
	}

}
