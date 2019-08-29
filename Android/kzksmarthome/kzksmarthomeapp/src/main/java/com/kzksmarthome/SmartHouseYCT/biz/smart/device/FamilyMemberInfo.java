package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: FamilyMemberInfo
 * @Description: 家庭成员信息Bean
 * @author laixj
 * @date 2016/10/10 20:44
 * @version V1.0
 */
public class FamilyMemberInfo extends BaseModel implements Parcelable, Serializable {
    /**
     * "member_id": 2,
     "member_name": "xiexie",
     "member_type": 2,
     "account":"13678800000",
     "image": null
     */

    private int member_id;
    private String member_name;
    private int member_type;
    private String image;
    private String account;

    @Override
    public int hashCode() {
        return member_id;
    }

    @Override
    public String toString() {
        return "FamilyMemberInfo{" +
                "member_id=" + member_id +
                ", member_name='" + member_name + '\'' +
                ", member_type=" + member_type +
                ", image='" + image + '\'' +
                ", account='" + account + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FamilyMemberInfo that = (FamilyMemberInfo) o;

        if (member_id != that.member_id) return false;
        return true;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public FamilyMemberInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.member_id);
        dest.writeString(this.member_name);
        dest.writeInt(this.member_type);
        dest.writeString(this.image);
        dest.writeString(this.account);
    }

    protected FamilyMemberInfo(Parcel in) {
        this.member_id = in.readInt();
        this.member_name = in.readString();
        this.member_type = in.readInt();
        this.image = in.readString();
        this.account = in.readString();
    }

    public static final Creator<FamilyMemberInfo> CREATOR = new Creator<FamilyMemberInfo>() {
        @Override
        public FamilyMemberInfo createFromParcel(Parcel source) {
            return new FamilyMemberInfo(source);
        }

        @Override
        public FamilyMemberInfo[] newArray(int size) {
            return new FamilyMemberInfo[size];
        }
    };
}
