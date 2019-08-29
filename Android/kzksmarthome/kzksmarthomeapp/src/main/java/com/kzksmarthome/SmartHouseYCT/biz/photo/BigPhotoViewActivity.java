package com.kzksmarthome.SmartHouseYCT.biz.photo;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseFragmentActivity;
import com.kzksmarthome.common.lib.util.Util;

/**
 * 
 * @Description: 大图图片浏览界面
 * @Copyright: Copyright (c) 2015
 * @version: 1.0.0.0
 * @author: jack
 * @createDate 2015-5-7
 * 
 */
public class BigPhotoViewActivity extends BaseFragmentActivity implements
        ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    HackyViewPager mViewPager;

    @BindView(R.id.pageinfo)
    TextView mPageInfoTV;

    @BindView(R.id.pageinfoSum)
    TextView mPageInfoTVSumTV;

    @BindView(R.id.image_description_tv)
    TextView mImageDescriptionTV;

    @BindView(R.id.image_description_title)
    TextView mImageDescriptionTitleTV;

    @OnClick(R.id.root)
    void exit() {
        finish();
    }

    /**
     * 图片内容
     */
    private ArrayList<PhotoData> mPhotoDataList;

    private boolean mNeedViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photoview);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        Intent i = getIntent();
        i.setExtrasClassLoader(PhotoData.class.getClassLoader());
        mNeedViewResult = i.getBooleanExtra("NeedViewResult", false);
        mPhotoDataList = i.getParcelableArrayListExtra("photoDataList");
        // 选中位置
        final int index = i.getIntExtra("index", 0);
        mViewPager.setAdapter(new PhotoPagerAdapter(this, this, mPhotoDataList));
        mViewPager.setCurrentItem(index);
        mViewPager.setOnPageChangeListener(this);
        mPageInfoTV.setText(String.valueOf(index + 1));
        mPageInfoTVSumTV.setText("/" + mPhotoDataList.size());
        mImageDescriptionTV.setMovementMethod(new ScrollingMovementMethod());
        if (!Util.isListEmpty(mPhotoDataList)) {
            PhotoData data = mPhotoDataList.get(index);
            setTextViewState(mImageDescriptionTV, data.photoDescription);
            setTextViewState(mImageDescriptionTitleTV, data.title);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(final int pos) {
        mPageInfoTV.setText(String.valueOf(pos + 1));
        PhotoData data = mPhotoDataList.get(pos);
        setTextViewState(mImageDescriptionTV, data.photoDescription);
        setTextViewState(mImageDescriptionTitleTV, data.title);
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra("index", pos);
        }
    }

    private void setTextViewState(TextView tv, String text) {
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

}
