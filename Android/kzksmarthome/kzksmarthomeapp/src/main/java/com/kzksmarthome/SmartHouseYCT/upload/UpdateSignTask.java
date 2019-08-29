package com.kzksmarthome.SmartHouseYCT.upload;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.kzksmarthome.common.module.log.L;
import org.json.JSONObject;

import com.tencent.upload.Const.ServerEnv;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * 向服务器请求更新签名的异步任务，用于演示客户端如何获取签名<br/>
 * 作用: 1.拉取上传使用的签名 2.拉取用于文件资源操作(删除、复制等)的单次有效签名<br/><br/>
 * <p/>
 * 注意：业务修改了BizService.APPID之后，需要自己实现相关的签名拉取逻辑<br/>
 */
public class UpdateSignTask extends AsyncTask<Void, Integer, String> {
    private Context mContext;
    private ProgressDialog mDialog;

    private String mAppid;
    private String mUserid;
    private String mSecretId;
    private String mBucket;
    private String mFileId;

    private OnGetSignListener mListener;

    public interface OnGetSignListener {
        public void onSign(String sign);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param appid    业务APPID
     * @param userid   业务的用户ID
     * @param secretId 业务在腾讯云注册申请之后分配得到，注意此处仅用于演示，正式业务请勿将secretId写死在客户端，否则可能泄露导致安全问题
     * @param bucket   业务使用的Bucket
     * @param fileId   文件ID； 传入ID为空时，则拉取全局有效的签名；传入ID不为空，则拉取单次有效签名
     * @param listener 任务结果回调
     */
    public UpdateSignTask(Context context, String appid, String userid, String secretId, String bucket, String fileId, OnGetSignListener listener) {
        mContext = context;
        mAppid = appid;
        mUserid = userid;
        mSecretId = secretId;
        mFileId = fileId;
        mBucket = bucket;
        mListener = listener;
        mDialog = new ProgressDialog(context);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("正在更新签名...");
        mDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        mDialog.show();
    }

    private String encodeUrl(String url) {
        if (TextUtils.isEmpty(url))
            return url;
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            L.e(e);
        }
        return url;
    }

    @Override
    protected String doInBackground(Void... params) {
        String cgi = String.format("http://%s/tools/v1/getsign?secret_id=%s&expired=%d&UserId=%s&appid=%s&bucket=%s",
                BizService.ENVIRONMENT == ServerEnv.NORMAL ? "web.image.myqcloud.com" : "web.imagetest.myqcloud.com",
                mSecretId,
                System.currentTimeMillis() / 1000 + 3600 * 24 * 30, // 签名有效期一个月
                mUserid,
                mAppid,
                mBucket);
        if (!TextUtils.isEmpty(mFileId)) {  // mFileId不为空，则是为了拉取单次操作签名
            if (mFileId.startsWith("http")) {
                cgi += "&url=" + encodeUrl(mFileId); // 视频目前fileid为空，为了兼容暂时传入url
            } else {
                cgi += "&fileid=" + mFileId;
            }
        }
        try {

            L.i("jia---" + mFileId);
            URL url = null;
            if (!TextUtils.isEmpty(mFileId)) {  // mFileId不为空，则是为了拉取单次操作签名

                if (mFileId.startsWith("http")) {
                    //	cgi += "&url=" + mFileId; // 视频目前fileid为空，为了兼容暂时传入url
                } else {
                    url = new URL("http://203.195.194.28/php/getsign.php?type=copy&url=http://" + mBucket + "－" + mAppid + ".image.myqcloud.com/test1-10000002/0/" + mFileId + "/original");
                }
            } else {
                url = new URL("http://203.195.194.28/php/getsign.php?type=copy&url=http://" + mBucket + "－" + mAppid + ".image.myqcloud.com/arrowphoto/30/original");
            }

            //URL url = new URL("http://203.195.194.28/php/getsign.php");//cgi
//			Log.i("yuqian", "update sign cgi:" + cgi);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            byte[] mSocketBuf = new byte[4 * 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int count = 0;
            while ((count = in.read(mSocketBuf, 0, mSocketBuf.length)) > 0) {
                baos.write(mSocketBuf, 0, count);
            }

//            String config = new String(baos.toByteArray());
//            JSONObject jsonData = new JSONObject(config);
//            String configContent = jsonData.getString("data");
//            JSONObject configJson = new JSONObject(configContent);


            String config = new String(baos.toByteArray());
            JSONObject jsonData = new JSONObject(config);
            String configContent = jsonData.getString("sign");

            L.i("yuqian------" + configContent);
            return configContent;

        } catch (Exception e) {
            L.e(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String sign) {
        mDialog.dismiss();
        L.i("yuqian update sign response:" + sign);
        Toast.makeText(mContext, "更新签名" + (TextUtils.isEmpty(sign) ? "失败" : "成功") + " APPID=" + mAppid, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(sign)) {
            // mFileId为空说明是长期有效的签名，保存之；否则为单词有效签名，无需保存
            if (TextUtils.isEmpty(mFileId))
                BizService.getInstance().updateSign(mAppid, mUserid, mSecretId, mBucket, sign);
            if (mListener != null)
                mListener.onSign(sign);
        }
    }
}
