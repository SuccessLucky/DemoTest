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
 * @Title: FloorSelectPopWindowAdapter
 * @Description: 楼层选择PopWindow适配器
 * @date 2016/9/10 13:49
 */
public class FloorSelectPopWindowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FloorInfo> floorList = new ArrayList<FloorInfo>();

    public interface OnFloorItemClick {
        void onFloorItemClick(FloorInfo floorInfo);
    }

    private OnFloorItemClick mListener;

    public FloorSelectPopWindowAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public FloorSelectPopWindowAdapter(Context context, List<FloorInfo> floorList) {
        this(context);
        this.floorList = floorList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_floor_select_item, viewGroup, false);
        FloorSelectViewHolder viewHolder = new FloorSelectViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FloorSelectViewHolder deviceViewHolder = (FloorSelectViewHolder) viewHolder;
        FloorInfo floorInfo = floorList.get(position);
        if (null != floorInfo) {
            deviceViewHolder.smartFloorSelectItemTv.setText(floorInfo.getName());
        }
    }

    @Override
    public int getItemCount() {
        return floorList == null ? 0 : floorList.size();
    }

    public List<FloorInfo> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<FloorInfo> floorList) {
        this.floorList = floorList;
    }

    class FloorSelectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_floor_select_item_tv)
        TextView smartFloorSelectItemTv;
        @BindView(R.id.smart_floor_select_rl)
        RelativeLayout smartFloorSelectRl;

        FloorSelectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_floor_select_rl)
        public void onClick() {
            if (mListener != null) {
                mListener.onFloorItemClick(floorList.get(getPosition()));
            }
        }
    }

    public void setOnFloorItemClickListener(OnFloorItemClick lis) {
        mListener = lis;
    }
}
