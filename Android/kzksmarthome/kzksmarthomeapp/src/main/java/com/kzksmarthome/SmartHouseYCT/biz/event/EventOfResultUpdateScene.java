package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;

public class EventOfResultUpdateScene implements Parcelable {
    /**
     * 修改的场景在list中的位置
     */
    public int position;

    /**
     * 修改的场景
     */
    public SceneInfo sceneInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeParcelable(this.sceneInfo, flags);
    }

    public EventOfResultUpdateScene() {
    }

    protected EventOfResultUpdateScene(Parcel in) {
        this.position = in.readInt();
        this.sceneInfo = in.readParcelable(SceneInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultUpdateScene> CREATOR = new Creator<EventOfResultUpdateScene>() {
        @Override
        public EventOfResultUpdateScene createFromParcel(Parcel source) {
            return new EventOfResultUpdateScene(source);
        }

        @Override
        public EventOfResultUpdateScene[] newArray(int size) {
            return new EventOfResultUpdateScene[size];
        }
    };
}
