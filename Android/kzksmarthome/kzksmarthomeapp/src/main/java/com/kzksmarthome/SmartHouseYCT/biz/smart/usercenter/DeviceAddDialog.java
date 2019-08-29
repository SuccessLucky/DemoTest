package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;

/**
 * @Title: DeviceAddDialog
 * @Description: 设备添加/编辑界面
 * @author laixj
 * @date 2016/9/17 21:00
 * @version V1.0
 */
public class DeviceAddDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, DeviceInfo deviceInfo);
    }

    private OnEnsureClick mListener;
    
    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private DeviceInfo deviceInfo;
    /**
     * 设备名称
     */
    private EditText mEtvName;
    /**
     * 设备类型GridView
     */
    private GridView mDeviceTypeGrid;
    
    private DeviceAddDialogIconAdapter deviceTypeAdapter;

    private Context context;

    private DeviceAddDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private DeviceAddDialog(Context context, int defStyle, int flag, final int position,
                            DeviceInfo deviceInfo, DeviceAddDialogIconAdapter deviceTypeAdapter) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_device_add, null);

        this.flag = flag;
        this.position = position;
        this.deviceInfo = deviceInfo;
        this.deviceTypeAdapter = deviceTypeAdapter;
        this.context = context;

        mEtvName = (EditText) contentView.findViewById(R.id.device_add_name_et);
        mDeviceTypeGrid = (GridView) contentView.findViewById(R.id.device_add_type_grid);
        mDeviceTypeGrid.setAdapter(deviceTypeAdapter);
        mDeviceTypeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO: 2016/9/17
                getDeviceTypeAdapter().setSelected(position);
                getDeviceTypeAdapter().notifyDataSetChanged();
            }
        });

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.device_add_title_tv)).setText(R.string.device_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.device_add_title_tv)).setText(R.string.device_edit_str);
        }

        if(flag == 1 && null != deviceInfo){
            mEtvName.setText(deviceInfo.getDevice_name());
        }

        contentView.findViewById(R.id.device_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.device_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public DeviceAddDialog(Context context, int flag, int position, DeviceInfo deviceInfo, DeviceAddDialogIconAdapter deviceTypeAdapter) {
        this(context, R.style.center_dialog, flag, position, deviceInfo, deviceTypeAdapter);
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

    public DeviceAddDialogIconAdapter getDeviceTypeAdapter() {
        return deviceTypeAdapter;
    }

    public void setDeviceTypeAdapter(DeviceAddDialogIconAdapter deviceTypeAdapter) {
        this.deviceTypeAdapter = deviceTypeAdapter;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.device_add_ensure:
                if(TextUtils.isEmpty(mEtvName.getText().toString())){
                    SmartHomeApp.showToast(context.getString(R.string.device_add_name_hint_str));
                    return;
                }
                if(getDeviceTypeAdapter().getSelected() < 0){
                    SmartHomeApp.showToast(context.getString(R.string.device_add_type_hint_str));
                    return;
                }
                if (mListener != null) {
                    deviceInfo.setDevice_name(mEtvName.getText().toString());
                    //DeviceTypeInfo selected = getDeviceTypeAdapter().getTypeList().get(position);
                    //deviceInfo.setDeviceType(selected.getType());
                    int selectedNum = getDeviceTypeAdapter().getSelected();
                    int typeSize = getDeviceTypeAdapter().getTypeList().size();
                    if(selectedNum < typeSize) {
                        ImageInfo selected = getDeviceTypeAdapter().getTypeList().get(selectedNum);
                        //deviceInfo.setDeviceType(selected.getCode());
                        deviceInfo.setImage(selected.getName());
                        mListener.onEnsureClick(flag, getPosition(), deviceInfo);
                    }
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}