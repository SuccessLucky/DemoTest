package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @Title: UserInfo
 * @Description: 用户信息Bean
 * @author laixj
 * @date 2016/9/14 7:14
 * @version V1.0
 */
public class UserInfo extends BaseModel implements Parcelable {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 用户角色名称
     */
    private short roleId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPwd;
    /**
     * 联系电话
     */
    private String userPhone;

    public UserInfo(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getRoleId() {
        return roleId;
    }

    public void setRoleId(short roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;
        if (id.compareTo(userInfo.getId()) != 0 || roleId != userInfo.getRoleId()
                || userName.compareTo(userInfo.getUserName()) != 0) {
            return false;
        }
        return true;
    }

    public UserInfo(Integer id, short roleId, String userName, String userPwd, String userPhone) {
        this.id = id;
        this.roleId = roleId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userPhone = userPhone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.roleId);
        dest.writeString(this.userName);
        dest.writeString(this.userPwd);
        dest.writeString(this.userPhone);
    }

    protected UserInfo(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.roleId = (short) in.readInt();
        this.userName = in.readString();
        this.userPwd = in.readString();
        this.userPhone = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
