package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: FloorListAdapter
 * @Description: 楼层列表适配器
 * @date 2016/9/14 16:52
 */
public class FloorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FloorInfo> floorList = new ArrayList<FloorInfo>();

    public interface OnFloorItemClick {
        void onFloorItemClick(int position, FloorInfo floorInfo);
        void onFloorItemLongClick(int position, FloorInfo floorInfo);
    }

    private OnFloorItemClick mListener;

    public List<FloorInfo> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<FloorInfo> floorList) {
        this.floorList = floorList;
    }

    public FloorListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public FloorListAdapter(Context context, List<FloorInfo> floorList) {
        this(context);
        this.floorList = floorList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_floor_list_item, viewGroup, false);
        FloorViewHolder viewHolder = new FloorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FloorViewHolder floorViewHolder = (FloorViewHolder) viewHolder;
        FloorInfo floorInfo = floorList.get(position);
        if (null != floorInfo) {
            floorViewHolder.smartFloorListNameTv.setText(floorInfo.getName());
            // TODO: 2016/9/14  
        }
    }

    @Override
    public int getItemCount() {
        return floorList == null ? 0 : floorList.size();
    }

    public void setOnFloorItemClickListener(OnFloorItemClick lis) {
        mListener = lis;
    }

    class FloorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_floor_list_icon_iv)
        ImageView smartFloorListIconIv;
        @BindView(R.id.smart_floor_list_go_iv)
        ImageView smartFloorListGoIv;
        @BindView(R.id.smart_floor_list_name_tv)
        TextView smartFloorListNameTv;
        @BindView(R.id.smart_floor_list_item_rl)
        RelativeLayout smartFloorListItemRl;

        FloorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_floor_list_item_rl)
        public void onClick() {
            if (mListener != null) {
                mListener.onFloorItemClick(getPosition(), floorList.get(getPosition()));
            }
        }

        @OnLongClick(R.id.smart_floor_list_item_rl)
        public boolean onLongClick() {
            if (mListener != null) {
                mListener.onFloorItemLongClick(getPosition(), floorList.get(getPosition()));
            }
            return true;
        }
    }
}
