package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.SettingIOTRadeyActivity;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.SmartHouseYCT.util.UserRoleEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: GwListAdapter
 * @Description: 网关列表适配器
 * @date 2016/9/15 8:14
 */
public class GwListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GatewayInfo> gwList = new ArrayList<GatewayInfo>();
    private GatewayInfo selectedGw;

    public interface OnGwItemClick {
        void onGwItemClick(int position, GatewayInfo gwInfo);
        void onGwItemLongClick(int position, GatewayInfo gwInfo);
    }

    private OnGwItemClick mListener;

    public GwListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public GwListAdapter(Context context, List<GatewayInfo> gwList, GatewayInfo selectedGw) {
        this(context);
        this.gwList = gwList;
        this.selectedGw = selectedGw;
    }

    @Override
    public int getItemViewType(int position) {
        return gwList == null ? 0 : gwList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                view = mLayoutInflater.inflate(R.layout.smart_gw_list_item, viewGroup, false);
                GwViewHolder gwViewHolder = new GwViewHolder(view);
                viewHolder = gwViewHolder;
                break;
            case 1:
                view = mLayoutInflater.inflate(R.layout.smart_gw_add_item, viewGroup, false);
                AddViewHolder  addViewHolder = new AddViewHolder(view);
                viewHolder = addViewHolder;
                addViewHolder.smartGwAdd.setOnClickListener(this);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case 0:
                GwViewHolder gwViewHolder = (GwViewHolder) viewHolder;
                GatewayInfo gwInfo = gwList.get(position);
                if (null != gwInfo) {
                    gwViewHolder.smartGwListNameTv.setText(gwInfo.getMac_address());
                    if (null != selectedGw && gwInfo.equals(selectedGw)) {
                        gwViewHolder.smartGwListSelectIv.setVisibility(View.VISIBLE);
                    } else {
                        gwViewHolder.smartGwListSelectIv.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case 1:
                AddViewHolder addViewHolder = (AddViewHolder) viewHolder;
                break;
        }

    }

    @Override
    public void onClick(View view) {
        try {
            //清空原有数据
            SmartHomeAppLib.getInstance().getPreferences().edit()
                    .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, null)
                    .commit();
            SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,null).commit();
            Intent intent = new Intent(mContext, SettingIOTRadeyActivity.class);
            intent.putExtra(BundleKeyConstant.KEY_GW_IS_ADD,true);
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gwList == null ? 0 : gwList.size();
    }

    public void setOnGwItemClickListener(OnGwItemClick lis) {
        mListener = lis;
    }

    class GwViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_gw_list_select_iv)
        ImageView smartGwListSelectIv;
        @BindView(R.id.smart_gw_list_name_tv)
        TextView smartGwListNameTv;
        @BindView(R.id.smart_gw_list_item_rl)
        RelativeLayout smartGwListItemRl;

        GwViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_gw_list_item_rl)
        public void onClick() {
            GatewayInfo clicked = gwList.get(getPosition());
            if (null != selectedGw && clicked.equals(selectedGw)) {
                selectedGw = clicked;
                //smartGwListSelectIv.setVisibility(View.INVISIBLE);
            } else {
                selectedGw = clicked;
                //smartGwListSelectIv.setVisibility(View.VISIBLE);

            }
            if (mListener != null) {
                mListener.onGwItemClick(getPosition(), selectedGw);
            }
            notifyDataSetChanged();
        }

        @OnLongClick(R.id.smart_gw_list_item_rl)
        public boolean onLongClick(View view) {
            GatewayInfo clicked = gwList.get(getPosition());
            if(null != clicked && clicked.getMember_type() == UserRoleEnums.ADMIN.getCode()){
                if (mListener != null) {
                    mListener.onGwItemLongClick(getPosition(), clicked);
                }
            }
            return true;
        }
    }

     class AddViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.smart_gw_add)
        TextView smartGwAdd;

         AddViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
