package com.kzksmarthome.common.lib.easylink.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public abstract class PackageManagerUtil {
	//获取包名
	public static String getPackageName(Context context){
		return context.getPackageName();
	}
	//获取版本名
	public static String getVersionName(Context context){
		try {
			return context.getPackageManager().getPackageInfo(getVersionName(context), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	//获取版本号
	public static int getVersionCode(Context context){
		try {
			return context.getPackageManager().getPackageInfo(getVersionName(context), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 判断包名是否存在
	 */
	public static boolean checkPackage(Context context, String packageName){
		if (packageName == null || "".equals(packageName)) {
			Log.e("","包名不存在");
			return false;
		}
		try{
			Log.e("","检查包名");
			context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			Log.e("","检查包名成功");
			return true;
		}catch (PackageManager.NameNotFoundException e){
			Log.e("","检查包名失败"+e.getMessage());
			return false;
		}
	}
	/**
	 * 静默安装		android.permission.INSTALL_PACKAGES
	 * String apkAbsolutePath  安装文件的地址
	 */
	public static boolean installapk_back(String apkAbsolutePath) {
		String[] args = { "pm", "install", "-r", apkAbsolutePath };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}

	/**
	 * 静默卸载		android.permission.DELETE_PACKAGES
	 * String packageName 包名
	 */
	public static boolean uninstallapk_back(String packageName) {
		String[] args = { "pm", "uninstall", packageName };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}
	/**
	 * 杀死进程		android.permission.KILL_BACKGROUND_PROCESSES
	 */
	public static void killProcess(Context context, String processname){
		try {
			ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(am, processname);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
