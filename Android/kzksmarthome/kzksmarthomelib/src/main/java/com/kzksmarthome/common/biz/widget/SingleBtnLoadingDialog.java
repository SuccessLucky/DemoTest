package com.kzksmarthome.common.biz.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.lib.R;

public class SingleBtnLoadingDialog extends AlertDialog {

    // @BindView(R.id.btn_cancel)
    TextView cancelBtn;
    // @BindView(R.id.dialog_content)
    TextView content;

    // @OnClick(R.id.btn_cancel)
    public void onCancel() {
        if (cancelClickListener != null) {
            cancelClickListener.onClick(cancelBtn);
        } else {
            this.cancel();
        }
    }

    private android.view.View.OnClickListener cancelClickListener;

    public SingleBtnLoadingDialog(Context context, int theme) {
        super(context, R.style.white_bg_dialog);
    }

    public SingleBtnLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int margin = getContext().getResources().getDimensionPixelSize(R.dimen.margin_120px);
        int screanWidth = Util.getScreenWidth(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screanWidth - margin
                - margin, LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_single_btn, null);
        setContentView(view, params);
        // ButterKnife.bind(this);
        cancelBtn = (TextView) findViewById(R.id.btn_cancel);
        content = (TextView) findViewById(R.id.dialog_content);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    public void setContent(int contentResId) {
        this.content.setText(contentResId);
    }

    public void setContentAndBtn(int contentResId, int cancelResId) {
        this.content.setText(contentResId);
        this.cancelBtn.setText(cancelResId);
    }

    public void setCancelClickListener(android.view.View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }
}
