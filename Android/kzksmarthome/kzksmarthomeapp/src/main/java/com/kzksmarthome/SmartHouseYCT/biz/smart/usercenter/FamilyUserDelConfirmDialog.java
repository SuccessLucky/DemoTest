package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;

/**
 * @author laixj
 * @version V1.0
 * @Title: FamilyUserDelConfirmDialog
 * @Description: 家庭成员删除确认框
 * @date 2016/10/18 8:03
 */
public class FamilyUserDelConfirmDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int position, FamilyMemberInfo userInfo);
    }

    private OnEnsureClick mListener;

    private int position;

    private FamilyMemberInfo userInfo;

    private TextView mTvTitle;

    private Context context;

    private FamilyUserDelConfirmDialog(Context context, boolean cancelable, OnCancelListener listener) {
        super(context, cancelable, listener);
    }

    private FamilyUserDelConfirmDialog(Context context, int defStyle, final int position, FamilyMemberInfo userInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_family_user_del_confirm, null);

        this.position = position;
        this.context = context;
        this.userInfo = userInfo;

        mTvTitle = (TextView) contentView.findViewById(R.id.user_del_confirm_title_tv);
        if (null != userInfo) {
            mTvTitle.setText(String.format(context.getString(R.string.family_user_del_title_str), userInfo.getMember_name()));
        }
        contentView.findViewById(R.id.user_del_confirm_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.user_del_confirm_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public FamilyUserDelConfirmDialog(Context context, int position, FamilyMemberInfo sceneInfo) {
        this(context, R.style.center_dialog, position, sceneInfo);
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FamilyMemberInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(FamilyMemberInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_del_confirm_ensure:
                if (mListener != null) {
                    mListener.onEnsureClick(getPosition(), userInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}