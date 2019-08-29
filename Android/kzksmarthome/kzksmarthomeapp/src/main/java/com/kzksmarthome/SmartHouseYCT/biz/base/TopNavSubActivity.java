package com.kzksmarthome.SmartHouseYCT.biz.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfNewError;
import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.common.biz.widget.AndroidBug5497Workaround;
import com.kzksmarthome.common.biz.widget.AndroidBug5497Workaround.OnKeyboardListener;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 带导航Activity
 *
 * @author guoliexi
 * @createDate 2015-1-21
 */
public class TopNavSubActivity extends BaseSubActivity implements OnKeyboardListener {

    public final static String PARAM_BACK_TITLE = "back_title";
    public final static String PARAM_TOP_TITLE = "top_title";
    public final static String PARAM_BOTTOM_TITLE = "bottom_title";
    public final static String PARAM_TOP_RIGHT = "top_right_title";
    public final static String PARAM_TITLE_RIGHT_DRAWABLE = "top_title_right_drawable";
    public final static String PARAM_TOP_RIGHT_DEPUTY = "top_right_deputy_title";
    public final static String PARAM_TOP_RIGHT_DRAWABLE = "top_right_title_drawable";

    public final static String PARAM_LAYOUT = "layout_id";

    private static final int NUM_ANIMATION_DURATION = 500;// 数字动画ms

    @BindView(R.id.top_rl)
    RelativeLayout topRl;
    @BindView(R.id.top_back_tv)
    TextView mBackTV;
    @BindView(R.id.top_title_tv)
    TextView mTopTitleTV;
    @BindView(R.id.top_right_rl)
    RelativeLayout mRightLayout;
    @BindView(R.id.top_right_tv)
    TextView mTopRightTV;
    @BindView(R.id.network_hint_tv)
    TextView networkHintTv;

    @OnClick(R.id.top_back_tv)
    void back() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (null != fragment && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onBackPressed();
        }
        //onBackPressed();
    }

    private Object mEventReceiver = new Object() {
        public void onEventMainThread(EventOfNewError event) {
            if (event.netError) {
                networkHintTv.setVisibility(View.VISIBLE);
            } else {
                networkHintTv.setVisibility(View.GONE);
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (null != fragment && fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).onBackPressed();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SmartHomeApp.getInstance().setmCurrentActivity(this);
    }

    @OnClick(R.id.top_right_rl)
    void rightBtnClick() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (null != fragment && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onRightBtnClick();
        }
    }

    @OnClick(R.id.top_title_tv)
    void titleBtnClick() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (null != fragment && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onTitleBtnClick();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getIntent().getIntExtra(PARAM_LAYOUT, R.layout.activity_top_nav);
        setContentView(layoutId);
        ButterKnife.bind(this);
        AndroidBug5497Workaround.assistActivity(this);
        init(savedInstanceState);
        GjjEventBus.getInstance().register(mEventReceiver);
        networkHintTv.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int layoutId = getIntent().getIntExtra(PARAM_LAYOUT, R.layout.activity_top_nav);
        setContentView(layoutId);
    }*/

    @Override
    protected void handleArgs(Bundle bundle) {
        super.handleArgs(bundle);
        if (null != bundle) {
            //topRl.setBackground(getResources().getDrawable(R.color.main_bg_color));
            String bt = bundle.getString(PARAM_BACK_TITLE);
            setBackBtnText(bt);
            String tp = bundle.getString(PARAM_TOP_TITLE);
            setTopTitleTV(tp);
            String tr = bundle.getString(PARAM_TOP_RIGHT);
            String deputy = bundle.getString(PARAM_TOP_RIGHT_DEPUTY);
            setRightBtnText(tr, deputy);

            String bot = bundle.getString(PARAM_BOTTOM_TITLE);
            int drawableId = bundle.getInt(PARAM_TITLE_RIGHT_DRAWABLE);
            Drawable rd = null;
            if (drawableId > 0) {
                rd = getResources().getDrawable(drawableId);
            }
            mTopTitleTV.setCompoundDrawablesWithIntrinsicBounds(null, null, rd, null);
            int rightIvId = bundle.getInt(PARAM_TOP_RIGHT_DRAWABLE);
            Drawable rightDrawable = null;
            if (rightIvId > 0) {
                rightDrawable = getResources().getDrawable(rightIvId);
                if (!TextUtils.isEmpty(tr)) {
                    mTopRightTV.setText(tr);
                } else {
                    mTopRightTV.setText("");
                }
                mTopRightTV.setVisibility(View.VISIBLE);
                mRightLayout.setVisibility(View.VISIBLE);
                mTopRightTV.setCompoundDrawablesWithIntrinsicBounds(rightDrawable, null, null, null);
            } else {
                mTopRightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (null != fragment && fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).handleArgs(bundle);
            }
        }
    }

    public void setBackBtnText(String text) {
        if (text != null) {
            mBackTV.setText(text);
            mBackTV.setVisibility(View.VISIBLE);
        } else {
            mBackTV.setVisibility(View.GONE);
        }
    }

    public void setTopTitleTV(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTopTitleTV.setText(text);
            mTopTitleTV.setVisibility(View.VISIBLE);
        } else {
            mTopTitleTV.setVisibility(View.GONE);
        }
    }

    /**
     * 是否重复播放动画标志
     */
    private boolean mRunFlag;

    public void setRightBtnText(String text, String deputyText) {
        if (!TextUtils.isEmpty(text)) {
            mTopRightTV.setText(text);
            mTopRightTV.setVisibility(View.VISIBLE);
            mRightLayout.setVisibility(View.VISIBLE);
        } else {
            mTopRightTV.setVisibility(View.GONE);
            mRightLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void showRightIv(int rightIvId) {
        //Drawable rd = getResources().getDrawable(rightIvId);
       /* mRightLayout.setVisibility(View.VISIBLE);
        topRightIv.setVisibility(View.VISIBLE);
        mTopRightTV.setVisibility(View.GONE);*/
    }

    public TextView getTopRightTV() {
        return mTopRightTV;
    }

    public TextView getTopTitleTV() {
        return mTopTitleTV;
    }

    public RelativeLayout getTopRl() {
        return topRl;
    }

    public void setTopRl(RelativeLayout topRl) {
        this.topRl = topRl;
    }

    public TextView getmBackTV() {
        return mBackTV;
    }

    public void setmBackTV(TextView mBackTV) {
        this.mBackTV = mBackTV;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("laixj", "TopNavSubActivity onDestroy------");
    }

    @Override
    public void finish() {
        if (MainActivity.getMainActivity() == null) {
            Intent it = getIntent();
            if (it != null && "notification".equals(it.getStringExtra("from"))) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        super.finish();
    }

    @Override
    public void onShow(boolean showKeyboard, int height) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (null != fragment && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onSoftKeyboardStateChange(showKeyboard, height);
        }
    }

}
