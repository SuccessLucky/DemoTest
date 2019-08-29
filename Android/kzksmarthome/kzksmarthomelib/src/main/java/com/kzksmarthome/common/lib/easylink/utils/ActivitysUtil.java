package com.kzksmarthome.common.lib.easylink.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 界面管理
 * Created by hkf on 2016/10/27 0027.
 */
public class ActivitysUtil {
    private List<Activity> mList = new LinkedList<Activity>();
    private static ActivitysUtil instance;

    public synchronized static ActivitysUtil getInstance() {
        if (null == instance) {
            instance = new ActivitysUtil();
        }
        return instance;
    }

    //添加activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    //删除activity
    public void removeActivity(Activity activity) {
        for(int i=0;i<mList.size();i++){
            if(mList.get(i)==activity){
                mList.remove(i);
                if(activity!=null){
                    activity.finish();
                }
            }
        }
        System.gc();
    }

    //程序退出
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
			System.exit(0);
		}
    }
}
