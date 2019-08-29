package com.kzksmarthome.common.lib.tcp;


import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;

/**
 * Created by Kop on 2015/7/28.
 */
public abstract class SplitCallback {
    public abstract void onSplitFinish(byte[] header, byte[] body);
    public void onSplitFinishBackground(final byte[] header, final byte[] body) {
        BackgroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                onSplitFinish(header, body);
            }
        });
    }
}
