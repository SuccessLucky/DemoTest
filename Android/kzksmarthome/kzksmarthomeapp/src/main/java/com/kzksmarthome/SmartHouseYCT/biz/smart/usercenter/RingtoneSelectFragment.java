package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultRingtoneSelect;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: RingtoneSelectFragment
 * @Description: 铃声选择界面
 * @date 2016/9/15 16:34
 */
public class RingtoneSelectFragment extends BaseRequestFragment implements RingtoneListAdapter.OnRingtoneItemClick {

    public static final int REQUEST_CODE_RINGTONE_SELECT = 102;

    @BindView(R.id.smart_ringtone_select_recycle)
    RecyclerView smartRingtoneSelectRecycle;

    private List<RingtoneInfo> ringtoneList = new ArrayList<RingtoneInfo>();

    private RingtoneListAdapter adapter = null;

    /**
     * 铃声类型  alarm、keypad、offline
     */
    private String ringtoneType;

    /**
     * 选中的铃声
     */
    private RingtoneInfo selectedRingtone = null;

    @Override
    public void onRightBtnClick() {
        Log.d("laixj", "设置铃声sureSelect--"+selectedRingtone.toString());
        Log.d("laixj", "设置铃声sureSelect--"+ringtoneType);
        EventOfResultRingtoneSelect event = new EventOfResultRingtoneSelect();
        event.ringtoneInfo = selectedRingtone;
        event.type = ringtoneType;
        GjjEventBus.getInstance().post(event);
        super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_ringtone_switch_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        ringtoneType = bundle.getString("type");
        selectedRingtone = bundle.getParcelable("ringtone");
        ringtoneList = getRingtoneList(RingtoneManager.TYPE_RINGTONE);
    }

    private void initViews() {
        smartRingtoneSelectRecycle.setLayoutManager(new LinearLayoutManager(smartRingtoneSelectRecycle.getContext()));
        adapter = new RingtoneListAdapter(getActivity(), ringtoneList, selectedRingtone);
        adapter.setOnRingtoneItemClickListener(this);
        smartRingtoneSelectRecycle.setAdapter(adapter);
    }

    public List<RingtoneInfo> getRingtoneList(int type) {
        List<RingtoneInfo> result = new ArrayList<RingtoneInfo>();
        RingtoneManager manager = new RingtoneManager(getActivity());
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        if (cursor.moveToFirst()) {
            do {
                result.add(new RingtoneInfo(cursor.getInt(RingtoneManager.ID_COLUMN_INDEX), cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX), cursor.getString(RingtoneManager.URI_COLUMN_INDEX)));
            } while (cursor.moveToNext());
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRingtoneItemClick(int position, RingtoneInfo ringtoneInfo) {
        selectedRingtone = ringtoneInfo;
    }
}
