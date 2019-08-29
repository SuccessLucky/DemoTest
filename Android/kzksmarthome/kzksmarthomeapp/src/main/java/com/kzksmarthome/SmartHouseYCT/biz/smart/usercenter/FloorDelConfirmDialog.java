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
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

/**
 * @author laixj
 * @version V1.0
 * @Title: FloorDelConfirmDialog
 * @Description: 楼层删除确认框
 * @date 2016/10/18 8:03
 */
public class FloorDelConfirmDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int position, FloorInfo floorInfo);
    }

    private OnEnsureClick mListener;

    private int position;

    private FloorInfo floorInfo;

    private TextView mTvTitle;

    private Context context;

    private FloorDelConfirmDialog(Context context, boolean cancelable, OnCancelListener listener) {
        super(context, cancelable, listener);
    }

    private FloorDelConfirmDialog(Context context, int defStyle, final int position, FloorInfo floorInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_family_user_del_confirm, null);

        this.position = position;
        this.context = context;
        this.floorInfo = floorInfo;

        mTvTitle = (TextView) contentView.findViewById(R.id.user_del_confirm_title_tv);
        if (null != floorInfo) {
            mTvTitle.setText(context.getString(R.string.floor_del_title_str));
        }
        contentView.findViewById(R.id.user_del_confirm_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.user_del_confirm_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public FloorDelConfirmDialog(Context context, int position, FloorInfo floorInfo) {
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FloorInfo getFloorInfo() {
        return floorInfo;
    }

    public void setFloorInfo(FloorInfo floorInfo) {
        this.floorInfo = floorInfo;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_del_confirm_ensure:
                if (mListener != null) {
                    mListener.onEnsureClick(getPosition(), floorInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}