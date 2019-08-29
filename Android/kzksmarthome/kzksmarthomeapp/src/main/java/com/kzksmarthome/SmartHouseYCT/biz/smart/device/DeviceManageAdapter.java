package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * 设备适配器
 * Created by jack on 2016/9/5.
 */
public class DeviceManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;
    private String baseImgUrl = "";

    public interface OnDeviceItemClick {
        void onDeleteClick(int position, DeviceInfo deviceInfo);
        void onAddClick();
        void onEditClick(int position, DeviceInfo deviceInfo);
    }

    private OnDeviceItemClick mListener;

    /**
     * 是否可删除
     */
    private boolean canDelete;

    public DeviceManageAdapter(Activity context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_device_manage_item, viewGroup, false);
        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DeviceViewHolder deviceViewHolder = (DeviceViewHolder) viewHolder;
        /*if (position == (getItemCount() - 1)) {
            deviceViewHolder.smartBottomView.setVisibility(View.VISIBLE);
        } else {
            deviceViewHolder.smartBottomView.setVisibility(View.GONE);
        }*/
        DeviceInfo deviceInfo = deviceList.get(position);
        if (null != deviceInfo) {
            if (isCanDelete()) {
                deviceViewHolder.smartDeviceManageItemRed.setVisibility(View.VISIBLE);
            } else {
                deviceViewHolder.smartDeviceManageItemRed.setVisibility(View.GONE);
            }
            if (-1 == deviceInfo.getDevice_id()) {
                deviceViewHolder.smartDeviceManageTv.setText("添加");
                deviceViewHolder.smartDeviceManageIm.setImageResource(R.drawable.scene_add_icon);
            } else {
                deviceViewHolder.smartDeviceManageTv.setText(deviceInfo.getDevice_name());
                //imageLoader.displayImage(baseImgUrl + "Un_"+deviceInfo.getImage() + "@2x.png", deviceViewHolder.smartDeviceManageIm);
                //deviceViewHolder.smartDeviceManageIm.loadImage(mImageLoader, baseImgUrl + "Un_"+deviceInfo.getImage() + "@2x.png");
                Glide
                        .with(mContext)
                        .load(baseImgUrl + "Un_"+deviceInfo.getImage() + "@2x.png")
                        .centerCrop()
                        .placeholder(R.drawable.default_img)
                        .crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(deviceViewHolder.smartDeviceManageIm);
            }
        }
    }

    @Override
    public int getItemCount() {
        return deviceList == null ? 0 : deviceList.size();
    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public void setOnDeviceItemClickListener(OnDeviceItemClick lis) {
        mListener = lis;
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_device_manage_im)
        ImageView smartDeviceManageIm;
        @BindView(R.id.smart_device_manage_item_red)
        TextView smartDeviceManageItemRed;
        @BindView(R.id.smart_device_manage_tv)
        TextView smartDeviceManageTv;
        /*@BindView(R.id.smart_bottom_view)
        View smartBottomView;*/
        @BindView(R.id.smart_device_manage_rl)
        RelativeLayout smartDeviceManageRl;

        @OnClick(R.id.smart_device_manage_rl)
        public void onClick() {
            DeviceInfo deviceInfo = deviceList.get(getPosition());
            if (null != deviceInfo) {
                if(isCanDelete()){//删除设备
                    // TODO: 2016/9/17
                    if(null != mListener){
                        mListener.onDeleteClick(getPosition(), deviceInfo);
                    }
                }else{
                    if(-1 == deviceInfo.getDevice_id()){//添加设备
                        if(null != mListener){
                            mListener.onAddClick();
                        }
                    }else{
                        SmartHomeApp.showToast(deviceInfo.getDevice_name());
                    }
                }
            }
        }
        @OnLongClick(R.id.smart_device_manage_rl)
        public boolean onLongClick(){
            DeviceInfo deviceInfo = deviceList.get(getPosition());
            mListener.onEditClick(getPosition(), deviceInfo);
            return true;
        }
        DeviceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
