package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: SecurityManageFragment
 * @Description: 安防管理界面
 * @date 2016/9/15 22:03
 */
public class SecurityManageFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener,
        SecurityContactListAdapter.OnSecurityUserItemClick, SecurityContactAddDialog.OnEnsureClick {

    @BindView(R.id.smart_security_contact_recycle)
    RecyclerView smartSecurityContactRecycle;

    private SecurityContactListAdapter adapter;

    private List<SecurityContactInfo> contactList = new ArrayList<SecurityContactInfo>();

    private PopupWindow roomPopWindow;

    private SecurityContactAddDialog addDialog = null;

    private SecurityContactAddDialog editDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_security_manage_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData(){
        contactList.add(new SecurityContactInfo(1, "18909876754"));
        contactList.add(new SecurityContactInfo(2, "15709876098"));
        contactList.add(new SecurityContactInfo(3, "18678876754"));
    }

    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(smartSecurityContactRecycle.getContext());
        smartSecurityContactRecycle.setLayoutManager(linearLayoutManager);
        adapter = new SecurityContactListAdapter(getActivity(), contactList);
        adapter.setOnSecurityUserItemClickListener(this);
        smartSecurityContactRecycle.setAdapter(adapter);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {

    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {

    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (null != addDialog) {
                addDialog.dismiss();
            }
            if (null != editDialog) {
                editDialog.dismiss();
            }
        } catch (Exception e) {
        }
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

    @Override
    public void onRightBtnClick() {
        showAddDialog();
    }

    private void showAddDialog(){
        addDialog = new SecurityContactAddDialog(getActivity(), 0, -1, new SecurityContactInfo());
        addDialog.setOnEnsureClickListener(SecurityManageFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }

    private void showEditDialog(int position, SecurityContactInfo contactInfo){
        editDialog = new SecurityContactAddDialog(getActivity(), 1, position, contactInfo);
        editDialog.setOnEnsureClickListener(SecurityManageFragment.this);
        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.show();
    }

    @Override
    public void onSecurityUserItemClick(int position, SecurityContactInfo securityContactInfo) {
        showEditDialog(position, securityContactInfo);
    }

    @Override
    public void onSecurityUserItemLongClick(int position, SecurityContactInfo securityContactInfo) {
        showToast("长按");
    }

    @Override
    public void onEnsureClick(int flag, int position, SecurityContactInfo contactInfo) {
        Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
        Log.d("laixj", "添加/编辑：contactInfo=" + contactInfo.toString());
        if (flag == 0) {//添加
            // TODO: 2016/9/11
            adapter.getSecurityUserList().add(contactInfo);
            adapter.notifyDataSetChanged();
        } else if (flag == 1) {//编辑
            // TODO: 2016/9/11
            adapter.getSecurityUserList().set(position, contactInfo);
            adapter.notifyDataSetChanged();
        }
    }
}
