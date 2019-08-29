package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceAddDialogIconAdapter
 * @Description: 设备添加/编辑房间图标适配器
 * @date 2016/9/17 20:39
 */
public class DeviceAddDialogIconAdapter extends BaseAdapter {

    private Activity mContext;
    private LayoutInflater mInflater;

    //protected ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;

    private int selected = -1;

    public interface OnDeviceTypeClick {
        void onDeviceTypeClick(int position, DeviceTypeEnums deviceTypeEnums);
    }

    private OnDeviceTypeClick mListener;

    //private List<DeviceTypeInfo> typeList = new ArrayList<DeviceTypeInfo>();
    private List<ImageInfo> typeList = new ArrayList<ImageInfo>();

    public DeviceAddDialogIconAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
        //typeList = EnumUtil.toList(DeviceTypeEnums.class);
    }

    @Override
    public int getCount() {
        return typeList == null ? 0 : typeList.size();
    }

    @Override
    public Object getItem(int position) {
        return typeList == null ? null : typeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.smart_device_add_type_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageInfo typeInfo = typeList.get(position);
        if (null != typeInfo) {
            if (selected > -1) {
                if (selected == position) {
                    viewHolder.smartDeviceTypeItemRed.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.smartDeviceTypeItemRed.setVisibility(View.GONE);
                }
            } else {
                viewHolder.smartDeviceTypeItemRed.setVisibility(View.GONE);
            }
            //imageLoader.displayImage(typeInfo.getBase_url()+"Un_"+typeInfo.getName()+"."+typeInfo.getImage_type(), viewHolder.smartDeviceTypeItemIv);
            //viewHolder.smartDeviceTypeItemIv.loadImage(mImageLoader, typeInfo.getBase_url()+"Un_"+typeInfo.getName()+"@2x."+typeInfo.getImage_type());
            Glide
                    .with(mContext)
                    .load(typeInfo.getBase_url()+"Un_"+typeInfo.getName()+"@2x."+typeInfo.getImage_type())
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.smartDeviceTypeItemIv);
        }
        return convertView;
    }

    public void setOnDeviceTypeClickListener(OnDeviceTypeClick lis) {
        mListener = lis;
    }

    public List<ImageInfo> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<ImageInfo> typeList) {
        this.typeList = typeList;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    static class ViewHolder {
        @BindView(R.id.smart_device_type_item_iv)
        ImageView smartDeviceTypeItemIv;
        @BindView(R.id.smart_device_type_item_red)
        TextView smartDeviceTypeItemRed;
        @BindView(R.id.smart_device_type_item_rl)
        RelativeLayout smartDeviceTypeItemRl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
