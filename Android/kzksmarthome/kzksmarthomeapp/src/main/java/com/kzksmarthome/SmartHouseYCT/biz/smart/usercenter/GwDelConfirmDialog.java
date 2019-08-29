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
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;

/**
 * @author laixj
 * @version V1.0
 * @Title: FamilyUserDelConfirmDialog
 * @Description: 网关删除确认框
 * @date 2016/10/18 8:03
 */
public class GwDelConfirmDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int position, GatewayInfo gwInfo);
    }

    private OnEnsureClick mListener;

    private int position;

    private GatewayInfo gwInfo;

    private TextView mTvTitle;

    private Context context;

    private GwDelConfirmDialog(Context context, boolean cancelable, OnCancelListener listener) {
        super(context, cancelable, listener);
    }

    private GwDelConfirmDialog(Context context, int defStyle, final int position, GatewayInfo gwInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_family_user_del_confirm, null);

        this.position = position;
        this.context = context;
        this.gwInfo = gwInfo;

        mTvTitle = (TextView) contentView.findViewById(R.id.user_del_confirm_title_tv);
        if (null != gwInfo) {
            mTvTitle.setText(context.getString(R.string.gw_del_title_str));
        }
        contentView.findViewById(R.id.user_del_confirm_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.user_del_confirm_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public GwDelConfirmDialog(Context context, int position, GatewayInfo gwInfo) {
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public GatewayInfo getGwInfo() {
        return gwInfo;
    }

    public void setGwInfo(GatewayInfo gwInfo) {
        this.gwInfo = gwInfo;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_del_confirm_ensure:
                if (mListener != null) {
                    mListener.onEnsureClick(getPosition(), gwInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}