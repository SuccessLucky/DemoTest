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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Title: RingtoneListAdapter
 * @Description: 铃声列表适配器
 * @author laixj
 * @date 2016/9/15 16:59
 * @version V1.0
 */
public class RingtoneListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<RingtoneInfo> ringtoneList = new ArrayList<RingtoneInfo>();
    private RingtoneInfo selectedRingtone;

    public interface OnRingtoneItemClick {
        void onRingtoneItemClick(int position, RingtoneInfo ringtoneInfo);
    }

    private OnRingtoneItemClick mListener;

    public RingtoneListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public RingtoneListAdapter(Context context, List<RingtoneInfo> ringtoneList, RingtoneInfo selectedRingtone) {
        this(context);
        this.ringtoneList = ringtoneList;
        this.selectedRingtone = selectedRingtone;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_ringtone_list_item, viewGroup, false);
        RingtoneViewHolder viewHolder = new RingtoneViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RingtoneViewHolder ringtoneViewHolder = (RingtoneViewHolder) viewHolder;
        RingtoneInfo ringtoneInfo = ringtoneList.get(position);
        if (null != ringtoneInfo) {
            ringtoneViewHolder.smartRingtoneListNameTv.setText(ringtoneInfo.getRingtoneName());
            if(null != selectedRingtone && ringtoneInfo.equals(selectedRingtone)){
                ringtoneViewHolder.smartRingtoneListSelectIv.setVisibility(View.VISIBLE);
            }else{
                ringtoneViewHolder.smartRingtoneListSelectIv.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return ringtoneList == null ? 0 : ringtoneList.size();
    }

    public void setOnRingtoneItemClickListener(OnRingtoneItemClick lis) {
        mListener = lis;
    }

    class RingtoneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_ringtone_list_select_iv)
        ImageView smartRingtoneListSelectIv;
        @BindView(R.id.smart_ringtone_list_name_tv)
        TextView smartRingtoneListNameTv;
        @BindView(R.id.smart_ringtone_list_item_rl)
        RelativeLayout smartRingtoneListItemRl;

        RingtoneViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_ringtone_list_item_rl)
        public void onClick() {
            RingtoneInfo clicked = ringtoneList.get(getPosition());
            if(null != selectedRingtone && clicked.equals(selectedRingtone)){
                selectedRingtone = clicked;
                //smartRingtoneListSelectIv.setVisibility(View.INVISIBLE);
            }else{
                selectedRingtone = clicked;
                smartRingtoneListSelectIv.setVisibility(View.VISIBLE);

            }
            if (mListener != null) {
                mListener.onRingtoneItemClick(getPosition(), selectedRingtone);
            }
            notifyDataSetChanged();
        }
    }
}
