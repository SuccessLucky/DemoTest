package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

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
 * @Title: SceneDeviceSelectListAdapter
 * @Description: 用户设备权限列表适配器
 * @date 2016/9/14 16:20
 */
public class SceneDeviceSelectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private List<SceneDetailInfo> deviceList = new ArrayList<SceneDetailInfo>();
    //private ImageLoader mImageLoader;
    private String baseDeviceImgUrl = "";
    public interface OnDeviceItemClick {
        void onDeviceItemClick(int position, SceneDetailInfo deviceInfo);
    }

    private OnDeviceItemClick mListener;

    public List<SceneDetailInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<SceneDetailInfo> deviceList) {
        this.deviceList = deviceList;
    }

    public SceneDeviceSelectListAdapter(Activity context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
        baseDeviceImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, "");
    }

    public SceneDeviceSelectListAdapter(Activity context, List<SceneDetailInfo> deviceList) {
        this(context);
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
        SceneDetailInfo deviceInfo = deviceList.get(position);
        if (null != deviceInfo) {
            deviceViewHolder.smartDeviceRightsListNameTv.setText(deviceInfo.getDevice_name());
            if (deviceInfo.isSelected()) {
                deviceViewHolder.smartDeviceRightsListSelectIv.setVisibility(View.VISIBLE);
            } else {
                deviceViewHolder.smartDeviceRightsListSelectIv.setVisibility(View.INVISIBLE);
            }
            String floor = deviceInfo.getFloor_name()==null?"":deviceInfo.getFloor_name();
            String room = deviceInfo.getRoom_name()==null?"":deviceInfo.getRoom_name();
            deviceViewHolder.smartDeviceRightsListDescTv.setText(floor+" "+room);
            //deviceViewHolder.smartDeviceRightsListIconIv.loadImage(mImageLoader, baseDeviceImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
            //deviceViewHolder.smartDeviceRightsListIconIv.setImageResource(deviceInfo.getId());
            Glide
                    .with(mContext)
                    .load(baseDeviceImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(deviceViewHolder.smartDeviceRightsListIconIv);
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
        @BindView(R.id.smart_device_rights_list_select_iv)
        ImageView smartDeviceRightsListSelectIv;
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
            SceneDetailInfo clicked = deviceList.get(getPosition());
            if (null == clicked) {
                return;
            }
            clicked.setSelected(!clicked.isSelected());
            deviceList.set(getPosition(), clicked);
            if (mListener != null) {
                mListener.onDeviceItemClick(getPosition(), deviceList.get(getPosition()));
            }
            notifyDataSetChanged();
        }
    }
}
