package com.project.common;

/**
 * Created by xieyanhao on 16/12/7.
 */
public class AuthenticationFilter {

    public static final String token = "d6e63b481ac9a3371c1d6e38129cde70";

    public static boolean authentication(String requestToken) {
        return token.equals(requestToken);
    }

}
