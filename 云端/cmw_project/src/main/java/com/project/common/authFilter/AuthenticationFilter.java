package com.project.common.authFilter;

import com.project.bean.UserSessionBean;
import com.project.common.config.LoginConstant;
import com.project.entity.AdminUserEntity;
import com.project.entity.AuthUserEntity;
import com.project.entity.MemberUserEntity;
import com.project.service.AuthUserEntityService;
import com.project.utils.BeanUtils;
import com.project.utils.CookieUtils;
import com.project.utils.LoginUtils;
import com.project.utils.UrlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xieyanhao on 16/7/10.
 */
public class AuthenticationFilter implements Filter {
    private static Log log = LogFactory.getLog(AuthenticationFilter.class);
    private AuthURLAccessRecognizer urlRecognizer;
    private List<String> excludePatterns;

    @Override
    public void destroy() {
    }

    /**
     * 利用 session 判断用户是否登陆
     *
     * @param session
     * @param accessType 用户类型
     * @return boolean
     */
    private boolean isUserLoggedIn(HttpSession session, int accessType) {
        UserSessionBean userBean = (UserSessionBean) session.getAttribute(LoginConstant.SESSION_KEY);

        if (userBean == null)
            return false;

        if (accessType == LoginConstant.ADMIN_ACCESS)
            if (userBean.getUser() instanceof AdminUserEntity)
                return true;

        if (accessType == LoginConstant.NORMAL_MEMBER_ACCESS)
            if (userBean.getUser() instanceof MemberUserEntity)
                return true;

        return false;
    }

    /**
     * 在 seesion 中取出实例化service
     *
     * @return userAuthService
     */
    private AuthUserEntityService getUserAuthService() {
        AuthUserEntityService userAuthService = new BeanUtils().getBean("authUserEntityService");
        return userAuthService;
    }

    /**
     * 在 cookie 中是否获取用户,并将用户信息存入 session 中
     *
     * @param request
     * @param accessType 用户类型
     * @return boolean
     */
    private boolean canAuthByCookie(HttpServletRequest request, int accessType) {

        String key = LoginConstant.PRIVILEGED_MEMBER_COOKIE_TOKEN_KEY;

        if (accessType == LoginConstant.ADMIN_ACCESS) {
            key = LoginConstant.ADMIN_COOKIE_TOKEN_KEY;
        }

        Cookie[] cookies = request.getCookies();
        String token = CookieUtils.getKeyFromCookies(cookies, key);

        if (token == null) {
            return false;
        }

        AuthUserEntityService userAuthService = getUserAuthService();
        AuthUserEntity user = userAuthService.getValidUserBySessionToken(token, accessType);
        if (user == null) {
            return false;
        }

        LoginUtils.setLoginState(request.getSession(), user);
        return true;
    }

    /**
     * 判断数据库中的 session token 是否过期
     *
     * @param request
     * @param user
     * @return boolean
     */
    private boolean isSessionExpired(HttpServletRequest request, AuthUserEntity user) {
        Date date = new Date();
        try {
            if (date.getTime() > user.getSessionStatusEntity().getSessionExpireDate().getTime()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * 重定向到最后请求链接
     *
     * @param request
     * @param response
     * @param accessType
     * @param url
     * @param queryStr
     * @throws IOException
     */
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response, int accessType, String url, String queryStr) throws IOException {
        String redirectURL = "";

        if (accessType == LoginConstant.ADMIN_ACCESS) {
            redirectURL = LoginConstant.ADMIN_LOGING_PATH;
        } else {
            assert false : "Bug!";
        }

        String reqURL = UrlUtils.calcRequestStr(url, queryStr);
        request.getSession().setAttribute(LoginConstant.LAST_ACCESSED_URL_KEY, reqURL);
        response.sendRedirect(request.getContextPath() + redirectURL);
    }

    /**
     * 判断用户类型
     *
     * @param accessType
     * @return
     */
    private boolean isPassthroughAccess(int accessType) {
        if (accessType == LoginConstant.NORMAL_MEMBER_ACCESS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是可请求的 url
     *
     * @param url
     * @return boolean
     */
    private boolean shouldExcludeUrl(String url) {
        for (String pattern : excludePatterns) {
            if (Pattern.matches(pattern, url)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        String url = httpReq.getRequestURL().toString();
        String queryString = httpReq.getQueryString();

        String reqURL = UrlUtils.calcRequestStr(httpReq);
        if (shouldExcludeUrl(url)) {
            chain.doFilter(request, response);
            return;
        }

        int accessType = urlRecognizer.getAccessType(url, httpReq);

        if (accessType == LoginConstant.UNKNOWN_ACCESS) {
            log.warn("Unknown access type for url:" + reqURL);
            httpRes.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (!isUserLoggedIn(httpReq.getSession(), accessType)) {
            // 用户没有 session 登陆
            if (canAuthByCookie(httpReq, accessType)) {
                chain.doFilter(request, response);
                return;
            }

            if (isPassthroughAccess(accessType)) {
                chain.doFilter(request, response);
                return;
            }
            redirectToLogin(httpReq, httpRes, accessType, url, queryString);
            return;
        } else {
            UserSessionBean userBean = (UserSessionBean) httpReq.getSession().getAttribute(LoginConstant.SESSION_KEY);
            AuthUserEntity user = userBean.getUser();

            // logic for admin actively kick out of someone
            if (isSessionExpired(httpReq, user)) {
                LoginUtils.resetLoginState(httpReq.getSession());

                if (isPassthroughAccess(accessType)) {
                    chain.doFilter(request, response);
                    return;
                }

                redirectToLogin(httpReq, httpRes, accessType, url, queryString);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void initExcludePattern(String patterns) {
        this.excludePatterns = new ArrayList<String>();
        String[] ary = patterns.split("\\|\\|");
        for (String str : ary) {
            if (str.length() > 0) {
                excludePatterns.add(str);
            }
        }
    }

    @Override
    public void init(FilterConfig cfg) throws ServletException {
        initExcludePattern(cfg.getInitParameter("excludePatterns"));
        urlRecognizer = new AuthURLAccessRecognizer();
    }
}
