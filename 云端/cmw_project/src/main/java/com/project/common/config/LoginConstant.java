package com.project.common.config;

import com.google.common.collect.Maps;

import java.util.Map;

public class LoginConstant {
	public static final String SESSION_KEY = "userSession";
	public static final String PRIVILEGED_MEMBER_COOKIE_TOKEN_KEY = "_pt";
	public static final String ADMIN_COOKIE_TOKEN_KEY = "_at";
	public static final String LAST_ACCESSED_URL_KEY = "lastAccessedURLKey";
	public static final String FINAL_ADMIN_USER = "admin";
	public static final long SESSION_EXPIRE_TIME = 3600 * 24 * 7 * 1000; // one // week // in ms
	public static final long SESSION_EXPIRE_TIME_ADMIN = 3600 * 24 * 1000; // one day


	public static final int ADMIN_ACCESS = 1;
	public static final int NORMAL_MEMBER_ACCESS = 2;
	public static final int MERCHANT_ACCESS = 3;
	public static final int UNKNOWN_ACCESS = 11;

	public static final String ADMIN_URL_BASE = "/bk/";
	public static final String NORMAL_MEMBER_URL_BASE = "/web/";
	public static final String ADMIN_LOGING_PATH = "/bk/login.do";

	public static final String AVATAR_IMAGE = "img200X200";

	public static final String SCENE_ADMIN = "scene_admin";
	public static final String SCENE_MEMBER = "scene_member";
	public static final String SCENE_MERCHANT = "scene_merchant";

	public static final int USER_TYPE_ADMIN = 0;
	public static final int USER_TYPE_MEMBER = 1;
	public static final int USER_TYPE_MERCHANT = 2;

	public static final int FROM_WEB_EMAIL = 0;
	public static final int FROM_APP_EMAIL = 1;
	public static final int FROM_APP_PHONE = 2;

	public static int MEMBER_MANAGE_STATUS_LOCKED = 2;
	public static int MEMBER_MANAGE_FIRST_LOGIN = 1;

	// 权限控制参数
	public static final int P_MEMBER = 1001;
	public static final int P_ACTIVITY = 1002;
	public static final int P_THEME = 1003;
	public static final int P_ORDER = 1004;
	public static final int P_MERCHANT = 1005;
	public static final int P_CATEGORY = 1006;
	public static final int P_TAG = 1007;
	public static final int P_CITY = 1008;

	public static final Map<Integer, String> role = Maps.newHashMap();
	static {
		role.put(1001, "member");
		role.put(1002, "activity");
		role.put(1003, "theme");
		role.put(1004, "order");
		role.put(1005, "merchant");
		role.put(1006, "category");
		role.put(1007, "tag");
		role.put(1008, "city");
	}

}
