package com.kzksmarthome.SmartHouseYCT.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.tencent.upload.Const.ServerEnv;
import com.tencent.upload.UploadManager;

public class BizService {
	
	/*******************以下参数需要根据业务修改**************************/
	/*******修改之后需要自己实现签名逻辑，详见{@link UpdateSignTask}*******/
	public static String APPID = "10002464";
	public static String SECRETID = "AKID2ZzT8fyMGt0uQdiCaPldUKrDWIktcyTK";  // SerectID, 正式业务请勿将该值保存在客户端，否则泄露可能导致安全隐患
	public static String USERID = "314074007";
	public static String SIGN;
	public static String BUCKET = "arrowphoto"; // 业务请自行修改成自己配置的bucket
	/*****************************************************************/
	
	public static String APP_VERSION = "1.1.1";
	public static ServerEnv ENVIRONMENT;
	
	private static BizService sIntance = null;
    private static final byte[] INSTANCE_LOCK = new byte[0];
    
    private UploadManager mUploadManager;
    
    private SharedPreferences mSharedPreferences;

    public static synchronized BizService getInstance()
    {
        if (sIntance == null)
        {
            synchronized (INSTANCE_LOCK)
            {
                if (sIntance == null)
                {
                    sIntance = new BizService();
                }
            }
        }
        return sIntance;
    }

    private BizService()
    {

    }
    
    public void init(Context context)
    {
    	mSharedPreferences = context.getSharedPreferences("cloud_sign", 0);
//    	loadSign();
    	UploadManager.authorize(BizService.APPID, BizService.USERID, BizService.SIGN); 	
    	mUploadManager = new UploadManager(context, "");
    }
    
    public UploadManager getUploadManager()
    {
    	return mUploadManager;
    }
    
    public void changeUploadEnv(ServerEnv env)
    {
    	if(ENVIRONMENT == env)
    		return;
    	
    	ENVIRONMENT = env;
        mUploadManager.setServerEnv(ENVIRONMENT);
    }
    
    public void updateSign(String appid, String userid, String secretid, String bucket, String sign)
    {
    	APPID = appid;
    	SECRETID = secretid;
    	USERID = userid;
        BUCKET = bucket;
    	SIGN = sign;
    	UploadManager.authorize(BizService.APPID, BizService.USERID, BizService.SIGN);
    	saveSign();
    }
    
    // 加载签名信息
    private void loadSign()
    {
    	APPID = mSharedPreferences.getString("appid", APPID);
    	USERID = mSharedPreferences.getString("userid", USERID);
        BUCKET = mSharedPreferences.getString("bucket", BUCKET);
    	SECRETID = mSharedPreferences.getString("ak", SECRETID);
    	SIGN = mSharedPreferences.getString("sign", null);
    	Log.i("Demo", "load appid=" + APPID + " ak=" + SECRETID + " userid=" + USERID );
    }
    
    // 保存签名信息
    private void saveSign()
    {
    	if(ENVIRONMENT != ServerEnv.NORMAL)
    		return;
    	Log.i("Demo", "save appid=" + APPID + " ak=" + SECRETID + " userid=" + USERID + " sign=" + SIGN);
    	Editor edit = mSharedPreferences.edit();
    	edit.putString("appid", APPID);
    	edit.putString("ak", SECRETID);
    	edit.putString("userid", USERID);
    	edit.putString("sign", SIGN);
        edit.putString("bucket", BUCKET);
    	edit.commit();
    }
}
