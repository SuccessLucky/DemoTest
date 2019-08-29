package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

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
 * @Title: TvCtrOperationSelectDialog
 * @Description: 电视控制操作选择Dialog
 * @author laixj
 * @date 2016/9/11 14:08
 * @version V1.0
 */
public class TvCmdOperationDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo);
    }

    private OnEnsureClick mListener;

    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private DeviceInfo deviceInfo;

    private DeviceButtonInfo cmdInfo;
    /**
     * 按键名称
     */
    private EditText mEtvCmdName;

    private Context context;

    private TvCmdOperationDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private TvCmdOperationDialog(Context context, int defStyle, int flag, int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_tv_cmd_add, null);

        this.flag = flag;
        this.position = position;
        this.deviceInfo = deviceInfo;
        this.cmdInfo = cmdInfo;
        this.context = context;

        mEtvCmdName = (EditText) contentView.findViewById(R.id.tv_cmd_add_et);

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.tv_cmd_add_title_tv)).setText(R.string.cmd_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.tv_cmd_add_title_tv)).setText(R.string.cmd_edit_str);
        }
        if(flag == 1 && null != cmdInfo){
            mEtvCmdName.setText(cmdInfo.getName());
        }

        contentView.findViewById(R.id.tv_cmd_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.tv_cmd_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public TvCmdOperationDialog(Context context, int flag, int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        this(context, R.style.center_dialog, flag, position, deviceInfo, cmdInfo);//center_dialog
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

    public DeviceButtonInfo getCmdInfo() {
        return cmdInfo;
    }

    public void setCmdInfo(DeviceButtonInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
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
            case R.id.tv_cmd_add_ensure:
                if(TextUtils.isEmpty(mEtvCmdName.getText().toString())){
                    SmartHomeApp.showToast("请输入按键名");
                    return;
                }
                if (mListener != null) {
                    cmdInfo.setName(mEtvCmdName.getText().toString());
                    mListener.onEnsureClick(flag, position, deviceInfo, cmdInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}