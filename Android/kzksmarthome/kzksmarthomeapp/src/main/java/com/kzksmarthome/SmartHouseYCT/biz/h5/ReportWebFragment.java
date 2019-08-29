package com.kzksmarthome.SmartHouseYCT.biz.h5;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseFragment;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.stat.BusinessStat;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public class ReportWebFragment extends BaseFragment implements OnClickListener{

    private String url;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar;
    
    @BindView(R.id.webview)
    Browser mWebView;
    
    @BindView(R.id.error_tv)
    TextView mErrorTip;
    boolean isError = false;
    /**
     * 分享URL
     */
    private String shareUrl;
    /**
     * 分享图片地址
     */
    private String shareICOnUrl;
    /**
     * 分享标题
     */
    private String shareTitle = "箭牌分享";
    /**
     * 分享内容
     */
    private String shareSummary = "箭牌智能卫浴";
    /**
     * 友盟分享控制器
     */
    private UMSocialService mController;
    private PopupWindow shareWindow;
    @OnClick(R.id.error_tv)
    void reload() {
        if(url != null){
            mErrorTip.setVisibility(View.GONE); 
            mWebView.reload();
            isError = false;
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_check_report, container, false);
        ButterKnife.bind(this, mRootView);

        WebViewClient webViewClient = new WebViewClient() {
            
            @Override
            public void onPageFinished(WebView view, String url) {
                if (getActivity() == null) {
                    return;
                }
                if (!isError) {
                    // 这个时候网页才显示
                    mWebView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                if (getActivity() == null) {
                    return;
                }
                view.setVisibility(View.GONE);
                mErrorTip.setVisibility(View.VISIBLE);
                isError = true;
/*                if (errorCode == WebViewClient.ERROR_HOST_LOOKUP ) {
                    mErrorTip.setText(R.string.network_unavailable);
                } else {
                    mErrorTip.setText(R.string.load_fail_click_reload);
                }*/
            }

        };
        WebChromeClient webChromeClient = new DefaultWebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (getActivity() == null) {
                    return;
                }
                if (newProgress == 0) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        };
        mWebView.init(webViewClient, webChromeClient);

        Bundle b = getArguments();
        url = b.getString(BundleKeyConstant.KEY_REPORT_URL);
        shareUrl = url;
        shareICOnUrl = b.getString(BundleKeyConstant.KEY_REPORT_ICON);
        shareTitle = b.getString(BundleKeyConstant.KEY_REPORT_TITLE);
        shareSummary = b.getString(BundleKeyConstant.KEY_REPORT_SUMMARY); 
        mWebView.loadUrl(url);
        /*mWebView.loadUrl("http://www.guojj.com/api/mobile/project_report.php");
        UserInfo uInfo = UserMgr.getInstance().getUser();
        String js = "javascript:var GJJ_TOKEN='" + Base64.encodeToString(uInfo.getSign(), Base64.NO_WRAP) + "';var GJJ_UID='" + uInfo.getUid() + "'";
        mWebView.loadUrl(js);*/

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }
    
    @Override
    public void onRightBtnClick() {
      showShareWindow();
    }
    
    /**
     * 分享
     */
    @SuppressWarnings("unused")
    private void showShareWindow() {
        dismissShareWindow();
        View contentView;
        if (shareWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate( R.layout.share_window_layout, null);
            contentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissShareWindow();
                }
            });
            ImageView share_weixin = (ImageView) contentView.findViewById(R.id.share_weixin);
            ImageView share_weixinpy = (ImageView) contentView.findViewById(R.id.share_weixinpy);
            ImageView share_sina = (ImageView) contentView.findViewById(R.id.share_sina);
            ImageView share_qq = (ImageView) contentView.findViewById(R.id.share_qq);
            Button share_cannel = (Button) contentView.findViewById(R.id.share_cannel);
            
            share_weixin.setOnClickListener(this);
            share_weixinpy.setOnClickListener(this);
            share_sina.setOnClickListener(this);
            share_qq.setOnClickListener(this);
            share_cannel.setOnClickListener(this);
            
            Rect r = new Rect();
            mRootView.getWindowVisibleDisplayFrame(r);
            final int[] location = new int[2];
            mRootView.getLocationOnScreen(location);
            shareWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
            shareWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            shareWindow.setAnimationStyle(R.style.popwin_anim_style);
            shareWindow.setOutsideTouchable(true);
            shareWindow.setFocusable(true);
        } else {
            contentView = shareWindow.getContentView();
        }

        shareWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
//      mPopWindow.showAsDropDown(view, 0, 0);
    }

    /**
     * 取消工程消息弹出框
     * 
     * @return
     */
    private boolean dismissShareWindow() {
        if (null != shareWindow) {
            shareWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        // 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        switch (view.getId()) {
   /*     case R.id.share_weixin:
            Utils.shareWeiXin(mController, getActivity(), shareSummary, shareTitle, shareUrl, shareICOnUrl);
            performShare(SHARE_MEDIA.WEIXIN);
            BusinessStat.getInstance().addStat("share", "click", "msg", "wx");
            break;
        case R.id.share_weixinpy:
            Utils.shareWeiXinPY(mController, getActivity(), shareSummary, shareTitle, shareUrl, shareICOnUrl);
            performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
            BusinessStat.getInstance().addStat("share", "click", "msg", "wxpy");
            break;
        case R.id.share_sina:
            Utils.shareSina(mController, getActivity(), shareTitle + "," + shareUrl, shareICOnUrl);
            directShare(SHARE_MEDIA.SINA);
            BusinessStat.getInstance().addStat("share", "click", "msg", "sina");
            break;
        case R.id.share_qq:
            Utils.shareQQ(mController, getActivity(), shareSummary, shareTitle, shareUrl, shareICOnUrl);
            performShare(SHARE_MEDIA.QQ);
            BusinessStat.getInstance().addStat("share", "click", "msg", "qq");
            break;*/
        case R.id.share_cannel:
            BusinessStat.getInstance().addStat("share", "click", "msg", "cancel");
            break;
        default:
            break;
        }
         dismissShareWindow();
    }
    
    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(getActivity(), platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText;
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享成功";
                    BusinessStat.getInstance().addStat("share", "succ", "msg", platform.toString());
                } else {
                    showText = "分享失败";
                    BusinessStat.getInstance().addStat("share", "fail", "msg", platform.toString());
                }
                SmartHomeApp.showToast(showText);
            }
        });
    }
    
    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
     */
    private void directShare(SHARE_MEDIA platform) {
        mController.directShare(getActivity(), platform, new SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText;
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败";
                    L.d("directShare 分享失败 %s", eCode);
                    BusinessStat.getInstance().addStat("share", "fail", "msg", platform.toString());
                } else {
                    showText = "分享成功";
                    BusinessStat.getInstance().addStat("share", "succ", "msg", platform.toString());
                }
                SmartHomeApp.showToast(showText);
            }
        });
    }
}
