package com.kzksmarthome.SmartHouseYCT.biz.base;

import in.srain.cube.app.lifecycle.IComponentContainer;
import in.srain.cube.app.lifecycle.LifeCycleComponent;
import in.srain.cube.app.lifecycle.LifeCycleComponentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfActivityStatus;
import com.kzksmarthome.SmartHouseYCT.biz.splash.SplashActivity;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.user.UserInfo;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 * 
 */
public class BaseFragmentActivity extends FragmentActivity implements IComponentContainer {

	public final static String INTENT_EXTRA_FRAGMENT_CLASS_NAME = "class_name";

	public final static String INTENT_EXTRA_FRAGMENT_ARGS = "args";

	private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

	private boolean noCheckLogin = false;

	private FragmentFactory mFragmentFactory;

	private static int runingCount = 0;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if (!SmartHomeApp.getInstance().isInitialized()) {
			Intent splash = new Intent(getApplicationContext(), SplashActivity.class);
			startActivity(splash);
			finish();
			return;
		}
		noCheckLogin = getIntent().getBooleanExtra("noCheckLogin", false);
		if (!noCheckLogin && !SmartHomeAppLib.getUserMgr().checkLoginStatus()) {
			finish();
			return;
		}
		GjjEventBus.getInstance().register(this);
		L.d("%s TaskId %s", this, this.getTaskId());
	}

	/*
	 * @Override public void setContentView(int layoutResID) { View v =
	 * getLayoutInflater().inflate(layoutResID, null); setContentView(v); }
	 * 
	 * @Override public void setContentView(View view) {
	 * super.setContentView(view); checkShouldAddView(view); }
	 * 
	 * @Override public void setContentView(View view, LayoutParams params) {
	 * super.setContentView(view, params); checkShouldAddView(view); }
	 * 
	 * protected void checkShouldAddView(View root) { if (Build.VERSION.SDK_INT
	 * >= Build.VERSION_CODES.KITKAT) { SystemBarTintManager tintManager = new
	 * SystemBarTintManager(this); tintManager.setStatusBarTintEnabled(true);
	 * tintManager.setStatusBarTintResource(R.color.orange); } }
	 */

	protected void init(Bundle savedInstanceState) {

	}

	public FragmentFactory getFragmentFactory() {
		if (mFragmentFactory == null) {
			mFragmentFactory = new FragmentFactory();
		}
		return mFragmentFactory;
	}

	public boolean isFragmentInCache(BaseFragment f) {
		if (mFragmentFactory != null) {
			return mFragmentFactory.getFragmentFromCache(f.getClass()) != null;
		}
		return false;
	}

	public void onEventMainThread(UserInfo event) {
		if (!SmartHomeAppLib.getUserMgr().isLogin() && !isFinishing()) {
			MainTaskExecutor.scheduleTaskOnUiThread(150, new Runnable() {

				@Override
				public void run() {
					finish();
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		try {
			if (GjjEventBus.getInstance().isRegistered(this)) {
				GjjEventBus.getInstance().unregister(this);
			}
		} catch (Exception e) {
			L.e(e);
		}
		super.onDestroy();
		mComponentContainer.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		runingCount++;
		GjjEventBus.getInstance().post(new EventOfActivityStatus(EventOfActivityStatus.STATUS_RESUME));
		if (noCheckLogin || SmartHomeAppLib.getUserMgr().checkLoginStatus()) {
			mComponentContainer.onBecomesVisibleFromPartiallyInvisible();
		} else {
			finish();
			return;
		}
	}

	@Override
	protected void onRestart() {
		super.onStart();
		mComponentContainer.onBecomesVisibleFromTotallyInvisible();
	}

	@Override
	protected void onPause() {
		super.onPause();
		runingCount--;
		GjjEventBus.getInstance().post(new EventOfActivityStatus(EventOfActivityStatus.STATUS_PAUSE));
		mComponentContainer.onBecomesPartiallyInvisible();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mComponentContainer.onBecomesTotallyInvisible();
	}

	@Override
	public void addComponent(LifeCycleComponent component) {
		mComponentContainer.addComponent(component);
	}
	
	public static int getRuningCount(){
		return runingCount;
	}
}
