package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.kzksmarthome.lib.R;

public class EditTextWithDel extends EditText {

    private Drawable delInable;
    private Drawable delAble;
    private Drawable rightDrawable;
    private Drawable leftDrawable;
    private Drawable topDrawable;
    private Drawable bottomDrawable;
    private Context mContext;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        delInable = mContext.getResources().getDrawable(R.drawable.login_icon_delete_un);
        delAble = mContext.getResources().getDrawable(R.drawable.login_icon_delete_pr);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!hasFocus() || TextUtils.isEmpty(getText().toString())) {
                    setDrawable(null);
                } else {
                    setDrawable(delInable);
                }
            }
        });
        setDrawable(null);

        this.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(getText().toString())) {
                    setDrawable(delInable);
                } else {
                    setDrawable(null);
                }
            }
        });
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top,
            Drawable right, Drawable bottom) {
        this.leftDrawable = left;
        this.topDrawable = top;
        this.bottomDrawable = bottom;
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    /**
     * 设置删除图片
     */
    private void setDrawable(Drawable delDrawable) {
        if (rightDrawable == delDrawable) {
            return;
        }
        rightDrawable = delDrawable;
        setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable,
                bottomDrawable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (delAble != null) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - getPaddingRight() - this.getTotalPaddingRight();
            if (rect.contains(eventX, eventY)) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                } else {
                    setDrawable(delAble);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
