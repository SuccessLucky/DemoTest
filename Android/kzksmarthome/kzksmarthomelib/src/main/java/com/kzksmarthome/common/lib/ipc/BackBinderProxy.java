package com.kzksmarthome.common.lib.ipc;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.RemoteException;

import com.kzksmarthome.common.app.SmartHomeAppLib;

import kzksmarthome.common.lib.ipc.IBackProcProxy;


/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class BackBinderProxy extends IBackProcProxy.Stub {

    @Override
    public void sharePrefPutString(String key, String value) throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(key, value).commit();
    }

    @Override
    public String sharePrefGetString(String key, String defaultValue) throws RemoteException {
        return SmartHomeAppLib.getInstance().getPreferences().getString(key, defaultValue);
    }

    @Override
    public void sharePrefPutInt(String key, int value) throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().putInt(key, value).commit();
    }

    @Override
    public int sharePrefGetInt(String key, int defaultValue) throws RemoteException {
        return SmartHomeAppLib.getInstance().getPreferences().getInt(key, defaultValue);
    }

    @Override
    public void sharePrefPutLong(String key, long value) throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().putLong(key, value).commit();
    }

    @Override
    public long sharePrefGetLong(String key, long defaultValue) throws RemoteException {
        return SmartHomeAppLib.getInstance().getPreferences().getLong(key, defaultValue);
    }

    @Override
    public void sharePrefPutBoolean(String key, boolean value) throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().putBoolean(key, value).commit();
    }

    @Override
    public boolean sharePrefGetBoolean(String key, boolean defaultValue) throws RemoteException {
        return SmartHomeAppLib.getInstance().getPreferences().getBoolean(key, defaultValue);
    }

    @Override
    public void sharePrefPutFloat(String key, float value) throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().putFloat(key, value).commit();
    }

    @Override
    public float sharePrefGetFloat(String key, float defaultValue) throws RemoteException {
        return SmartHomeAppLib.getInstance().getPreferences().getFloat(key, defaultValue);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void sharePrefApply() throws RemoteException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            SmartHomeAppLib.getInstance().getPreferences().edit().commit();

        } else {
            SmartHomeAppLib.getInstance().getPreferences().edit().apply();
        }
    }

    @Override
    public void sharePrefClear() throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().clear().commit();
    }

    @Override
    public boolean sharePrefCommit() throws RemoteException {
        return SmartHomeAppLib.getInstance().getPreferences().edit().commit();
    }

    @Override
    public void sharePrefRemove(String key) throws RemoteException {
        SmartHomeAppLib.getInstance().getPreferences().edit().remove(key).commit();
    }

}
