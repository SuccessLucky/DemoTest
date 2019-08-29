package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备房间适配器
 * Created by jack on 2016/9/6.
 */
public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnRoomItemClick {
        void onRoomItemClick(int position);
    }

    private OnRoomItemClick mListener;

    private Activity mContext;
    private LayoutInflater mInflater;
    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;
    private String baseImgUrl = "";
    private List<RoomInfo> roomList = new ArrayList<RoomInfo>();
    /**
     * 房间Map
     */
    private Map<RoomInfo, List<DeviceInfo>> roomMap = new HashMap<RoomInfo, List<DeviceInfo>>();

    public RoomAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_ROOM_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.smart_device_main_item, viewGroup, false);
        DeviceMainViewHolder viewHolder = new DeviceMainViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DeviceMainViewHolder sceneMainViewHolder = (DeviceMainViewHolder) viewHolder;
        RoomInfo roomInfo = roomList.get(position);
        if (null != roomInfo) {
            sceneMainViewHolder.smartDeviceMainItemTv.setText(roomInfo.getName());
            //imageLoader.displayImage(baseImgUrl + roomInfo.getImage() + "@2x.png", sceneMainViewHolder.smartDeviceMainItemIm);
            //sceneMainViewHolder.smartDeviceMainItemIm.loadImage(mImageLoader, baseImgUrl + roomInfo.getImage() + "@2x.png");
            //sceneMainViewHolder.smartDeviceMainItemTv.setTag(baseImgUrl + roomInfo.getImage() + "@2x.png");
            Glide
                    .with(mContext)
                    .load(baseImgUrl + roomInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sceneMainViewHolder.smartDeviceMainItemIm);

        }
        int num = (position + 1) % 3;
        if( num == 0){
            sceneMainViewHolder.roomItemRightLine.setVisibility(View.INVISIBLE);
        }else{
            sceneMainViewHolder.roomItemRightLine.setVisibility(View.VISIBLE);
        }
        int count = getItemCount();
        // 最大行数
        int maxNum = (int) Math.ceil((double) count / (double) 3);
        int positionLineNum = (int) Math.ceil((double) (position + 1) / (double) 3);

        if(maxNum == positionLineNum){
            sceneMainViewHolder.roomItemBottomLine.setVisibility(View.INVISIBLE);
        }else{
            sceneMainViewHolder.roomItemBottomLine.setVisibility(View.VISIBLE);
        }

        //sceneMainViewHolder.smartDeviceMainItemIm.setTag(position);
    }

    @Override
    public int getItemCount() {
        return roomList == null ? 0 : roomList.size();
    }

    public void setOnRoomItemClickListener(OnRoomItemClick lis) {
        mListener = lis;
    }

    class DeviceMainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_device_main_item_im)
        ImageView smartDeviceMainItemIm;
        @BindView(R.id.smart_device_main_item_tv)
        TextView smartDeviceMainItemTv;
        @BindView(R.id.smart_device_main_item_red)
        TextView smartDeviceMainItemRed;
      /*  @BindView(R.id.smart_device_main_item_bottom_line)
        View smartDeviceMainItemBottomLine;
        @BindView(R.id.smart_device_main_item_right_line)
        View smartDeviceMainItemRightLine;*/
        @BindView(R.id.smart_device_main_item_rl)
        LinearLayout smartDeviceMainItemRl;

        @BindView(R.id.room_item_right_line)
        View roomItemRightLine;
        @BindView(R.id.room_item_bottom_line)
        View roomItemBottomLine;

        @OnClick({R.id.smart_device_main_item_rl})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.smart_device_main_item_rl:
                    if (null != mListener) {
                        mListener.onRoomItemClick(getPosition());
                    }
                    break;
            }
        }

        DeviceMainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<RoomInfo> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomInfo> roomList) {
        this.roomList = roomList;
    }

    public Map<RoomInfo, List<DeviceInfo>> getRoomMap() {
        return roomMap;
    }

    public void setRoomMap(Map<RoomInfo, List<DeviceInfo>> roomMap) {
        this.roomMap = roomMap;
    }

}
