package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * @author laixj
 * @version V1.0
 * @Title: FloorDelConfirmDialog
 * @Description: 楼层删除确认框
 * @date 2016/10/18 8:03
 */
public class OperateConfirmDialog extends Dialog implements
		View.OnClickListener {

	public interface OnEnsureClick {
		void onEnsureClick();
	}

	private OnEnsureClick mListener;

	private String mTitleString;

	private TextView mTvTitle;

	private Context context;

	private OperateConfirmDialog(Context context, boolean cancelable, OnCancelListener listener) {
		super(context, cancelable, listener);
	}

	private OperateConfirmDialog(Context context, int defStyle, final String titleString) {
		super(context, defStyle);
		View contentView = View.inflate(context, R.layout.dialog_family_user_del_confirm, null);

		this.context = context;
		this.mTitleString = titleString;

		mTvTitle = (TextView) contentView.findViewById(R.id.user_del_confirm_title_tv);
		if (!TextUtils.isEmpty(mTitleString)) {
			mTvTitle.setText(mTitleString);
		}
		contentView.findViewById(R.id.user_del_confirm_cancel).setOnClickListener(this);
		contentView.findViewById(R.id.user_del_confirm_ensure).setOnClickListener(this);
		super.setContentView(contentView);
	}

	public OperateConfirmDialog(Context context, String titleString) {
		this(context, R.style.center_dialog, titleString);
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

	public String getmTitleString() {
		return mTitleString;
	}

	public void setmTitleString(String mTitleString) {
		this.mTitleString = mTitleString;
	}

	public void setOnEnsureClickListener(OnEnsureClick lis) {
		mListener = lis;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.user_del_confirm_ensure:
				if (mListener != null) {
					mListener.onEnsureClick();
				}
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
}