package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class DrawableCenterRadioButton extends RadioButton implements OnCheckedChangeListener {

    private Drawable rightDrawable;

    public DrawableCenterRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawableCenterRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawableCenterRadioButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        Drawable[] drawables = getCompoundDrawables();
        if (null != drawables) {
            rightDrawable = drawables[2];
        }
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        this.setOnCheckedChangeListener(this);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isChecked() && rightDrawable != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = 0;
            drawableWidth = rightDrawable.getIntrinsicWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);
            canvas.translate((getWidth() - textWidth) / 2, 0);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }
}