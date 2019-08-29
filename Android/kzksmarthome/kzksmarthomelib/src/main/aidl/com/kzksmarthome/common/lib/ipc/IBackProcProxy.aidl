package kzksmarthome.common.lib.ipc;

import java.util.List;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;

interface IBackProcProxy {

    void sharePrefPutString(String key, String value);
    
    String sharePrefGetString(String key, String defaultValue);
    
    void sharePrefPutInt(String key, int value);
    
    int sharePrefGetInt(String key, int defaultValue);
    
    void sharePrefPutLong(String key, long value);
    
    long sharePrefGetLong(String key, long defaultValue);
    
    void sharePrefPutBoolean(String key, boolean value);
    
    boolean sharePrefGetBoolean(String key, boolean defaultValue);
    
    void sharePrefPutFloat(String key, float value);
    
    float sharePrefGetFloat(String key, float defaultValue);
    
    void sharePrefApply();
    
    void sharePrefClear();
    
    boolean sharePrefCommit();
    
    void sharePrefRemove(String key);
    
}