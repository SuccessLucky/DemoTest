package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;

public class EventOfResultAddScene implements Parcelable {
    /**
     * 添加的场景
     */
    public SceneInfo sceneInfo;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sceneInfo, flags);
    }

    public EventOfResultAddScene() {
    }

    protected EventOfResultAddScene(Parcel in) {
        this.sceneInfo = in.readParcelable(SceneInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultAddScene> CREATOR = new Creator<EventOfResultAddScene>() {
        @Override
        public EventOfResultAddScene createFromParcel(Parcel source) {
            return new EventOfResultAddScene(source);
        }

        @Override
        public EventOfResultAddScene[] newArray(int size) {
            return new EventOfResultAddScene[size];
        }
    };
}
