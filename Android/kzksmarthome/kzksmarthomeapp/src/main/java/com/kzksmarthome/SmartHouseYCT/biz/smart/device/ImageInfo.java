package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: ImageInfo
 * @Description: 图片信息Bean
 * @author laixj
 * @date 2016/9/8 22:25
 * @version V1.0
 */
public class ImageInfo extends BaseModel implements Parcelable,Serializable {
    /**
     * id : 2
     * base_url : http://localhost:8080/staticFile/imgs/room/
     * name : room001
     * image_type : png
     */

    private int id;
    private String base_url;
    private String name;
    private String image_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageInfo imageInfo = (ImageInfo) o;

        if (id != imageInfo.id) return false;
        return image_type != null ? image_type.equals(imageInfo.image_type) : imageInfo.image_type == null;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", base_url='" + base_url + '\'' +
                ", name='" + name + '\'' +
                ", image_type='" + image_type + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.base_url);
        dest.writeString(this.name);
        dest.writeString(this.image_type);
    }

    public ImageInfo() {
    }

    protected ImageInfo(Parcel in) {
        this.id = in.readInt();
        this.base_url = in.readString();
        this.name = in.readString();
        this.image_type = in.readString();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };
}
