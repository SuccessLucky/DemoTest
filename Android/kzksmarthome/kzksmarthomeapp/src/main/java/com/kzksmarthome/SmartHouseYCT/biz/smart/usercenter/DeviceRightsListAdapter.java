package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceRightsListAdapter
 * @Description: 用户设备权限列表适配器
 * @date 2016/9/14 16:20
 */
public class DeviceRightsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

    private String baseImgUrl = "";

    public interface OnDeviceItemClick {
        void onDeviceItemClick(int position, DeviceInfo deviceInfo);
    }

    private OnDeviceItemClick mListener;

    public DeviceRightsListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, "");
    }

    public DeviceRightsListAdapter(Context context, List<DeviceInfo> deviceList) {
        this(context);
        this.deviceList = deviceList;
    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_device_rights_list_item, viewGroup, false);
        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DeviceViewHolder deviceViewHolder = (DeviceViewHolder) viewHolder;
        DeviceInfo deviceInfo = deviceList.get(position);
        if (null != deviceInfo) {
            deviceViewHolder.smartDeviceRightsListNameTv.setText(deviceInfo.getDevice_name());
            deviceViewHolder.smartDeviceRightsListDescTv.setText(deviceInfo.getFloor_name()+deviceInfo.getRoom_name());
            Glide
                    .with(mContext)
                    .load(baseImgUrl + "Pr_"+deviceInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(deviceViewHolder.smartDeviceRightsListIconIv);
            //deviceViewHolder.smartDeviceRightsListIconIv.setImageResource(deviceInfo.getId());
        }
    }

    @Override
    public int getItemCount() {
        return deviceList == null ? 0 : deviceList.size();
    }

    public void setOnDeviceItemClickListener(OnDeviceItemClick lis) {
        mListener = lis;
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_device_rights_list_icon_iv)
        ImageView smartDeviceRightsListIconIv;
        @BindView(R.id.smart_device_rights_list_name_tv)
        TextView smartDeviceRightsListNameTv;
        @BindView(R.id.smart_device_rights_list_desc_tv)
        TextView smartDeviceRightsListDescTv;
        @BindView(R.id.smart_device_rights_list_item_rl)
        RelativeLayout smartDeviceRightsListItemRl;

        DeviceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_device_rights_list_item_rl)
        public void onClick() {
            if (mListener != null) {
                mListener.onDeviceItemClick(getPosition(), deviceList.get(getPosition()));
            }
        }
    }
}
