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
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;
import com.kzksmarthome.SmartHouseYCT.util.UserRoleEnums;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: UserListAdapter
 * @Description: 用户列表适配器
 * @date 2016/9/14 14:38
 */
public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FamilyMemberInfo> userList = new ArrayList<FamilyMemberInfo>();

    public interface OnUserItemClick {
        void onUserItemClick(int position, FamilyMemberInfo UserInfo);

        void onUserItemLongClick(int position, FamilyMemberInfo UserInfo);
    }

    private OnUserItemClick mListener;

    public UserListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public UserListAdapter(Context context, List<FamilyMemberInfo> userList) {
        this(context);
        this.userList = userList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_user_list_item, viewGroup, false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        UserViewHolder userViewHolder = (UserViewHolder) viewHolder;
        FamilyMemberInfo userInfo = userList.get(position);
        if (null != userInfo) {
            userViewHolder.smartUserListNameTv.setText(userInfo.getMember_name());
            userViewHolder.smartUserListPhoneTv.setText(userInfo.getAccount());
            if (UserRoleEnums.ADMIN.getCode() == userInfo.getMember_type()) {
                // TODO: 2016/9/14
            } else if (UserRoleEnums.USER.getCode() == userInfo.getMember_type()) {
                // TODO: 2016/9/14
            }
            userViewHolder.smartUserListHeadIv.setImageResource(R.drawable.smart_setting_head);
        }
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public List<FamilyMemberInfo> getUserList() {
        return userList;
    }

    public void setUserList(List<FamilyMemberInfo> UserList) {
        this.userList = UserList;
    }

    public void setOnUserItemClickListener(OnUserItemClick lis) {
        mListener = lis;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_user_list_head_iv)
        ImageView smartUserListHeadIv;
        @BindView(R.id.smart_user_list_role_iv)
        ImageView smartUserListRoleIv;
        @BindView(R.id.smart_user_list_name_tv)
        TextView smartUserListNameTv;
        @BindView(R.id.smart_user_list_phone_tv)
        TextView smartUserListPhoneTv;
        @BindView(R.id.smart_user_list_item_rl)
        RelativeLayout smartUserListItemRl;

        UserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_user_list_item_rl)
        public void onClick() {
            if (mListener != null) {
                mListener.onUserItemClick(getPosition(), userList.get(getPosition()));
            }
        }

        @OnLongClick(R.id.smart_user_list_item_rl)
        public boolean onLongClick(View view) {
            if (mListener != null) {
                mListener.onUserItemLongClick(getPosition(), userList.get(getPosition()));
            }
            return true;
        }
    }
}
