package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddUserResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DelUserResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetUserListResponse;
import com.kzksmarthome.SmartHouseYCT.util.UserRoleEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: RightsManageFragment
 * @Description: 权限管理界面
 * @date 2016/9/15 22:03
 */
public class RightsManageFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener,
        UserListAdapter.OnUserItemClick, UserAddDialog.OnEnsureClick, FamilyUserOperationSelectDialog.OnOperationSelectItemClick, FamilyUserDelConfirmDialog.OnEnsureClick {

    @BindView(R.id.smart_rights_manage_recycle)
    RecyclerView smartRightsManageRecycle;

    private UserListAdapter adapter;

    private List<FamilyMemberInfo> userList = new ArrayList<FamilyMemberInfo>();

    private PopupWindow roomPopWindow;

    private UserAddDialog addDialog = null;
    private UserAddDialog editDialog = null;
    private FamilyUserOperationSelectDialog selectDialog = null;
    private FamilyUserDelConfirmDialog delConfirmDialog = null;

    private int delPosition = -1;
    private int editPosition = -1;
    private FamilyMemberInfo addUser = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_rights_manage_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        //((TopNavSubActivity) getActivity()).getTopRightTV().setOnClickListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData(){
        com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        RestRequestApi.getFamilyUserList(getActivity(), userInfo.mobile, userInfo.password, "", this);
    }

    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(smartRightsManageRecycle.getContext());
        smartRightsManageRecycle.setLayoutManager(linearLayoutManager);
        adapter = new UserListAdapter(getActivity(), userList);
        adapter.setOnUserItemClickListener(this);
        smartRightsManageRecycle.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (null != addDialog) {
                addDialog.dismiss();
                addDialog = null;
            }
            if (null != editDialog) {
                editDialog.dismiss();
                editDialog = null;
            }
            if (null != selectDialog) {
                selectDialog.dismiss();
                selectDialog = null;
            }
            if (null != delConfirmDialog) {
                delConfirmDialog.dismiss();
                delConfirmDialog = null;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onRightBtnClick() {
        showAddDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_right_tv:
                // TODO: 2016/9/15
                showAddDialog();
                break;
            default:
                break;
        }
    }

    private void showAddDialog(){
        addDialog = new UserAddDialog(getActivity(), 0, -1, new FamilyMemberInfo());
        addDialog.setOnEnsureClickListener(RightsManageFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }

    private void showEditDialog(int position, FamilyMemberInfo userInfo){
        editDialog = new UserAddDialog(getActivity(), 1, position, userInfo);
        editDialog.setOnEnsureClickListener(RightsManageFragment.this);
        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.show();
    }

    @Override
    public void onEnsureClick(int flag, int position, FamilyMemberInfo userInfo) {
        Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
        Log.d("laixj", "添加/编辑：userInfo=" + userInfo.toString());
        if (flag == 0) {//添加
            // TODO: 2016/9/11
            //adapter.getUserList().add(userInfo);
            //adapter.notifyDataSetChanged();
            addUser = userInfo;
            showLoadingDialog(R.string.loading_str, false);
            RestRequestApi.addFamilyUser(getActivity(), userInfo.getAccount(), this);
        }
        /*else if (flag == 1) {//编辑
            // TODO: 2016/9/11
            //adapter.getUserList().set(position, userInfo);
            //adapter.notifyDataSetChanged();
            editPosition = position;
            showLoadingDialog(R.string.loading_str, false);
            RestRequestApi.addFloor(getActivity(), userInfo.getPhone(), this);
        }*/
    }

    @Override
    public void onUserItemClick(int position, FamilyMemberInfo userInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("userInfo", userInfo);
        PageSwitcher.switchToTopNavPage(getActivity(), RightsListFragment.class, bundle, "", getString(R.string.setting_smart_rights_list_str), "", -1, null, R.drawable.icon_add);
    }

    @Override
    public void onUserItemLongClick(int position, FamilyMemberInfo userInfo) {
        showOperatorSelectDialog(position, userInfo);
    }

    @Override
    public void onEnsureClick(int position, FamilyMemberInfo userInfo) {
        delPosition = position;
        if(null == userInfo){
            return;
        }
        showLoadingDialog(R.string.loading_str, false);
        RestRequestApi.delFamilyUser(getActivity(), userInfo.getMember_id(), this);
    }

    private void showOperatorSelectDialog(int position, FamilyMemberInfo userInfo){
        if(null == selectDialog){
            selectDialog = new FamilyUserOperationSelectDialog(getActivity(), position, userInfo);
        }else{
            selectDialog.setPosition(position);
            selectDialog.setUserInfo(userInfo);
        }
        selectDialog.setOnOperationSelectClickListener(this);
        selectDialog.setCancelable(true);
        selectDialog.setCanceledOnTouchOutside(true);
        selectDialog.show();
    }

    private void showDelConfirmDialog(int position, FamilyMemberInfo userInfo){
        if(null == delConfirmDialog){
            delConfirmDialog = new FamilyUserDelConfirmDialog(getActivity(), position, userInfo);
        }else{
            delConfirmDialog.setPosition(position);
            delConfirmDialog.setUserInfo(userInfo);
        }
        delConfirmDialog.setOnEnsureClickListener(this);
        delConfirmDialog.setCancelable(true);
        delConfirmDialog.setCanceledOnTouchOutside(true);
        delConfirmDialog.show();
    }

    @Override
    public void onDeleteClick(int position, FamilyMemberInfo userInfo) {
        showDelConfirmDialog(position, userInfo);
    }

    @Override
    public void onEditClick(int position, FamilyMemberInfo userInfo) {
        showEditDialog(position, userInfo);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_USER_LIST_URL)) {
            if (response != null) {
                GetUserListResponse param = (GetUserListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            List<FamilyMemberInfo> list = param.getResult();
                            for(FamilyMemberInfo user : list){
                                if(user.getMember_type() == UserRoleEnums.USER.getCode()){
                                    userList.add(user);
                                }
                            }
                            adapter.setUserList(userList);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                }
            }
        }else if (url.equals(ApiHost.DELETE_USER_URL)) {
            dismissLoadingDialog();
            if (response != null) {
                DelUserResponse param = (DelUserResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        SmartHomeApp.showToast("家庭成员删除成功");
                        userList.remove(delPosition);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                }
            }
        }else if (url.equals(ApiHost.ADD_USER_URL)) {
            dismissLoadingDialog();
            if (response != null) {
                AddUserResponse param = (AddUserResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        SmartHomeApp.showToast("家庭成员添加成功");
                        if(null != addUser){
                            addUser.setMember_id(param.getResult().getMember_id());
                            userList.add(addUser);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
    }
}
