package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;

/**
 * @Title: FamilyUserOperationSelectDialog
 * @Description: 家庭用户操作选择Dialog
 * @author laixj
 * @date 2016/9/11 14:08
 * @version V1.0
 */
public class GwOperationSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnOperationSelectItemClick {
        void onDeleteClick(int position, GatewayInfo gwInfo);
        void onEditClick(int position, GatewayInfo gwInfo);
    }

    private OnOperationSelectItemClick mListener;

    private int position;

    private GatewayInfo gwInfo;

    private GwOperationSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private GwOperationSelectDialog(Context context, int defStyle, int position, GatewayInfo gwInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_gateway_operation_select, null);

        //contentView.findViewById(R.id.gw_operator_select_edit_rl).setOnClickListener(this);
        contentView.findViewById(R.id.gw_operator_select_delete_rl).setOnClickListener(this);
        contentView.findViewById(R.id.gw_operator_select_cancel_rl).setOnClickListener(this);

        this.position = position;
        this.gwInfo = gwInfo;

        super.setContentView(contentView);
    }

    public GwOperationSelectDialog(Context context, int position, GatewayInfo gwInfo) {
        this(context, R.style.center_dialog, position, gwInfo);
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

    public GatewayInfo getGwInfo() {
        return gwInfo;
    }

    public void setGwInfo(GatewayInfo gwInfo) {
        this.gwInfo = gwInfo;
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
            /*case R.id.gw_operator_select_edit_rl:
                if (mListener != null) {
                    mListener.onEditClick(position, userInfo);
                }
                dismiss();
                break;*/
            case R.id.gw_operator_select_delete_rl:
                if (mListener != null) {
                    mListener.onDeleteClick(position, gwInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}