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
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

/**
 * @Title: FloorAddDialog
 * @Description: 楼层添加/编辑界面
 * @author laixj
 * @date 2016/9/16 19:33
 * @version V1.0
 */
public class FloorAddDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, FloorInfo floorInfo);
    }

    private OnEnsureClick mListener;

    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private FloorInfo floorInfo;
    /**
     * 楼层名称
     */
    private EditText mEtvName;

    private Context context;

    private FloorAddDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private FloorAddDialog(Context context, int defStyle, int flag, int position, FloorInfo floorInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_floor_add, null);

        this.flag = flag;
        this.position = position;
        this.floorInfo = floorInfo;
        this.context = context;

        mEtvName = (EditText) contentView.findViewById(R.id.floor_add_name_et);

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.floor_add_title_tv)).setText(R.string.floor_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.floor_add_title_tv)).setText(R.string.floor_edit_str);
        }

        if(flag == 1 && null != floorInfo){
            mEtvName.setText(floorInfo.getName());
            mEtvName.setSelection(mEtvName.getText().toString().length());
        }

        contentView.findViewById(R.id.floor_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.floor_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public FloorAddDialog(Context context, int flag, int position, FloorInfo floorInfo) {
        this(context, R.style.center_dialog, flag, position, floorInfo);
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
        ((TopNavSubActivity)context).hideKeyboardForCurrentFocus();
        switch (view.getId()){
            case R.id.floor_add_ensure:
                if(TextUtils.isEmpty(mEtvName.getText().toString())){
                    SmartHomeApp.showToast(context.getString(R.string.floor_add_name_hint_str));
                    return;
                }
                if (mListener != null) {
                    floorInfo.setName(mEtvName.getText().toString());
                    mListener.onEnsureClick(flag, position, floorInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }
    }
}