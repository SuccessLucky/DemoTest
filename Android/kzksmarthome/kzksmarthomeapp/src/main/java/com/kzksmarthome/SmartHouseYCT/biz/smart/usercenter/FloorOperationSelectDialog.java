package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

/**
 * @Title: FloorOperationSelectDialog
 * @Description: 楼层操作选择Dialog
 * @author laixj
 * @date 2016/9/11 14:08
 * @version V1.0
 */
public class FloorOperationSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnOperationSelectItemClick {
        void onDeleteClick(int position, FloorInfo floorInfo);
        void onEditClick(int position, FloorInfo floorInfo);
    }

    private OnOperationSelectItemClick mListener;

    private int position;

    private FloorInfo floorInfo;

    private FloorOperationSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private FloorOperationSelectDialog(Context context, int defStyle, int position, FloorInfo floorInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_floor_operation_select, null);

        contentView.findViewById(R.id.floor_operator_select_edit_rl).setOnClickListener(this);
        contentView.findViewById(R.id.floor_operator_select_delete_rl).setOnClickListener(this);
        contentView.findViewById(R.id.floor_operator_select_cancel_rl).setOnClickListener(this);

        this.position = position;
        this.floorInfo = floorInfo;

        super.setContentView(contentView);
    }

    public FloorOperationSelectDialog(Context context, int position, FloorInfo floorInfo) {
        this(context, R.style.center_dialog, position, floorInfo);
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

    public FloorInfo getFloorInfo() {
        return floorInfo;
    }

    public void setFloorInfo(FloorInfo floorInfo) {
        this.floorInfo = floorInfo;
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
            case R.id.floor_operator_select_edit_rl:
                if (mListener != null) {
                    mListener.onEditClick(position, floorInfo);
                }
                dismiss();
                break;
            case R.id.floor_operator_select_delete_rl:
                if (mListener != null) {
                    mListener.onDeleteClick(position, floorInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}