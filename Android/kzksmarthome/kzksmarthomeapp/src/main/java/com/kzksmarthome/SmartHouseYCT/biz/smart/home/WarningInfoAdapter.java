package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * 告警信息适配器
 * Created by jack on 2016/10/29.
 */

public class WarningInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WarningInfoBean> mDataList;

    public WarningInfoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(ArrayList<WarningInfoBean> data){
        mDataList = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.warning_item_layout, viewGroup, false);
        WarningViewHolder warningViewHolder = new WarningViewHolder(view);
        return warningViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        WarningViewHolder holder = (WarningViewHolder) viewHolder;
        WarningInfoBean warningInfoBean = mDataList.get(position);
        if(warningInfoBean != null){
            holder.warningInfoTimeTv.setText(warningInfoBean.getCreate_date());
            holder.warningInfoTv.setText(warningInfoBean.getAlarm_msg());
        }

    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0:mDataList.size();
    }

     class WarningViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.warning_info_im)
        ImageView warningInfoIm;
        @BindView(R.id.warning_info_tv)
        TextView warningInfoTv;
        @BindView(R.id.warning_info_time_tv)
        TextView warningInfoTimeTv;

         WarningViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
