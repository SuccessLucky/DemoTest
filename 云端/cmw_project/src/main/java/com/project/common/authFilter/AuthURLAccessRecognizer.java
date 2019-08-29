package com.project.common.authFilter;

import com.project.common.config.LoginConstant;
import com.project.utils.UrlUtils;

import javax.servlet.http.HttpServletRequest;

public class AuthURLAccessRecognizer extends PKCommonUrlRecognizer {

	private static String regex_user = "^/rest/\\D+/?.*\\.json$";

	public int getAccessType(String url, HttpServletRequest request) {
		String servletPath = request.getServletPath();

		if (isUrlAction(url)) {
			// 判断是否合法请求
			if (servletPath.startsWith(LoginConstant.ADMIN_URL_BASE)) {
				return LoginConstant.ADMIN_ACCESS;
			}

			if (servletPath.startsWith(LoginConstant.NORMAL_MEMBER_URL_BASE)
					|| UrlUtils.regex(servletPath, regex_user)) {
				return LoginConstant.NORMAL_MEMBER_ACCESS;
			}
		} else {

			if (servletPath.startsWith(LoginConstant.ADMIN_URL_BASE)) {
				return LoginConstant.ADMIN_ACCESS;
			}

			if (servletPath.startsWith(LoginConstant.NORMAL_MEMBER_URL_BASE)) {
				return LoginConstant.NORMAL_MEMBER_ACCESS;
			}
		}

		return LoginConstant.NORMAL_MEMBER_ACCESS;
		// return LoginConstant.UNKNOWN_ACCESS;
	}

}
