package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.WarningDateAdapter;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.WarningDateDialog;
import com.kzksmarthome.SmartHouseYCT.biz.widget.datepicker.DateUtils;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.squareup.okhttp.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * 告警界面
 * Created by jack on 2016/10/29.
 */

public class WarningFragment extends BaseRequestFragment implements RequestCallback, WarningDateDialog.DateSelectClick, WarningDateAdapter.DateItemClick {
    @BindView(R.id.smart_warning_date)
    TextView smartWarningDate;
    @BindView(R.id.smart_warning_date_recyclerView)
    RecyclerView smartWarningDateRecyclerView;
    @BindView(R.id.smart_warning_info_recyclerView)
    RecyclerView smartWarningInfoRecyclerView;
    ArrayList<String> dateList = new ArrayList<String>(6);
    private WarningDateDialog mWarningDateDialog;
    /**
     * 告警时间适配器
     */
    private WarningDateAdapter mWarningDateAdapter;
    /**
     * 告警信息适配器
     */
    private WarningInfoAdapter mWarningInfoAdapter;
    /**
     * 当前时间
     */
    private String mNowDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_warning_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        smartWarningDateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        smartWarningInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWarningDateAdapter = new WarningDateAdapter(getActivity(), this);
        mWarningInfoAdapter = new WarningInfoAdapter(getActivity());
        smartWarningDateRecyclerView.setAdapter(mWarningDateAdapter);
        smartWarningInfoRecyclerView.setAdapter(mWarningInfoAdapter);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        mNowDate = simpleDateFormat.format(date);
        smartWarningDate.setText(mNowDate.replaceAll("-", "\\/"));
        initData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        setDateList(mNowDate);
        mWarningDateAdapter.setmSelectPosition(0);
        RestRequestApi.getWarningInfo(getActivity(), mNowDate, this);
    }


    /**
     * 设置时间
     *
     * @param date
     */
    private void setDateList(String date) {
        if (date != null) {
            String[] dateArray = date.split("-");
            if (dateArray != null && dateArray.length > 2) {
                String year = dateArray[0];
                String month = dateArray[1];
                String day = dateArray[2];
                int day_int = 0;
                if (day != null && ((Integer.valueOf(day) - 4) > 0)) {
                    day_int = Integer.valueOf(day);
                    for (int i = 0; i < 5; i++) {
                        dateList.add(year + "-" + month + "-" + (day_int - i));
                    }
                } else {
                    day_int = Integer.valueOf(day);
                    int count = 0;
                    for (int i = 0; i < 5; i++) {
                        if (day_int - i > 0) {
                            dateList.add(year + "-" + month + "-" + (day_int - i));
                        } else {
                            int month_int = Integer.valueOf(month);
                            int monthDay = DateUtils.getMonthDays(Integer.valueOf(year), (month_int - 1));
                            dateList.add(year + "-" + (month_int - 1) + "-" + (monthDay - count++));
                        }
                    }
                }
            }
        }
        dateList.add("more");
        mWarningDateAdapter.setData(dateList);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        showToast(R.string.loading_fail);
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        WarningInfoResponse warningInfoResponse = (WarningInfoResponse) response.body;
        if (warningInfoResponse != null) {
            if (warningInfoResponse.isSuccess()) {
                ArrayList<WarningInfoBean> dataList = new ArrayList<WarningInfoBean>(warningInfoResponse.getResult());
                mWarningInfoAdapter.setData(dataList);
            }
        }

    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        showToast(R.string.loading_fail);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showDateDialog() {
        mWarningDateDialog = new WarningDateDialog(getActivity());
        mWarningDateDialog.setDateSelectClickListener(this);
        mWarningDateDialog.setCancelable(true);
        mWarningDateDialog.setCanceledOnTouchOutside(true);
        mWarningDateDialog.show();
    }

    @Override
    public void onDateSelectClick(String date) {
        dateList.clear();
        setDateList(date);
        mWarningDateAdapter.setmSelectPosition(0);
        RestRequestApi.getWarningInfo(getActivity(), date, this);
    }

    @Override
    public void onDateItemClick(int position) {
        if (dateList != null) {
            if (dateList.size() == (position + 1)) {
                showDateDialog();
            } else {
                mWarningDateAdapter.setmSelectPosition(position);
                RestRequestApi.getWarningInfo(getActivity(), dateList.get(position), this);
            }
        }
    }
}
