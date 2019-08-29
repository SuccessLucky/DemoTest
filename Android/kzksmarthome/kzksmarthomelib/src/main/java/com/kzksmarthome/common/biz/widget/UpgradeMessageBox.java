package com.kzksmarthome.common.biz.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.lib.R;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate 2015-1-29
 *
 */
public class UpgradeMessageBox extends Dialog {
   
    private TextView mMsgView;
    private TextView mMsgViewSecond;
    private Button mButton1;
    private Button mButton2;

    private View mLeftSpacer;
    private View mRightSpacer;
    
    private TextView mVersionNameAndApkSizeTextView;
    private TextView mUpgradeTimeTextView;
    
    private Object mTag;

    public UpgradeMessageBox(Context context) {
        this(context,  null, null, null);
    }

    private UpgradeMessageBox(Context context,  String title, String btn1Text, String btn2Text) {
        super(context, android.R.style.Theme_NoTitleBar);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.dialog_bg)));
        setContentView(R.layout.upgrade_dialog);
        mRightSpacer = findViewById(R.id.rightSpacer);
        mLeftSpacer = findViewById(R.id.leftSpacer);

        mVersionNameAndApkSizeTextView = (TextView)findViewById(R.id.versionName);
        mUpgradeTimeTextView = (TextView)findViewById(R.id.versionTime);
        
        final View.OnClickListener onClickListener = new MessageBoxBtnClickListener();
        mButton1 = (Button) findViewById(R.id.messageBoxBtn1);
        mButton1.setOnClickListener(onClickListener);
        mButton1.setTag(this);

        if (btn1Text != null)
            mButton1.setText(btn1Text);

        mButton2 = (Button) findViewById(R.id.messageBoxBtn2);
        mButton2.setOnClickListener(onClickListener);
        mButton2.setTag(this);

        if (btn2Text != null)
            mButton2.setText(btn2Text);

        mMsgView = (TextView) findViewById(R.id.tvMsg);
        mMsgViewSecond = (TextView) findViewById(R.id.tvMsg2);
    }

    private OnMessageBoxButtonClickedListener mOnMessageBoxButtonClickedListener = null;
    public void setOnMessageBoxButtonClickedListener(OnMessageBoxButtonClickedListener listener) {
        mOnMessageBoxButtonClickedListener = listener;
    }

    private OnMessageBoxButtonClickedListener getOnMessageBoxButtonClickedListener() {
        UpgradeMessageBox.OnMessageBoxButtonClickedListener listener = mOnMessageBoxButtonClickedListener;
//         Object obj = getTag();
//         if(obj != null && obj instanceof MessageBox.OnMessageBoxButtonClickedListener){
//             listener = (OnMessageBoxButtonClickedListener) obj;
//         }
        return listener;
    }

    private void perormButton1Click() {
        UpgradeMessageBox.OnMessageBoxButtonClickedListener listener = getOnMessageBoxButtonClickedListener();
        if (listener != null) {
            listener.onCancel(false);
        }
        dismissDialog();
    }

    private void perormButton2Click() {
        UpgradeMessageBox.OnMessageBoxButtonClickedListener listener = getOnMessageBoxButtonClickedListener();
        if (listener != null) {
            listener.onConfirm(false);
        }
        dismissDialog();
    }

    private final class MessageBoxBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mButton1 == v) {
                perormButton1Click();

            } else if (mButton2 == v) {
                perormButton2Click();
            }
        }
    }

    public void setButton1(String text) {
        mButton1.setText(text);
    }
    
    public void setButton1Visibility(int visibility) {
        mButton1.setVisibility(visibility);
    }
    
    public void setButton2(String text) {
        mButton2.setText(text);
    }
    
    public void setVersionNameAndApkSize(String versionName,long apkSize){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(SmartHomeAppLib.getInstance().getContext().getString(R.string.version));
        stringBuilder.append(":");
        stringBuilder.append(versionName);
        stringBuilder.append(" | ");
        stringBuilder.append(SmartHomeAppLib.getInstance().getContext().getString(R.string.size));
        stringBuilder.append(":");
        stringBuilder.append(Util.formatSizeInByteToMB(apkSize));
        mVersionNameAndApkSizeTextView.setText(stringBuilder.toString());
    }
    
    public void setUploadTime(long upgradeTime){
        mUpgradeTimeTextView.setText(SmartHomeAppLib.getInstance().getContext().getString(R.string.launch_time)
                + Util.formatStringTime(upgradeTime, "yyyy年M月d日"));
    }


    public void setMessage(String msg) {
        mMsgView.setText(msg);
        //mMsgView.setGravity(Gravity.CENTER);
    }
    
    public void setSecondMessage(String msg) {
        mMsgViewSecond.setVisibility(View.VISIBLE);
        mMsgViewSecond.setText(msg);
    }

    /**
     * 部分文字需要变色，
     * @param msg 内容
     * @param startIndex 开始变色的下标
     * @param textColorId  color的id
     * ***/
    public void setMessage(String msg, int startIndex, int textColorId) {
        SpannableStringBuilder style = new SpannableStringBuilder(msg);
        style.setSpan(new ForegroundColorSpan(textColorId), startIndex, style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMsgView.setText(style);
    }

    public void setFlag(Object flag) {
        mMsgView.setTag(flag);
    }

    public Object getFlag() {
        return mMsgView.getTag();
    }

    public void setTag(Object tag) {
        mTag=tag;
    }

    public Object getTag() {
        return mTag;
    }

    public boolean dismissDialog() {
        if (isShowing()) {
            try {
                dismiss();
                return true;
            } catch (Exception e) {
                L.w(e);
            }
        }

        return false;
    }

    public boolean show(boolean showButton1, boolean showCheckBox) {
        if (!isShowing()) {
//            if (!isShowing() && getWindow().getDecorView().getWindowToken() != null) {
            if (showButton1) {
                mButton1.setVisibility(View.VISIBLE);
            } else {
                mButton1.setVisibility(View.GONE);
            }
            
            int spacerVisibility = showButton1? View.GONE : View.VISIBLE;
            mRightSpacer.setVisibility(spacerVisibility);
            mLeftSpacer.setVisibility(spacerVisibility);


            try {
                show();

            } catch (Exception e) {
                // WindowManager$BadTokenException will be caught and the app would not display
                // the 'Force Close' message
                // see http://stackoverflow.com/a/9950503/668963
                L.w(e);
            }

            return true;
        }

        return false;
    }

    public static interface OnMessageBoxButtonClickedListener {
        void onConfirm(boolean isChecked);

        void onCancel(boolean isChecked);
    }
}
