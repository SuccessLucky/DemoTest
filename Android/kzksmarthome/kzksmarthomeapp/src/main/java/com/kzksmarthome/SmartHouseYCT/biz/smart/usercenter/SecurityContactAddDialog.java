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

/**
 * @Title: SecurityContactAddDialog
 * @Description: 安防联系人操作Dialog
 * @author laixj
 * @date 2016/9/15 22:18
 * @version V1.0
 */
public class SecurityContactAddDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, SecurityContactInfo contactInfo);
    }

    private OnEnsureClick mListener;

    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private SecurityContactInfo contactInfo;
    /**
     * 联系电话
     */
    private EditText mEtvPhone;

    private Context context;

    private SecurityContactAddDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private SecurityContactAddDialog(Context context, int defStyle, int flag, int position, SecurityContactInfo contactInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_security_contact_add, null);

        this.flag = flag;
        this.position = position;
        this.contactInfo = contactInfo;
        this.context = context;

        mEtvPhone = (EditText) contentView.findViewById(R.id.security_contact_add_et);

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.security_contact_add_title_tv)).setText(R.string.security_contact_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.security_contact_add_title_tv)).setText(R.string.security_contact_edit_str);
        }

        if(flag == 1 && null != contactInfo){
            mEtvPhone.setText(contactInfo.getPhone());
        }

        contentView.findViewById(R.id.security_contact_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.security_contact_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public SecurityContactAddDialog(Context context, int flag, int position, SecurityContactInfo contactInfo) {
        this(context, R.style.center_dialog, flag, position, contactInfo);
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

    public SecurityContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(SecurityContactInfo contactInfo) {
        this.contactInfo = contactInfo;
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
            case R.id.security_contact_add_ensure:
                if(TextUtils.isEmpty(mEtvPhone.getText().toString())){
                    SmartHomeApp.showToast("请输入安防联系电话");
                    return;
                }
                if (mListener != null) {
                    contactInfo.setPhone(mEtvPhone.getText().toString());
                    mListener.onEnsureClick(flag, position, contactInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}