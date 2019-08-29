package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

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
 * @Title: SecurityContactListAdapter
 * @Description: 安防联系人列表适配器
 * @date 2016/9/15 21:40
 */
public class SecurityContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SecurityContactInfo> userList = new ArrayList<SecurityContactInfo>();

    @OnClick(R.id.smart_security_contact_list_item_rl)
    public void onClick() {
    }

    public interface OnSecurityUserItemClick {
        void onSecurityUserItemClick(int position, SecurityContactInfo securityContactInfo);
        void onSecurityUserItemLongClick(int position, SecurityContactInfo securityContactInfo);
    }

    private OnSecurityUserItemClick mListener;

    public SecurityContactListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public SecurityContactListAdapter(Context context, List<SecurityContactInfo> userList) {
        this(context);
        this.userList = userList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_security_contact_list_item, viewGroup, false);
        SecurityUserViewHolder viewHolder = new SecurityUserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SecurityUserViewHolder userViewHolder = (SecurityUserViewHolder) viewHolder;
        SecurityContactInfo userInfo = userList.get(position);
        if (null != userInfo) {
            userViewHolder.smartSecurityContactListNameTv.setText(userInfo.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public List<SecurityContactInfo> getSecurityUserList() {
        return userList;
    }

    public void setSecurityUserList(List<SecurityContactInfo> SecurityUserList) {
        this.userList = SecurityUserList;
    }

    public void setOnSecurityUserItemClickListener(OnSecurityUserItemClick lis) {
        mListener = lis;
    }

    class SecurityUserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_security_contact_list_name_tv)
        TextView smartSecurityContactListNameTv;
        @BindView(R.id.smart_security_contact_list_item_rl)
        RelativeLayout smartSecurityContactListItemRl;

        SecurityUserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_security_contact_list_item_rl)
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onSecurityUserItemClick(getPosition(), userList.get(getPosition()));
            }
        }

        @OnLongClick(R.id.smart_security_contact_list_item_rl)
        public boolean onLongClick(View view) {
            if (mListener != null) {
                mListener.onSecurityUserItemLongClick(getPosition(), userList.get(getPosition()));
            }
            return true;
        }
    }
}
