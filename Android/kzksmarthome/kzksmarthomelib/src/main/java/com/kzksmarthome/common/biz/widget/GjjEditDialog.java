package com.kzksmarthome.common.biz.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.lib.R;

public class GjjEditDialog extends AlertDialog {
    /**
     * 提交按钮
     */
//    @BindView(R.id.btn_confirm)
    TextView confirmBtn;
    /**
     * 取消按钮
     */
//    @BindView(R.id.btn_cancel)
    TextView cancelBtn;
    /**
     * 内容布局
     */
//    @BindView(R.id.dialog_content_layout)
    LinearLayout content;
     /**
     * 地址选择列表
     */
//     @BindView(R.id.address_list)
     ListView address_list;
/*    @BindView(R.id.dialog_address1)
    TextView dialog_address1;
    
    @BindView(R.id.dialog_address2)
    TextView dialog_address2;*/
    /**
     * 地址输入框
     */
//    @BindView(R.id.address_edit)
    EditText address_edit;
    /**
     * 链表数组
     */
    private String[] dataArray;

//    @OnClick(R.id.btn_confirm)
  public  void onConfirm() {
//        this.cancel();
        if (confirmClickListener != null) {
            confirmClickListener.onClick(confirmBtn);
        }
    }

//    @OnClick(R.id.btn_cancel)
  public void onCancel() {
        this.cancel();
        if (cancelClickListener != null) {
            cancelClickListener.onClick(cancelBtn);
        }
    }

    private android.view.View.OnClickListener confirmClickListener;
    private android.view.View.OnClickListener cancelClickListener;
    private int contentResId;
    private int confirmResId;
    private int cancelResId;
    private Context context;

    public GjjEditDialog(Context context, int theme, String[] data) {
        super(context, theme);
        this.dataArray = data;
        this.context = context;

    }
    public GjjEditDialog(Context context, int theme) {
        this(context, theme, null);
    }

    public GjjEditDialog(Context context) {
        this(context, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int margin = getContext().getResources().getDimensionPixelSize(R.dimen.margin_120px);
        int screanWidth = Util.getScreenWidth(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screanWidth - margin
                - margin, LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit, null);
        setContentView(view, params);
//        ButterKnife.bind(this);

        confirmBtn = (TextView) findViewById(R.id.btn_confirm);
        cancelBtn = (TextView) findViewById(R.id.btn_cancel);
        content = (LinearLayout) findViewById(R.id.dialog_content_layout);
        address_list = (ListView) findViewById(R.id.address_list);
        address_edit = (EditText) findViewById(R.id.address_edit);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                onConfirm(); 
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        if (contentResId != 0) {
            // this.content.setText(contentResId);
        }
        if (confirmResId != 0) {
            this.confirmBtn.setText(confirmResId);
        }
        if (cancelResId != 0) {
            this.cancelBtn.setText(cancelResId);
        }
    }

    public void setContent(int contentResId) {
        if (null != this.content) {
            // this.content.setText(contentResId);
        }
        this.contentResId = contentResId;
    }

    public void setContentAndBtn(int contentResId, int confirmResId, int cancelResId) {
        if (null != this.content) {
            // this.content.setText(contentResId);
        }
        if (null != this.address_list) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.dialog_edit_item, R.id.dialog_address, dataArray);
            address_list.setAdapter(adapter);
        }

        this.contentResId = contentResId;
        if (null != this.confirmBtn) {
            this.confirmBtn.setText(confirmResId);
        }
        this.confirmResId = confirmResId;
        if (null != this.cancelBtn) {
            this.cancelBtn.setText(cancelResId);
        }
        this.cancelResId = cancelResId;
//        this.dialog_address1.setText(text)
    }

 

/*    public TextView getDialog_address1() {
        return dialog_address1;
    }

    public void setDialog_address1(TextView dialog_address1) {
        this.dialog_address1 = dialog_address1;
    }

    public TextView getDialog_address2() {
        return dialog_address2;
    }

    public void setDialog_address2(TextView dialog_address2) {
        this.dialog_address2 = dialog_address2;
    }*/

    public EditText getAddress_edit() {
        return address_edit;
    }

    public void setAddress_edit(EditText address_edit) {
        this.address_edit = address_edit;
    }
    

    public ListView getAddress_list() {
        return address_list;
    }

    public void setAddress_list(ListView address_list) {
        this.address_list = address_list;
    }

    public void setConfirmClickListener(android.view.View.OnClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public void setCancelClickListener(android.view.View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }
}
