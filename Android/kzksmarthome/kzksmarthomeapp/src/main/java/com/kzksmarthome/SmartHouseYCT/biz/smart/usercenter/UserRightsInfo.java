package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.module.user.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: SceneInfo
 * @Description: 场景模式信息Bean
 * @author laixj
 * @date 2016/9/14 14:09
 * @version V1.0
 */
public class UserRightsInfo extends BaseModel implements Parcelable,Serializable {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    /**
     * 可操作的设备列表
     */
    private List<DeviceInfo> deviceList;
    /**
     * 可操作的场景列表
     */
    private List<SceneInfo> sceneList;

    public UserRightsInfo(){}

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRightsInfo info = (UserRightsInfo) o;
        if (id.compareTo(info.getId()) != 0 ) {
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }

    public List<SceneInfo> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneInfo> sceneList) {
        this.sceneList = sceneList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeTypedList(this.deviceList);
        dest.writeTypedList(this.sceneList);
    }

    protected UserRightsInfo(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        this.deviceList = in.createTypedArrayList(DeviceInfo.CREATOR);
        this.sceneList = in.createTypedArrayList(SceneInfo.CREATOR);
    }

    public static final Creator<UserRightsInfo> CREATOR = new Creator<UserRightsInfo>() {
        @Override
        public UserRightsInfo createFromParcel(Parcel source) {
            return new UserRightsInfo(source);
        }

        @Override
        public UserRightsInfo[] newArray(int size) {
            return new UserRightsInfo[size];
        }
    };

    @Override
    public String toString() {
        return "UserRightsInfo{" +
                "id=" + id +
                ", userInfo=" + userInfo +
                ", deviceList=" + deviceList +
                ", sceneList=" + sceneList +
                '}';
    }
}
