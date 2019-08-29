package com.kzksmarthome.common.module.stat;

import android.content.Context;


public abstract class AbstractStat {
    
    protected final Context mContext;
    
    AbstractStat(Context context) {
        mContext = context;
    }
    
    /**
     * 将内存中的统计数据持久化
     */
    protected abstract void flush();
    
    protected abstract void send(int priority);
}
