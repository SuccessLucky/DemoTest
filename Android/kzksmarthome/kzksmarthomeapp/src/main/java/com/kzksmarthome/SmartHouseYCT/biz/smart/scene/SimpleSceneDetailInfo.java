package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @Title: SceneDetailInfo
 * @Description: 场景详细信息Bean
 * @author laixj
 * @date 2016/9/14 14:09
 * @version V1.0
 */
public class SimpleSceneDetailInfo extends BaseModel implements Parcelable {
    /**
     * scene_detail_id : 0
     * device_id : 1
     * device_state1 : 1
     * device_state2 : 0
     * device_state3 : 0
     * other_status : 30
     */

    private int scene_detail_id;
    private int device_id;
    private int device_state1;
    private int device_state2;
    private int device_state3;
    private String other_status;

    public int getScene_detail_id() {
        return scene_detail_id;
    }

    public void setScene_detail_id(int scene_detail_id) {
        this.scene_detail_id = scene_detail_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getDevice_state1() {
        return device_state1;
    }

    public void setDevice_state1(int device_state1) {
        this.device_state1 = device_state1;
    }

    public int getDevice_state2() {
        return device_state2;
    }

    public void setDevice_state2(int device_state2) {
        this.device_state2 = device_state2;
    }

    public int getDevice_state3() {
        return device_state3;
    }

    public void setDevice_state3(int device_state3) {
        this.device_state3 = device_state3;
    }

    public String getOther_status() {
        return other_status;
    }

    public void setOther_status(String other_status) {
        this.other_status = other_status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleSceneDetailInfo that = (SimpleSceneDetailInfo) o;

        return scene_detail_id == that.scene_detail_id;

    }

    @Override
    public int hashCode() {
        return scene_detail_id;
    }

    @Override
    public String toString() {
        return "SimpleSceneDetailInfo{" +
                "scene_detail_id=" + scene_detail_id +
                ", device_id=" + device_id +
                ", device_state1=" + device_state1 +
                ", device_state2=" + device_state2 +
                ", device_state3=" + device_state3 +
                ", other_status='" + other_status + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.scene_detail_id);
        dest.writeInt(this.device_id);
        dest.writeInt(this.device_state1);
        dest.writeInt(this.device_state2);
        dest.writeInt(this.device_state3);
        dest.writeString(this.other_status);
    }

    public SimpleSceneDetailInfo() {
    }

    protected SimpleSceneDetailInfo(Parcel in) {
        this.scene_detail_id = in.readInt();
        this.device_id = in.readInt();
        this.device_state1 = in.readInt();
        this.device_state2 = in.readInt();
        this.device_state3 = in.readInt();
        this.other_status = in.readString();
    }

    public static final Creator<SimpleSceneDetailInfo> CREATOR = new Creator<SimpleSceneDetailInfo>() {
        @Override
        public SimpleSceneDetailInfo createFromParcel(Parcel source) {
            return new SimpleSceneDetailInfo(source);
        }

        @Override
        public SimpleSceneDetailInfo[] newArray(int size) {
            return new SimpleSceneDetailInfo[size];
        }
    };
}
