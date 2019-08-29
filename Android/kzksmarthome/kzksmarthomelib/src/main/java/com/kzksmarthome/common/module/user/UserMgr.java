/**
 *
 */
package com.kzksmarthome.common.module.user;

import android.text.TextUtils;
import android.util.Log;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfLaunchLogin;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.util.GsonHelper;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

/**
 * 类/接口注释
 *
 * @author guoliexi
 * @createDate 2015-1-13
 */
public class UserMgr {

    private static final int DEFAULT_CHECK_REFRESH_SIGN_SECONDS = 60 * 30;
    protected UserInfo mUserInfo = null;


    public UserMgr() {
        GjjEventBus.getInstance().register(userEvent);
        //UserInfoDAO dao = CmwDAOFactory.getDAO(UserInfoDAO.class);
        // TODO remove these codes
        /*UserInfo userInfo = new UserInfo();
        userInfo.uid = "nico";
        userInfo.expireTime = Integer.MAX_VALUE;
        userInfo.email = "xmjl@ywj.com";
        userInfo.sign = new byte[] { 1, 2 };
        userInfo.skey = "1A2B3C4D5E6F";
        saveUser(userInfo);*/
        mUserInfo = getUser();//dao.getUser();
        if (null != mUserInfo) {
            Log.d("laixj", mUserInfo.toString());
        }
    }

    /**
     * 判断是否登陆
     *
     * @return
     */
    public boolean isLogin() {
        //TODO:先屏蔽登录
        if (null == mUserInfo) {
            return false;
        }
        if (TextUtils.isEmpty(mUserInfo.token)) {
            return false;
        }
//        if (null == userInfo.sign || Util.bytesToInt(userInfo.sign, 0) == 0) {
//            return false;
//        }
//        if (userInfo.getExpireTime() < System.currentTimeMillis() / 1000) {
//            return false;
//        }
        return true;
    }

    /**
     * 是否临近过期时间，距离过期时间30分钟内，或session key为空串，返回true
     *
     * @return
     */
    public boolean isCloseToExpireTime() {
        if (null != mUserInfo) {
            boolean result = (mUserInfo.expire_time - DEFAULT_CHECK_REFRESH_SIGN_SECONDS) < System.currentTimeMillis() / 1000
                    || TextUtils.isEmpty(mUserInfo.token);
            L.d("UserMgr isCloseToExpireTime: %s", result);
            return result;
        }
        return false;
    }


    public Object userEvent = new Object() {
        public void onEventBackgroundThread(UserInfo event) {
            L.d("UserMgr:  userinfo change event %s", event);
            mUserInfo = event;
            if (!isLogin() && SmartHomeAppLib.getInstance().isForeProcess()) {
                GjjEventBus.getInstance().post(new EventOfLaunchLogin());
            }
        }
    };

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfo getUser() {
        if(mUserInfo == null){
            String userInfoStr = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_USER_INFO,"");
            if(!TextUtils.isEmpty(userInfoStr)) {
                mUserInfo = GsonHelper.getGson().fromJson(userInfoStr,UserInfo.class);
            }
        }
        return mUserInfo;
    }

    /**
     * 保存用户登录凭证
     *
     * @param userInfo
     */
    public void saveUser(UserInfo userInfo) {
        mUserInfo = userInfo;
        String userInfoStr = GsonHelper.getGson().toJson(userInfo);
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_USER_INFO,userInfoStr).commit();

       /* UserInfoDAO dao = CmwDAOFactory.getDAO(UserInfoDAO.class);
        long result = dao.updateUser(userInfo);
        if (result <= 0) {
            dao.deleteAllUser();// 保存一个用户
            SmartHomeAppLib app = SmartHomeAppLib.getInstance();
            //删除sdcard文件
            DataCleanMgr.cleanExternalCache(app.getContext());
            DataCleanMgr.cleanExternalFiles(app.getContext());
            //删除本地配置
//            GjjAppLib.getInstance().getPreferences().edit().clear().commit();
            //SmartHomeAppLib.getInstance().getPreferences().edit().remove(SharePrefConstant.PREFS_KEY_AID).commit();
            //后台进程接收到EventOfCleanDataCache后会清除内存和数据库中的缓存数据
            GjjEventBus.getInstance().post(new EventOfCleanDataCache(), true);
            //清除消息记录
            CmwDAOFactory.getDAO(MessageDAO.class).deleteAll();
            result = dao.addUser(userInfo);
            Log.d("laixj", "添加用户-result->" + result);
        }*/
        /*SharedPreferences.Editor editor = SmartHomeAppLib.getInstance().getPreferences().edit();
        Log.d("laixj", "保存nickname-->"+userInfo.nickname);
        Log.d("laixj", "保存password-->"+userInfo.password);
        editor.putString(SharePrefConstant.PREFS_KEY_USERNAME, userInfo.nickname);
        editor.putString(SharePrefConstant.PREFS_KEY_PASSWORD, userInfo.password);
        editor.commit();*/
        GjjEventBus.getInstance().post(userInfo, true);
    }



    /**
     * 清除登录凭证
     */
    public void logOut() {
//        UserInfoDAO dao = CmwDAOFactory.getDAO(UserInfoDAO.class);
//        dao.expiredUser();
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_USER_INFO,null);
        if (null != mUserInfo) {
            mUserInfo.expire_time = 0;
            mUserInfo.uuid = null;
            mUserInfo.token = null;
            mUserInfo.address = null;
            mUserInfo.age = 0;
            mUserInfo.portrait = null;
            mUserInfo.platform = 0;
            mUserInfo.email = null;
            mUserInfo.nickname = null;
            mUserInfo.sexual = 0;
        } else {
            mUserInfo = new UserInfo();
        }
        /*SharedPreferences.Editor editor = SmartHomeAppLib.getInstance().getPreferences().edit();
        editor.putString(SharePrefConstant.PREFS_KEY_PASSWORD, "");
        editor.commit();*/
        GjjEventBus.getInstance().post(mUserInfo, true);
        // 清除解锁手势
//        LockPatternUtils.clearLock();
    }

    /**
     * 检查未登录，跳转到登录页
     *
     * @return
     */
    public boolean checkLoginStatus() {
        if (!isLogin()) {
            if (null == mUserInfo) {
                mUserInfo = new UserInfo();
            }
            L.d("UserMgr: login false");
            GjjEventBus.getInstance().post(mUserInfo, true);
            return false;
        }
        return true;
    }
}
