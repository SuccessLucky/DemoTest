package com.kzksmarthome.common.biz.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

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
public class SpinningDialog extends Dialog {
    private TextView mMsgTextView;

    public SpinningDialog(Context context, int theme){
        super(context,theme);
    }

    public SpinningDialog(Activity context, String msg) {
        super(context, android.R.style.Theme_NoTitleBar);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x99000000));
        setContentView(R.layout.spinning_dialog);
        mMsgTextView = (TextView) findViewById(R.id.tvMsg);
        mMsgTextView.setText(msg);
    }

    public void setTag(Object tag) {
        mMsgTextView.setTag(tag);
    }

    public Object getTag() {
        return mMsgTextView.getTag();
    }

    public SpinningDialog(Activity context) {
        this(context, null);
    }

    public void setMessage(String msg) {
        mMsgTextView.setText(msg);
    }

    public boolean showDialog() {
        if (!isShowing()) {
            try {
                show();

            } catch (Exception e) {
                L.w(e);
            }
            return true;
        }

        return false;
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
}
