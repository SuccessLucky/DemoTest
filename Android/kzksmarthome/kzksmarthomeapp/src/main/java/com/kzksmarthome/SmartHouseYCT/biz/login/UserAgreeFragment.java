package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.BindView;

public class UserAgreeFragment extends BaseFragment {

    @BindView(R.id.agree_tv)
    TextView mAgreeContentTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_user_agree, container, false);
        ButterKnife.bind(this, mRootView);
        mAgreeContentTV.setMovementMethod(ScrollingMovementMethod.getInstance());
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
