package com.kzksmarthome.common.module.push.msg;

import android.os.Parcel;
import android.os.Parcelable;

public class ArrowPushMsg implements Parcelable {

    public static final int MSG_STATE_UNREAD = 1;
    public static final int MSG_STATE_READ = 2;
    
    public int mMsgID;
    public String mMsgTitle;
    public String mMsgContent;
    public int mMsgType;
    public long mTime;
    //1标示未读，2标示已读
    public int mReadState;
    public int mExtend1;
    public String mExtend2;
    
    public ArrowPushMsg() {
    }
    
    public ArrowPushMsg(Parcel source) {
        mMsgID = source.readInt();
        mMsgTitle = source.readString();
        mMsgContent = source.readString();
        mMsgType = source.readInt();
        mTime = source.readLong();
        mReadState = source.readInt();
        mExtend1 = source.readInt();
        mExtend2 = source.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMsgID);
        dest.writeString(mMsgTitle);
        dest.writeString(mMsgContent);
        dest.writeInt(mMsgType);
        dest.writeLong(mTime);
        dest.writeInt(mReadState);
        dest.writeInt(mExtend1);
        dest.writeString(mExtend2);
    }
    
    
    public static final Parcelable.Creator<ArrowPushMsg> CREATOR = new Parcelable.Creator<ArrowPushMsg>() {
        public ArrowPushMsg createFromParcel(Parcel source) {
            return new ArrowPushMsg(source);
        }

        public ArrowPushMsg[] newArray(int size) {
            return new ArrowPushMsg[size];
        }
    };

    @Override
    public String toString() {
        return "ArrowPushMsg [mMsgID=" + mMsgID + ", mMsgTitle=" + mMsgTitle + ", mMsgContent="
                + mMsgContent + ", mMsgType=" + mMsgType + ", mTime=" + mTime + ", mReadState="
                + mReadState + ", mExtend1=" + mExtend1 + ", mExtend2=" + mExtend2 + "]";
    }
    
}
