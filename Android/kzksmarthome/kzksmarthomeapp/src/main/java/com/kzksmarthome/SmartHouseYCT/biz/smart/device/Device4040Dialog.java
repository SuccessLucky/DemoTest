package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author jack
 * @version V1.0
 * @Title: SceneDelConfirmDialog
 * @Description: 4040设备控制
 * @date 2016/10/18 8:03
 */
public class Device4040Dialog extends Dialog {

    @BindView(R.id.scene_4040_title_tv)
    TextView scene4040TitleTv;
    @BindView(R.id.scene_4040_dy_tv)
    TextView scene4040DyTv;
    @BindView(R.id.scene_4040_dl_tv)
    TextView scene4040DlTv;
    @BindView(R.id.scene_4040_yg_gl_tv)
    TextView scene4040YgGlTv;
    @BindView(R.id.scene_4040_yg_dl_tv)
    TextView scene4040YgDlTv;
    @BindView(R.id.scene_4040_open_tv)
    TextView scene4040OpenTv;
    @BindView(R.id.scene_4040_close_tv)
    TextView scene4040CloseTv;
    @BindView(R.id.scene_4040_cancel)
    TextView scene4040Cancel;
    @BindView(R.id.scene_del_confirm_btn_ll)
    LinearLayout sceneDelConfirmBtnLl;
    @BindView(R.id.scene_del_confirm_container)
    LinearLayout sceneDelConfirmContainer;


    private Context context;

    private Device4040Dialog(Context context, boolean cancelable, OnCancelListener listener) {
        super(context, cancelable, listener);
    }

    private Device4040Dialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_device_4040_dialog, null);
        ButterKnife.bind(this, contentView);
        this.context = context;
        super.setContentView(contentView);
    }

    public Device4040Dialog(Context context) {
        this(context, R.style.center_dialog);
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

    public TextView getScene4040TitleTv() {
        return scene4040TitleTv;
    }

    public void setScene4040TitleTv(TextView scene4040TitleTv) {
        this.scene4040TitleTv = scene4040TitleTv;
    }

    public TextView getScene4040DyTv() {
        return scene4040DyTv;
    }

    public void setScene4040DyTv(TextView scene4040DyTv) {
        this.scene4040DyTv = scene4040DyTv;
    }

    public TextView getScene4040DlTv() {
        return scene4040DlTv;
    }

    public void setScene4040DlTv(TextView scene4040DlTv) {
        this.scene4040DlTv = scene4040DlTv;
    }

    public TextView getScene4040YgGlTv() {
        return scene4040YgGlTv;
    }

    public void setScene4040YgGlTv(TextView scene4040YgGlTv) {
        this.scene4040YgGlTv = scene4040YgGlTv;
    }

    public TextView getScene4040YgDlTv() {
        return scene4040YgDlTv;
    }

    public void setScene4040YgDlTv(TextView scene4040YgDlTv) {
        this.scene4040YgDlTv = scene4040YgDlTv;
    }


    public TextView getScene4040OpenTv() {
        return scene4040OpenTv;
    }

    public void setScene4040OpenTv(TextView scene4040OpenTv) {
        this.scene4040OpenTv = scene4040OpenTv;
    }

    public TextView getScene4040CloseTv() {
        return scene4040CloseTv;
    }

    public void setScene4040CloseTv(TextView scene4040CloseTv) {
        this.scene4040CloseTv = scene4040CloseTv;
    }

    public TextView getScene4040Cancel() {
        return scene4040Cancel;
    }

    public void setScene4040Cancel(TextView scene4040Cancel) {
        this.scene4040Cancel = scene4040Cancel;
    }
}