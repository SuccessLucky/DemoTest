package com.project.utils;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

	public static String getBasePath(HttpServletRequest httpReq) {
		String path = httpReq.getContextPath();

		int portNum = httpReq.getServerPort();

		String port = portNum == 80 || portNum == 443 ? "" : ":" + Integer.toString(portNum);

        String serverName = httpReq.getServerName();
		String scheme = httpReq.getScheme();
		if (serverName.endsWith(".com")) {
//			scheme += "s";
		}
		String basePath = scheme + "://" + serverName + port + path + "/";
		return basePath;
	}

	public static String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	public static String calcRequestStr(String url, String queryStr) {
		String ret = url;

		if (queryStr != null && queryStr.length() > 0)
			ret += "?" + queryStr;

		return ret;
	}

	public static String calcRequestStr(HttpServletRequest req) {
		String url = req.getRequestURL().toString();
		String queryString = req.getQueryString();

		String reqURL = calcRequestStr(url, queryString);
		return reqURL;
	}

	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}
	
	public static String encode(String url, String unicode) {
		if(Strings.isNullOrEmpty(url)){
			return "";
		}
		
		if (Strings.isNullOrEmpty(unicode)) {
			unicode = "UTF-8";
		}
		try {
			url = URLEncoder.encode(url, unicode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
		
	}
	
	public static String encode(String url) {
		return encode(url, null);
	}

    public static boolean regex(String str, String regex) {

        if (Strings.isNullOrEmpty(str) || Strings.isNullOrEmpty(regex)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
