/**
 * 
 */
package com.kzksmarthome.common.module.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 用户信息实体类，支持跨进程传递
 * 
 * @author guoliexi
 * @createDate 2015-1-12
 * 
 */
public class UserInfo implements Parcelable {

    public int ID;
    public String uuid = null;
    public String mobile;
    public String email;
    public String token;
    public int sexual;// 性别：男(1)、女(0)
    public String nickname;
    public String portrait;
    public int age;
    public int platform;
    public long expire_time;
    public String address;
    public String extend_1;
    public String extend_2;
    public String password;
    public String gateway;
    public int role;

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "ID=" + ID +
                ", uuid='" + uuid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", sexual=" + sexual +
                ", nickname='" + nickname + '\'' +
                ", portrait='" + portrait + '\'' +
                ", age=" + age +
                ", platform=" + platform +
                ", expire_time=" + expire_time +
                ", address='" + address + '\'' +
                ", extend_1='" + extend_1 + '\'' +
                ", extend_2='" + extend_2 + '\'' +
                ", password='" + password + '\'' +
                ", gateway='" + gateway + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.uuid);
        dest.writeString(this.mobile);
        dest.writeString(this.email);
        dest.writeString(this.token);
        dest.writeInt(this.sexual);
        dest.writeString(this.nickname);
        dest.writeString(this.portrait);
        dest.writeInt(this.age);
        dest.writeInt(this.platform);
        dest.writeLong(this.expire_time);
        dest.writeString(this.address);
        dest.writeString(this.extend_1);
        dest.writeString(this.extend_2);
        dest.writeString(this.password);
        dest.writeString(this.gateway);
        dest.writeInt(this.role);
    }

    protected UserInfo(Parcel in) {
        this.ID = in.readInt();
        this.uuid = in.readString();
        this.mobile = in.readString();
        this.email = in.readString();
        this.token = in.readString();
        this.sexual = in.readInt();
        this.nickname = in.readString();
        this.portrait = in.readString();
        this.age = in.readInt();
        this.platform = in.readInt();
        this.expire_time = in.readLong();
        this.address = in.readString();
        this.extend_1 = in.readString();
        this.extend_2 = in.readString();
        this.password = in.readString();
        this.gateway = in.readString();
        this.role = in.readInt();
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
