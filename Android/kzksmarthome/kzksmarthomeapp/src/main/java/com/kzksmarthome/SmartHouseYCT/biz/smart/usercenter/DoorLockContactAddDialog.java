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
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DoorAccessInfo;

/**
 * @Title: DoorLockContactAddDialog
 * @Description: 门锁联系人操作Dialog
 * @author laixj
 * @date 2016/10/31 21:33
 * @version V1.0
 */
public class DoorLockContactAddDialog extends Dialog implements View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, DoorAccessInfo contactInfo);
    }

    private OnEnsureClick mListener;

    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private DoorAccessInfo contactInfo;
    /**
     * 联系人名称
     */
    private EditText mEtvName;

    public EditText getmEtvId() {
        return mEtvId;
    }

    /**
     * 指纹ID
     */
    private EditText mEtvId;

    private Context context;

    private DoorLockContactAddDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private DoorLockContactAddDialog(Context context, int defStyle, int flag, int position, DoorAccessInfo contactInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_doorlock_contact_add, null);

        this.flag = flag;
        this.position = position;
        this.contactInfo = contactInfo;
        this.context = context;

        mEtvName = (EditText) contentView.findViewById(R.id.doorlock_contact_add_name_et);
        mEtvId = (EditText) contentView.findViewById(R.id.doorlock_contact_add_id_et);

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.doorlock_contact_add_title_tv)).setText(R.string.doorlock_contact_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.doorlock_contact_add_title_tv)).setText(R.string.doorlock_contact_edit_str);
        }

        if(flag == 1 && null != contactInfo){
            mEtvName.setText(contactInfo.getUser_name());
            mEtvId.setText(contactInfo.getLock_id());
        }

        contentView.findViewById(R.id.doorlock_contact_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.doorlock_contact_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public DoorLockContactAddDialog(Context context, int flag, int position, DoorAccessInfo contactInfo) {
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

    public DoorAccessInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(DoorAccessInfo contactInfo) {
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
            case R.id.doorlock_contact_add_ensure:
                if(TextUtils.isEmpty(mEtvName.getText().toString())){
                    SmartHomeApp.showToast(R.string.doorlock_username_hint_str);
                    return;
                }
                if(TextUtils.isEmpty(mEtvId.getText().toString())){
                    SmartHomeApp.showToast(R.string.doorlock_id_hint_str);
                    return;
                }
                if (mListener != null) {
                    contactInfo.setUser_name(mEtvName.getText().toString());
                    try {
                        contactInfo.setLock_id(Integer.parseInt(mEtvId.getText().toString()));
                    }catch (Exception e){

                    }
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