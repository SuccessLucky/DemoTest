package com.kzksmarthome.SmartHouseYCT.biz.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginActivity;
import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.module.log.L;
import com.squareup.okhttp.Request;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity implements RequestCallback {

	/*@BindView(R.id.splash_bottom)
    RelativeLayout splash_bottom;*/

    @BindView(R.id.splash_top_im)
    ImageView splash_top_im;

	/*@BindView(R.id.splash_bottom_im)
	CubeImageView splash_bottom_im;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SmartHomeApp app = SmartHomeApp.getInstance();

        final Param param = getGoIntent(app);
        if (MainActivity.getMainActivity() != null) {
            if (!param.nextActivityFullScreen) {
                requestNoFullScreen();
            }
            startActivity(param.goIntent);
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            ButterKnife.bind(this);
            if (param != null && !param.isLogining) {
                final long time = SystemClock.elapsedRealtime();
                ForegroundTaskExecutor.executeTask(new Runnable() {

                    @Override
                    public void run() {

                        //showSplashScreen();
                        while (!SmartHomeApp.getInstance().isInitialized() || (SystemClock.elapsedRealtime() - time) < 3500) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                L.e(e);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!param.nextActivityFullScreen) {
                                    requestNoFullScreen();
                                }
                                startActivity(param.goIntent);
                                finish();
                            }
                        });
                        MainTaskExecutor.scheduleTaskOnUiThread(800,
                                new Runnable() {
                                    @SuppressWarnings("deprecation")
                                    public void run() {
                                        //splash_bottom.setBackgroundDrawable(null);
                                        //splash_top_im.setImageDrawable(null);
                                        //splash_bottom_im.setImageDrawable(null);
                                    }
                                });
                    }
                });
            }
        }
        //获取IOT信息
        if(SmartHomeApp.getInstance().chekcGWINFO()) {
            SmartHomeApp.getInstance().getIOTInfoNew(false, 5);
        }
    }


    class Param {
        Intent goIntent;
        boolean nextActivityFullScreen;
        boolean isLogining;
    }

    private Param getGoIntent(final SmartHomeApp app) {
        Param param = new Param();
        if (MainActivity.getMainActivity() != null) {
            if (SmartHomeAppLib.getUserMgr().isLogin()) {
                param.goIntent = new Intent(app, MainActivity.class);
                param.isLogining = false;
            } else {
                param.isLogining = false;
                param.goIntent = new Intent(app, LoginActivity.class);
            }
            param.nextActivityFullScreen = false;
        } else {
            if (SmartHomeAppLib.getUserMgr().isLogin()) {
                param.isLogining = false;
                param.goIntent = new Intent(app, MainActivity.class);
            } else {
                param.isLogining = false;
                param.goIntent = new Intent(app, LoginActivity.class);
            }
            param.nextActivityFullScreen = false;
        }
        if (getIntent() != null) {
            param.goIntent.putExtras(getIntent());
        }
        return param;
    }

    private Param getGoIntent1(final SmartHomeApp app) {
        Param param = new Param();
        param.goIntent = new Intent(app, LoginActivity.class);
        param.nextActivityFullScreen = false;
        return param;
    }

    private void requestNoFullScreen() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		params.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if the splash screen is visible, we ignore all key events
        return true;
    }


     class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( SplashActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据闪屏图片设置闪屏效果
     */
    private void showSplashScreen() {
        // set background image in tile mode
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.splash_bg));
//        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        SmartHomeAppLib app = SmartHomeAppLib.getInstance();
        Bitmap splashBitmap = null;
        if (splashBitmap == null) {
            splashBitmap = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.qidong_img);
        }
        if (splashBitmap != null) {
            final Bitmap tmp = splashBitmap;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    L.d("setImageBitmap");
                    if (!isFinishing()) {
                        splash_top_im.setImageBitmap(tmp);
                    }
                }
            });
        }
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        L.d("loginFail");
        if (this.isFinishing()) {
            return;
        }
        SmartHomeApp.showToast("请检查网络");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (this.isFinishing()) {
            return;
        }
    }

    private void goMain() {
        final Param param1 = getGoIntent1(SmartHomeApp.getInstance());
        param1.goIntent = new Intent(SmartHomeApp.getInstance(), MainActivity.class);
        param1.nextActivityFullScreen = false;

        final long time = SystemClock.elapsedRealtime();
        ForegroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {

                showSplashScreen();
                while (!SmartHomeApp.getInstance().isInitialized() || (SystemClock.elapsedRealtime() - time) < 2000) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        L.e(e);
                    }
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (!param1.nextActivityFullScreen) {
                            requestNoFullScreen();
                        }
                        startActivity(param1.goIntent);
                        finish();
                    }
                });
                MainTaskExecutor.scheduleTaskOnUiThread(800,
                        new Runnable() {
                            @SuppressWarnings("deprecation")
                            public void run() {
                                //splash_bottom.setBackgroundDrawable(null);
                                splash_top_im.setImageDrawable(null);
                                //splash_bottom_im.setImageDrawable(null);
                            }
                        });
            }
        });
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        L.d("loginBizFail");
        if (this.isFinishing()) {
            return;
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        GjjEventBus.getInstance().unregister(this);
        super.onDestroy();
    }

}
