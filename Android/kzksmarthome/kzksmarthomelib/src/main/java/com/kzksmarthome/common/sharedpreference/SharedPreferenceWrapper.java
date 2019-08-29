package com.kzksmarthome.common.sharedpreference;

import java.util.Map;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.RemoteException;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.lib.ipc.ForeProcMessenger;

import kzksmarthome.common.lib.ipc.IBackProcProxy;


/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class SharedPreferenceWrapper implements SharedPreferences {
    private SharedPreferences sp;
    private EditorWrapper mEditorWrapper;

    private class EditorWrapper implements Editor {

        private Editor mEditor;

        public EditorWrapper(Editor edit) {
            mEditor = edit;
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void apply() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                commit();

            } else {
                if (SmartHomeAppLib.getInstance().isForeProcess()) {
                    IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                    if (binder != null) {
                        try {
                            binder.sharePrefApply();
                            return;

                        } catch (RemoteException e) {
                            L.w(e);
                        }
                    }
                }
                        mEditor.apply();
                    
       
            }
        }

        @Override
        public Editor clear() {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefClear();
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }
            BackgroundTaskExecutor.executeTask(new Runnable() {

                @Override
                public void run() {
                    mEditor.clear();
                }
                
            });
            return this;
        }

        @Override
        public boolean commit() {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        return binder.sharePrefCommit();

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }
              mEditor.commit();
            return true;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefPutBoolean(key, value);
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }

            mEditor.putBoolean(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefPutFloat(key, value);
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }

            mEditor.putFloat(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefPutInt(key, value);
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }

            mEditor.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefPutLong(key, value);
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }

            mEditor.putLong(key, value);
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefPutString(key, value);
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }

            mEditor.putString(key, value);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Editor putStringSet(String key, Set<String> value) {
            mEditor.putStringSet(key, value);
            return this;
        }

        @Override
        public Editor remove(final String key) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

                if (binder != null) {
                    try {
                        binder.sharePrefRemove(key);
                        return this;

                    } catch (RemoteException e) {
                        L.w(e);
                    }
                }
            }
            BackgroundTaskExecutor.executeTask(new Runnable() {

                @Override
                public void run() {
                    mEditor.remove(key);
                }
                
            });
            return this;
        }

    }

    public SharedPreferenceWrapper(SharedPreferences sp) {
        this.sp = sp;
    }

    @Override
    public boolean contains(String key) {
        return sp.contains(key);
    }

    @Override
    public Editor edit() {
        if (mEditorWrapper == null) {
            mEditorWrapper = new EditorWrapper(sp.edit());

        } else {
            mEditorWrapper.mEditor = sp.edit();
        }

        return mEditorWrapper;
    }

    @Override
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        if (SmartHomeAppLib.getInstance().isForeProcess()) {
            IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

            if (binder != null) {
                try {
                    return binder.sharePrefGetBoolean(key, defValue);

                } catch (RemoteException e) {
                    L.w(e);
                }
            }
        }

        return sp.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        if (SmartHomeAppLib.getInstance().isForeProcess()) {
            IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

            if (binder != null) {
                try {
                    return binder.sharePrefGetFloat(key, defValue);

                } catch (RemoteException e) {
                    L.w(e);
                }
            }
        }

        return sp.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        if (SmartHomeAppLib.getInstance().isForeProcess()) {
            IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

            if (binder != null) {
                try {
                    return binder.sharePrefGetInt(key, defValue);

                } catch (RemoteException e) {
                    L.w(e);
                }
            }
        }

        return sp.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        if (SmartHomeAppLib.getInstance().isForeProcess()) {
            IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

            if (binder != null) {
                try {
                    return binder.sharePrefGetLong(key, defValue);

                } catch (RemoteException e) {
                    L.w(e);
                }
            }
        }

        return sp.getLong(key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {
        if (SmartHomeAppLib.getInstance().isForeProcess()) {
            IBackProcProxy binder = ForeProcMessenger.getInstance().getBackProxy();

            if (binder != null) {
                try {
                    return binder.sharePrefGetString(key, defValue);

                } catch (RemoteException e) {
                    L.w(e);
                }
            }
        }

        return sp.getString(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return sp.getStringSet(key, defValues);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
