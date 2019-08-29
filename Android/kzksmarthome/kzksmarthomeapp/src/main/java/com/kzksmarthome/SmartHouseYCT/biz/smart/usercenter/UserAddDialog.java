package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.TopNavSubActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;

/**
 * @Title: UserAddDialog
 * @Description: 用户添加/编辑界面
 * @author laixj
 * @date 2016/9/16 7:15
 * @version V1.0
 */
public class UserAddDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, FamilyMemberInfo userInfo);
    }

    private OnEnsureClick mListener;

    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private FamilyMemberInfo userInfo;
    /**
     * 姓名
     */
    private EditText mEtvName;
    /**
     * 联系电话
     */
    private EditText mEtvPhone;
    /**
     * 账号密码
     */
    //private EditText mEtvPwd;

    private Context context;

    private UserAddDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private UserAddDialog(Context context, int defStyle, int flag, int position, FamilyMemberInfo userInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_user_add, null);

        this.flag = flag;
        this.position = position;
        this.userInfo = userInfo;
        this.context = context;

        //mEtvName = (EditText) contentView.findViewById(R.id.user_add_name_et);
        mEtvPhone = (EditText) contentView.findViewById(R.id.user_add_phone_et);
        //mEtvPwd = (EditText) contentView.findViewById(R.id.user_add_pwd_et);

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.user_add_title_tv)).setText(R.string.user_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.user_add_title_tv)).setText(R.string.user_edit_str);
        }

        if(flag == 1 && null != userInfo){
            mEtvName.setText(userInfo.getMember_name());
            //mEtvPhone.setText(userInfo.getM);
            //mEtvPwd.setText(userInfo.getUserPwd());
        }

        contentView.findViewById(R.id.user_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.user_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public UserAddDialog(Context context, int flag, int position, FamilyMemberInfo userInfo) {
        this(context, R.style.center_dialog, flag, position, userInfo);
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        ((TopNavSubActivity)context).hideKeyboardForCurrentFocus();
        switch (view.getId()){
            case R.id.user_add_ensure:
                /*if(TextUtils.isEmpty(mEtvName.getText().toString())){
                    SmartHomeApp.showToast(context.getString(R.string.user_name_hint_str));
                    return;
                }*/
                if(TextUtils.isEmpty(mEtvPhone.getText().toString())){
                    SmartHomeApp.showToast(context.getString(R.string.user_phone_hint_str));
                    return;
                }
                /*if(TextUtils.isEmpty(mEtvPwd.getText().toString())){
                    SmartHomeApp.showToast(context.getString(R.string.user_pwd_hint_str));
                    return;
                }*/
                if (mListener != null) {
                    //userInfo.setMember_name(mEtvName.getText().toString());
                    userInfo.setAccount(mEtvPhone.getText().toString());
                    //userInfo.setUserPwd(mEtvPwd.getText().toString());
                    mListener.onEnsureClick(flag, position, userInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}