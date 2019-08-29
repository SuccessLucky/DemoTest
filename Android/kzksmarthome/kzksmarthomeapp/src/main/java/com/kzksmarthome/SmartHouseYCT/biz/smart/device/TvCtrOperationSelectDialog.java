package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * @Title: TvCtrOperationSelectDialog
 * @Description: 电视控制操作选择Dialog
 * @author laixj
 * @date 2016/9/11 14:08
 * @version V1.0
 */
public class TvCtrOperationSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnOperationSelectItemClick {
        void onDeleteClick(int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo);
        void onEditClick(int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo);
        void onLearnClick(int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo);
    }

    private OnOperationSelectItemClick mListener;

    private int position;

    private DeviceInfo deviceInfo;

    private DeviceButtonInfo cmdInfo;

    private TvCtrOperationSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private TvCtrOperationSelectDialog(Context context, int defStyle, int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_tv_cmd_operation_select, null);

        contentView.findViewById(R.id.tv_operation_delete_rl).setOnClickListener(this);
        contentView.findViewById(R.id.tv_operation_edit_rl).setOnClickListener(this);
        contentView.findViewById(R.id.tv_operation_learn_rl).setOnClickListener(this);
        contentView.findViewById(R.id.tv_operation_cancel_rl).setOnClickListener(this);

        this.position = position;
        this.deviceInfo = deviceInfo;
        this.cmdInfo = cmdInfo;

        super.setContentView(contentView);
    }

    public TvCtrOperationSelectDialog(Context context, int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        this(context, R.style.tv_operation_select_dialog, position, deviceInfo, cmdInfo);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

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
            case R.id.tv_operation_delete_rl:
                if (mListener != null) {
                    mListener.onDeleteClick(position, deviceInfo, cmdInfo);
                }
                dismiss();
                break;
            case R.id.tv_operation_edit_rl:
                if (mListener != null) {
                    mListener.onEditClick(position, deviceInfo, cmdInfo);
                }
                dismiss();
                break;
            case R.id.tv_operation_learn_rl:
                if (mListener != null) {
                    mListener.onLearnClick(position, deviceInfo, cmdInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}