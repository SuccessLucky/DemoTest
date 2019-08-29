package com.kzksmarthome.common.module.push;

import android.os.Parcel;
import android.os.Parcelable;

public class ArrowPushInfo implements Parcelable {

    public static final int ACTION_TYPE_OPEN_SELF = 1;
    public static final int ACTION_TYPE_SELF_UPGRADE = 2;
    public static final int ACTION_TYPE_OPEN_EXTRA_BROWSER = 3;
    public static final int ACTION_TYPE_OPEN_SELF_BROWSER = 4;
    public static final int ACTION_TYPE_OPEN_MSG_DETAIL = 5;
    
    public int mPushID;
    public String mPushTitle;
    public String mPushSummery;
    public long mPushTime;
    //0标示不显示，1标示显示在通知栏，2标示显示在应用内部，3标示显示在通知栏和应用内部
    public int mShowType;
    //1标示打开应用（主界面，二级页面），2下载应用更新， 3外部浏览器打开网页，4内置浏览器打开网页，5打开消息详情页
    public int mActionType;
    public String mClickAction;
    
    public Parcelable mPushDetail;
    
    public ArrowPushInfo() {
    }
    
    public ArrowPushInfo(Parcel source) {
        mPushID = source.readInt();
        mPushTitle = source.readString();
        mPushSummery = source.readString();
        mPushTime = source.readLong();
        mShowType = source.readInt();
        mActionType = source.readInt();
        mClickAction = source.readString();
        mPushDetail = source.readParcelable(getClass().getClassLoader());
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPushID);
        dest.writeString(mPushTitle);
        dest.writeString(mPushSummery);
        dest.writeLong(mPushTime);
        dest.writeInt(mShowType);
        dest.writeInt(mActionType);
        dest.writeString(mClickAction);
        dest.writeParcelable(mPushDetail, flags);
    }
    
    
    public static final Parcelable.Creator<ArrowPushInfo> CREATOR = new Parcelable.Creator<ArrowPushInfo>() {
        public ArrowPushInfo createFromParcel(Parcel source) {
            return new ArrowPushInfo(source);
        }

        public ArrowPushInfo[] newArray(int size) {
            return new ArrowPushInfo[size];
        }
    };

    @Override
    public String toString() {
        return "ArrowPushInfo [mPushID=" + mPushID + ", mPushTitle=" + mPushTitle + ", mPushSummery="
                + mPushSummery + ", mPushTime=" + mPushTime + ", mShowType=" + mShowType
                + ", mActionType=" + mActionType + ", mClickAction=" + mClickAction + ", mPushDetail" + mPushDetail + "]";
    }
    
    

}
