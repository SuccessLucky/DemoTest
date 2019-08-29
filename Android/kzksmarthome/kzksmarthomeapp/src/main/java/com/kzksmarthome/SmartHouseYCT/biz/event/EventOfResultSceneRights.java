package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;

import java.util.List;

public class EventOfResultSceneRights implements Parcelable {
    /**
     * 选中的场景权限
     */
    public List<SceneInfo> sceneList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.sceneList);
    }

    public EventOfResultSceneRights() {
    }

    protected EventOfResultSceneRights(Parcel in) {
        this.sceneList = in.createTypedArrayList(SceneInfo.CREATOR);
    }

    public static final Creator<EventOfResultSceneRights> CREATOR = new Creator<EventOfResultSceneRights>() {
        @Override
        public EventOfResultSceneRights createFromParcel(Parcel source) {
            return new EventOfResultSceneRights(source);
        }

        @Override
        public EventOfResultSceneRights[] newArray(int size) {
            return new EventOfResultSceneRights[size];
        }
    };
}
