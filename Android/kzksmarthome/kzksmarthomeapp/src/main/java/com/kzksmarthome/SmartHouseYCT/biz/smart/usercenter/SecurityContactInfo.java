package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: GwInfo
 * @Description: 安防联系人信息Bean
 * @author laixj
 * @date 2016/9/14 7:10
 * @version V1.0
 */
public class SecurityContactInfo extends BaseModel implements Parcelable,Serializable {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 联系号码
     */
    private String phone;

    public SecurityContactInfo(){}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "SecurityContactInfo{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
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

        SecurityContactInfo info = (SecurityContactInfo) o;
        if (id.compareTo(info.getId()) != 0 || phone.compareTo(info.getPhone()) != 0) {
            return false;
        }
        return true;
    }

    public SecurityContactInfo(Integer id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.phone);
    }

    protected SecurityContactInfo(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.phone = in.readString();
    }

    public static final Creator<SecurityContactInfo> CREATOR = new Creator<SecurityContactInfo>() {
        @Override
        public SecurityContactInfo createFromParcel(Parcel source) {
            return new SecurityContactInfo(source);
        }

        @Override
        public SecurityContactInfo[] newArray(int size) {
            return new SecurityContactInfo[size];
        }
    };
}
