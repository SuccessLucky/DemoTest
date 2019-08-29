package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;

/**
 * @Title: FamilyUserOperationSelectDialog
 * @Description: 家庭用户操作选择Dialog
 * @author laixj
 * @date 2016/9/11 14:08
 * @version V1.0
 */
public class FamilyUserOperationSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnOperationSelectItemClick {
        void onDeleteClick(int position, FamilyMemberInfo userInfo);
        void onEditClick(int position, FamilyMemberInfo userInfo);
    }

    private OnOperationSelectItemClick mListener;

    private int position;

    private FamilyMemberInfo userInfo;

    private FamilyUserOperationSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private FamilyUserOperationSelectDialog(Context context, int defStyle, int position, FamilyMemberInfo userInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_family_user_operation_select, null);

        //contentView.findViewById(R.id.user_operator_select_edit_rl).setOnClickListener(this);
        contentView.findViewById(R.id.user_operator_select_delete_rl).setOnClickListener(this);
        contentView.findViewById(R.id.user_operator_select_cancel_rl).setOnClickListener(this);

        this.position = position;
        this.userInfo = userInfo;

        super.setContentView(contentView);
    }

    public FamilyUserOperationSelectDialog(Context context, int position, FamilyMemberInfo userInfo) {
        this(context, R.style.center_dialog, position, userInfo);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.CENTER);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    public FamilyMemberInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(FamilyMemberInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOnOperationSelectClickListener(OnOperationSelectItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.user_operator_select_edit_rl:
                if (mListener != null) {
                    mListener.onEditClick(position, userInfo);
                }
                dismiss();
                break;*/
            case R.id.user_operator_select_delete_rl:
                if (mListener != null) {
                    mListener.onDeleteClick(position, userInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}