package com.kzksmarthome.SmartHouseYCT.biz.photo;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * 
 * @Description: 图片信息
 * @Copyright: Copyright (c) 2015
 * @version: 1.0.0.0
 * @author: jack
 * @createDate 2015-5-6
 * 
 */
public class PhotoData extends BaseModel implements Parcelable {
    public String title;
    /**
     * 图片描述
     */
    public String photoDescription;
    /**
     * 图片url
     */
    public String photoUrl;

    public PhotoData() {
    }

    public PhotoData(Parcel source) {
        title = source.readString();
        photoDescription = source.readString();
        photoUrl = source.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(photoDescription);
        dest.writeString(photoUrl);
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>() {
        public PhotoData createFromParcel(Parcel source) {
            return new PhotoData(source);
        }

        public PhotoData[] newArray(int size) {
            return new PhotoData[size];
        }
    };

    @Override
    public String toString() {
        return "PhotoData [title=" + title + ", photoDescription=" + photoDescription + ", photoUrl=" + photoUrl + "]";
    }
}
