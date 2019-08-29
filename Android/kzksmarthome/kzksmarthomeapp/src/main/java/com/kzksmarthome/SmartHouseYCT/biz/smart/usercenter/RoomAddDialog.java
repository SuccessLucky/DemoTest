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
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;

/**
 * @Title: RoomAddDialog
 * @Description: 房间添加/编辑界面
 * @author laixj
 * @date 2016/9/17 21:01
 * @version V1.0
 */
public class RoomAddDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int flag, int position, RoomInfo roomInfo);
    }

    private OnEnsureClick mListener;
    
    /**
     * 0：添加 1：编辑
     */
    private int flag = 0;

    private int position;

    private RoomInfo roomInfo;
    /**
     * 房间名称
     */
    private EditText mEtvName;
    /**
     * 房间类型GridView
     */
    private GridView mRoomTypeGrid;
    
    private RoomAddDialogIconAdapter roomTypeAdapter;

    private Context context;

    private RoomAddDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private RoomAddDialog(Context context, int defStyle, int flag, final int position,
                          RoomInfo roomInfo, RoomAddDialogIconAdapter roomTypeAdapter) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_room_add, null);

        this.flag = flag;
        this.position = position;
        this.roomInfo = roomInfo;
        this.roomTypeAdapter = roomTypeAdapter;
        this.context = context;

        mEtvName = (EditText) contentView.findViewById(R.id.room_add_name_et);
        mRoomTypeGrid = (GridView) contentView.findViewById(R.id.room_add_type_grid);
        mRoomTypeGrid.setAdapter(roomTypeAdapter);
        mRoomTypeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO: 2016/9/17
                getRoomTypeAdapter().setSelected(position);
                getRoomTypeAdapter().notifyDataSetChanged();
            }
        });

        if(flag == 0){
            ((TextView)contentView.findViewById(R.id.room_add_title_tv)).setText(R.string.room_add_str);
        }else if(flag == 1){
            ((TextView)contentView.findViewById(R.id.room_add_title_tv)).setText(R.string.room_edit_str);
        }

        if(flag == 1 && null != roomInfo){
            mEtvName.setText(roomInfo.getName());
        }

        contentView.findViewById(R.id.room_add_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.room_add_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public RoomAddDialog(Context context, int flag, int position, RoomInfo roomInfo, RoomAddDialogIconAdapter roomTypeAdapter) {
        this(context, R.style.center_dialog, flag, position, roomInfo, roomTypeAdapter);
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

    public RoomAddDialogIconAdapter getRoomTypeAdapter() {
        return roomTypeAdapter;
    }

    public void setRoomTypeAdapter(RoomAddDialogIconAdapter roomTypeAdapter) {
        this.roomTypeAdapter = roomTypeAdapter;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.room_add_ensure:
                if(TextUtils.isEmpty(mEtvName.getText().toString())){
                    SmartHomeApp.showToast(context.getString(R.string.room_add_name_hint_str));
                    return;
                }
                if(getRoomTypeAdapter().getSelected() < 0){
                    SmartHomeApp.showToast(context.getString(R.string.room_add_type_hint_str));
                    return;
                }
                if (mListener != null) {
                    roomInfo.setName(mEtvName.getText().toString());
                    //RoomTypeInfo selected = getRoomTypeAdapter().getTypeList().get(position);
                    //roomInfo.setRoomType(selected.getType());
                    ImageInfo selected = getRoomTypeAdapter().getTypeList().get(getRoomTypeAdapter().getSelected());
                    roomInfo.setImage(selected.getName());
                    mListener.onEnsureClick(flag, getPosition(), roomInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}