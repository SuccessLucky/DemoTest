package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.Util;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Chuck on 2015/12/22.
 */
public class FirstFindPasswordFragment extends BaseRequestFragment implements RequestCallback{

    @BindView(R.id.register_number)
    EditText mRegisterNameET;

    @BindView(R.id.forget_num_tip)
    TextView mFindPswTip;
    @OnClick(R.id.next_step)
    void onNextStep() {
        mRegisterName = mRegisterNameET.getText().toString();
        if (!Util.isMobileNO(mRegisterName)) {
            showToast(R.string.mobile_error);
            return;
        }
    /*    Request req = GjjRequestFactory.applyRetrieve(IdType.ID_TYPE_MOBILE.getValue(), mRegisterName);
        GjjRequestManager.getInstance().execute(req, this);*/
    }

    private String mRegisterName;// mobile

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_find_psw_fisrt, container, false);
        ButterKnife.bind(this, mRootView);
        initView();

        Bundle arguments = getArguments();
        String name = arguments.getString(BundleKeyConstant.KEY_REGISTER_NAME);
        if(name != null) {
            if(Util.isMobileNO(name)) {
                mRegisterNameET.setText(name);
            } else if (Util.isEmail(name)) {
//                GjjApp.showToast(R.string.input_register_number);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    private void initView() {
        SpannableString spannableInfo = new SpannableString(getString(R.string.forget_number_tip));
        ClickableSpan clickable = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Util.askForMakeCall(getActivity(), "400-086-2599");
            }

        };
        int totalLen = spannableInfo.length() - 2;
        int tipLen = totalLen - 12;
        ForegroundColorSpan blue = new ForegroundColorSpan(getResources().getColor(R.color.orange));
        spannableInfo.setSpan(clickable, tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(blue, tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(new UnderlineSpan(), tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView agreeTV = mFindPswTip;
        agreeTV.setText(spannableInfo);
        agreeTV.setMovementMethod(LinkMovementMethod.getInstance());
        agreeTV.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {

    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {

    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {

    }

   /* @Override
    public void onRequestFinished(Request request, Bundle resultData) {
        if (getActivity() == null) {
            return;
        }
        String requestType = request.getRequestType();
        if (UserApiConstants.CMD_APPLY_RETRIEVE.equals(requestType)) {
            Bundle infoBundle = new Bundle();
            infoBundle.putString(BundleKeyConstant.KEY_REGISTER_NAME, mRegisterName);
            PageSwitcher.switchToTopNavPage(getActivity(), GetTokenFragment.class, infoBundle, R.string.back_btn, R.string.find_psw, 0);
        }

    }

    @Override
    public void onRequestError(Request request, Bundle resultData, int statusCode, int errorType) {
        L.d("onRequestError: statusCode-%s, errorType-%s", statusCode, errorType);
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        String requestType = request.getRequestType();
        if (UserApiConstants.CMD_APPLY_RETRIEVE.equals(requestType)) {
            // 发送短信验证失败处理
            final Header header = (Header) resultData.getSerializable(GjjOperationFactory.RSP_HEADER);
            if (header != null && !TextUtils.isEmpty(header.str_prompt)) {
                showToast(header.str_prompt);
            } else if (statusCode == ApiConstants.ClientErrorCode.ERROR_NETWORK_UNAVAILABLE.getCode()) {
                showToast(getString(R.string.network_unavailable));
            } else {
                showToast(R.string.get_sms_failed);
            }
        }
    }*/
}
