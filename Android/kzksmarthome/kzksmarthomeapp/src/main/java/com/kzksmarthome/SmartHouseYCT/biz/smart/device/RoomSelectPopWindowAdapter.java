package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: RoomSelectPopWindowAdapter
 * @Description: 房间选择PopWindow适配器
 * @date 2016/9/10 13:49
 */
public class RoomSelectPopWindowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<RoomInfo> roomList = new ArrayList<RoomInfo>();

    public interface OnRoomItemClick {
        void onRoomItemClick(RoomInfo roomInfo);
    }

    private OnRoomItemClick mListener;

    public RoomSelectPopWindowAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public RoomSelectPopWindowAdapter(Context context, List<RoomInfo> roomList) {
        this(context);
        this.roomList = roomList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_room_select_item, viewGroup, false);
        FloorSelectViewHolder viewHolder = new FloorSelectViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FloorSelectViewHolder deviceViewHolder = (FloorSelectViewHolder) viewHolder;
        RoomInfo roomInfo = roomList.get(position);
        if (null != roomInfo) {
            deviceViewHolder.smartRoomSelectItemTv.setText(roomInfo.getName());
        }
    }

    @Override
    public int getItemCount() {
        return roomList == null ? 0 : roomList.size();
    }

    public List<RoomInfo> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomInfo> roomList) {
        this.roomList = roomList;
    }

    class FloorSelectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_room_select_item_tv)
        TextView smartRoomSelectItemTv;
        @BindView(R.id.smart_room_select_rl)
        RelativeLayout smartRoomSelectRl;

        FloorSelectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_room_select_rl)
        public void onClick() {
            if (mListener != null) {
                mListener.onRoomItemClick(roomList.get(getPosition()));
            }
        }
    }

    public void setOnRoomItemClickListener(OnRoomItemClick lis) {
        mListener = lis;
    }
}
