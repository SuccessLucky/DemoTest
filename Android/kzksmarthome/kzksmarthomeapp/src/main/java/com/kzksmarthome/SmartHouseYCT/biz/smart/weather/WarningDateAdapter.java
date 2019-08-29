package com.kzksmarthome.SmartHouseYCT.biz.smart.weather;

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
 * 告警日期适配器
 * Created by jack on 2016/10/29.
 */

public class WarningDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{



    public interface DateItemClick {
        void onDateItemClick(int position);
    }
    private DateItemClick mDateItemClick;

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDateList;

    /**
     * 选中的时间
     */
    private int mSelectPosition = -1;

    public WarningDateAdapter(Context context,DateItemClick dateItemClick) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDateItemClick = dateItemClick;
    }

    public void setData(ArrayList<String> data){
        mDateList = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.warning_date_item, viewGroup, false);
        WarningDateViewHolder warningDateViewHolder = new WarningDateViewHolder(view);
        warningDateViewHolder.warningDateTv.setOnClickListener(this);
        warningDateViewHolder.warningDateIm.setOnClickListener(this);
        return warningDateViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        WarningDateViewHolder holder = (WarningDateViewHolder)viewHolder;
        String dateStr = mDateList.get(position);
        if(dateStr != null){
            holder.warningDateTv.setTag(position);
            holder.warningDateIm.setTag(position);
            if(!"more".equals(dateStr)) {
                holder.warningDateTv.setVisibility(View.VISIBLE);
                holder.warningDateIm.setVisibility(View.GONE);
                String[] dateStrArray = dateStr.split("-");
                if (dateStrArray != null && dateStrArray.length > 2) {
                    holder.warningDateTv.setText(dateStrArray[2]);
                }
                if(mSelectPosition != -1 && mSelectPosition == position){
                    holder.warningDateTv.setTextColor(mContext.getResources().getColor(R.color.red));
                }else{
                    holder.warningDateTv.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }else{
                holder.warningDateTv.setVisibility(View.GONE);
                holder.warningDateIm.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDateList == null ? 0:mDateList.size();
    }

     class WarningDateViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.warning_date_tv)
        TextView warningDateTv;
         @BindView(R.id.warning_date_im)
         ImageView warningDateIm;

         WarningDateViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onClick(View view) {
      Integer position = (Integer) view.getTag();
        if(position != null){
            mDateItemClick.onDateItemClick(position);
        }
    }

    /**
     * 设置选中时间
     * @param mSelectPosition
     */
    public void setmSelectPosition(int mSelectPosition) {
        this.mSelectPosition = mSelectPosition;
        notifyDataSetChanged();
    }
}
