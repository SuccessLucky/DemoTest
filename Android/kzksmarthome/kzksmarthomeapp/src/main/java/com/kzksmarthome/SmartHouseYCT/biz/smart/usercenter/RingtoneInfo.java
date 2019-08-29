package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: RingtoneInfo
 * @Description: 铃声信息Bean
 * @author laixj
 * @date 2016/9/15 16:53
 * @version V1.0
 */
public class RingtoneInfo extends BaseModel implements Parcelable,Serializable {

    /**
     * 编号
     */
    private int id;
    /**
     * 铃声名称
     */
    private String ringtoneName;
    /**
     * 铃声uri
     */
    private String ringtoneUri;

    public RingtoneInfo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    @Override
    public String toString() {
        return "RingtoneInfo{" +
                "id=" + id +
                ", ringtoneName='" + ringtoneName + '\'' +
                ", ringtoneUri='" + ringtoneUri + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RingtoneInfo ringtone = (RingtoneInfo) o;
        if (id != ringtone.getId() || ringtoneName.compareTo(ringtone.getRingtoneName()) != 0) {
            return false;
        }
        return true;
    }

    public RingtoneInfo(int id, String ringtoneName, String ringtoneUri) {
        this.id = id;
        this.ringtoneName = ringtoneName;
        this.ringtoneUri = ringtoneUri;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.ringtoneName);
        dest.writeString(this.ringtoneUri);
    }

    protected RingtoneInfo(Parcel in) {
        this.id = in.readInt();
        this.ringtoneName = in.readString();
        this.ringtoneUri = in.readString();
    }

    public static final Creator<RingtoneInfo> CREATOR = new Creator<RingtoneInfo>() {
        @Override
        public RingtoneInfo createFromParcel(Parcel source) {
            return new RingtoneInfo(source);
        }

        @Override
        public RingtoneInfo[] newArray(int size) {
            return new RingtoneInfo[size];
        }
    };
}
