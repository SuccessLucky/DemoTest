package com.project.utils;

import com.google.common.base.Strings;
import com.project.common.config.LoginConstant;
import com.project.entity.AuthUserEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class CookieUtils {
	public static String getKeyFromCookies(Cookie[] cookies, String key) {
        if (cookies == null || cookies.length == 0) {
            return null;
        }

		Cookie cookie = locateCookieByKey(cookies, key);
		if (cookie != null) {
            String valueObj = cookie.getValue();
            if (Strings.isNullOrEmpty(valueObj) || "None".equals(valueObj) || "null".equals(valueObj)) {
                return null;
            } else {
			    return cookie.getValue();
            }
		}
		return null;
	}

	public static Cookie locateCookieByKey(Cookie[] cookies, String key) {
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(key)) {
				return cookies[i];
			}
		}

		return null;
	}

	public static Cookie generateSessionCookie(AuthUserEntity user, int accessType) {
		String token = user.getSessionStatusEntity().getSessionToken();
		Date tokenExpireDate = user.getSessionStatusEntity().getSessionExpireDate();
		long diffInMillSec = tokenExpireDate.getTime() - new Date().getTime();

		String key = LoginConstant.PRIVILEGED_MEMBER_COOKIE_TOKEN_KEY;

		if (accessType == LoginConstant.ADMIN_ACCESS) {
			key = LoginConstant.ADMIN_COOKIE_TOKEN_KEY;
		}

		Cookie tokenCookie = new Cookie(key, token);
		tokenCookie.setPath("/");
		tokenCookie.setMaxAge((int) (diffInMillSec / 1000));
		return tokenCookie;
	}

	public static void sendCookie(AuthUserEntity user, HttpServletResponse response, int accessType) {
		Cookie loginCookie = CookieUtils.generateSessionCookie(user, accessType);
		response.addCookie(loginCookie);
	}
}
