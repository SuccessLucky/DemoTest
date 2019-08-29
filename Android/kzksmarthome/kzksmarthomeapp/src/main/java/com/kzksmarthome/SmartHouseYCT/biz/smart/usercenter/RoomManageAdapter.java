package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: RoomManageAdapter
 * @Description: 房间管理房间适配器
 * @date 2016/9/16 21:45
 */
public class RoomManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private LayoutInflater mInflater;
    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;
    private String baseImgUrl = "";
    /**
     * 是否可删除
     */
    private boolean canDelete;
    private LinearLayout.LayoutParams layoutParams = null;

    public interface OnRoomItemClick {
        void onDeleteClick(int position, RoomInfo roomInfo);
        void onRoomItemClick(int position, RoomInfo roomInfo);
        void onRoomItemLongClick(int position, RoomInfo roomInfo);
    }

    private OnRoomItemClick mListener;

    private List<RoomInfo> roomList = new ArrayList<RoomInfo>();
    /**
     * 房间Map
     */
    private Map<RoomInfo, List<DeviceInfo>> roomMap = new HashMap<RoomInfo, List<DeviceInfo>>();

    private int mCount = 0;

    public RoomManageAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_ROOM_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.smart_room_manage_item, viewGroup, false);
        RoomManageViewHolder viewHolder = new RoomManageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RoomManageViewHolder roomManageViewHolder = (RoomManageViewHolder) viewHolder;
        final RoomInfo roomInfo = roomList.get(position);
        Log.d("laixj", "getView-->" + position);
        if (null != roomInfo) {
            if (-1 == roomInfo.getId()) {
                roomManageViewHolder.smartRoomManageItemTv.setText("添加");
                roomManageViewHolder.smartRoomManageItemIm.setImageResource(R.drawable.scene_add_icon);
            } else {
                roomManageViewHolder.smartRoomManageItemTv.setText(roomInfo.getName());
                //imageLoader.displayImage(baseImgUrl + roomInfo.getImage() + "@2x.png", roomManageViewHolder.smartRoomManageItemIm);
                //roomManageViewHolder.smartRoomManageItemIm.loadImage(mImageLoader, baseImgUrl + roomInfo.getImage() + "@2x.png");
                Glide
                        .with(mContext)
                        .load(baseImgUrl + roomInfo.getImage() + "@2x.png")
                        .centerCrop()
                        .placeholder(R.drawable.default_img)
                        .crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(roomManageViewHolder.smartRoomManageItemIm);
            }
            if (isCanDelete()) {
                roomManageViewHolder.smartRoomManageItemRed.setVisibility(View.VISIBLE);
            } else {
                roomManageViewHolder.smartRoomManageItemRed.setVisibility(View.GONE);
            }
        }
        //roomManageViewHolder.smartRoomManageItemIm.setTag(position);
    }

    @Override
    public int getItemCount() {
        return roomList == null ? 0 : roomList.size();
    }

    public void setOnRoomItemClick(OnRoomItemClick lis) {
        mListener = lis;
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

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    class RoomManageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_room_manage_item_im)
        ImageView smartRoomManageItemIm;
        @BindView(R.id.smart_room_manage_item_tv)
        TextView smartRoomManageItemTv;
        @BindView(R.id.smart_room_manage_item_red)
        TextView smartRoomManageItemRed;
        @BindView(R.id.smart_room_manage_item_bottom_line)
        View smartRoomManageItemBottomLine;
        @BindView(R.id.smart_room_manage_item_right_line)
        View smartRoomManageItemRightLine;
        @BindView(R.id.smart_room_manage_rl)
        RelativeLayout smartRoomManageRl;

        @OnClick({R.id.smart_room_manage_rl, R.id.smart_room_manage_item_red})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.smart_room_manage_item_red:
                    if (isCanDelete()) {
                        if (null != mListener) {
                            mListener.onDeleteClick(getPosition(), roomList.get(getPosition()));
                        }
                    }
                    break;
                case R.id.smart_room_manage_rl:
                    if (!isCanDelete()) {
                        if (null != mListener) {
                            mListener.onRoomItemClick(getPosition(), roomList.get(getPosition()));
                        }
                    }
                    break;
            }
        }
        @OnLongClick(R.id.smart_room_manage_rl)
         boolean onLongClick(){
            if (!isCanDelete()) {
                if (null != mListener) {
                    mListener.onRoomItemLongClick(getPosition(), roomList.get(getPosition()));
                }
            }
            return false;
        }

        RoomManageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
