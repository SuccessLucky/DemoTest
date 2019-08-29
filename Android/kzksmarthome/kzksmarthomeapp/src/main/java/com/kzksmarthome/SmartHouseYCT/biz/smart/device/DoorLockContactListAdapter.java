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
import butterknife.OnLongClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: DoorLockContactListAdapter
 * @Description: 门锁联系人列表适配器
 * @date 2016/10/31 20:25
 */
public class DoorLockContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DoorAccessInfo> accessList = new ArrayList<DoorAccessInfo>();

    public interface OnDoorLockContactItemClick {
        void onDoorLockContactItemClick(int position, DoorAccessInfo doorAccessInfo);
        void onDoorLockContactItemLongClick(int position, DoorAccessInfo doorAccessInfo);
    }

    private OnDoorLockContactItemClick mListener;

    public DoorLockContactListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public DoorLockContactListAdapter(Context context, List<DoorAccessInfo> userList) {
        this(context);
        this.accessList = userList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_doorlock_contact_list_item, viewGroup, false);
        DoorLockContactViewHolder viewHolder = new DoorLockContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DoorLockContactViewHolder accessViewHolder = (DoorLockContactViewHolder) viewHolder;
        DoorAccessInfo accessInfo = accessList.get(position);
        if (null != accessInfo) {
            accessViewHolder.smartDoorlockContactListNameTv.setText(accessInfo.getUser_name());
            accessViewHolder.smartDoorlockContactListAccessTv.setText(String.format(mContext.getString(R.string.door_access_count), accessInfo.getUnlock_times()));
        }
    }

    @Override
    public int getItemCount() {
        return accessList == null ? 0 : accessList.size();
    }

    public List<DoorAccessInfo> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<DoorAccessInfo> accessList) {
        this.accessList = accessList;
    }

    public void setOnDoorLockContactItemClick(OnDoorLockContactItemClick lis) {
        mListener = lis;
    }

    class DoorLockContactViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.smart_doorlock_contact_list_name_tv)
        TextView smartDoorlockContactListNameTv;
        @BindView(R.id.smart_doorlock_contact_list_access_tv)
        TextView smartDoorlockContactListAccessTv;
        @BindView(R.id.smart_doorlock_contact_list_item_rl)
        RelativeLayout smartDoorlockContactListItemRl;

        DoorLockContactViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_doorlock_contact_list_item_rl)
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onDoorLockContactItemClick(getPosition(), accessList.get(getPosition()));
            }
        }

        @OnLongClick(R.id.smart_doorlock_contact_list_item_rl)
        public boolean onLongClick(View view) {
            if (mListener != null) {
                mListener.onDoorLockContactItemLongClick(getPosition(), accessList.get(getPosition()));
            }
            return true;
        }
    }
}
