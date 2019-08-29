package com.kzksmarthome.SmartHouseYCT.biz.base;

import in.srain.cube.app.lifecycle.IComponentContainer;
import in.srain.cube.app.lifecycle.LifeCycleComponent;
import in.srain.cube.app.lifecycle.LifeCycleComponentManager;
import android.app.Activity;
import android.os.Bundle;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.module.user.UserInfo;

public class BaseActivity extends Activity implements IComponentContainer {

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    private static int runingCount = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SmartHomeAppLib.getUserMgr().checkLoginStatus()) {
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        runingCount++;
//        GjjEventBus.getInstance().post(new EventOfActivityStatus(EventOfActivityStatus.STATUS_RESUME));
        if (!SmartHomeAppLib.getUserMgr().checkLoginStatus()) {
            finish();
            return;
        } else {
            mComponentContainer.onBecomesVisibleFromPartiallyInvisible();
        }
    }
    
    public  void onEventMainThread(UserInfo event) {
        if (!SmartHomeAppLib.getUserMgr().isLogin() && !isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (GjjEventBus.getInstance().isRegistered(this)) {
                GjjEventBus.getInstance().unregister(this);
            }
        } catch (Exception e) {
        }
        super.onDestroy();
        mComponentContainer.onDestroy();
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
//        GjjEventBus.getInstance().post(new EventOfActivityStatus(EventOfActivityStatus.STATUS_PAUSE));
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
