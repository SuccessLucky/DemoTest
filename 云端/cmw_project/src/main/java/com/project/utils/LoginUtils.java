package com.project.utils;

import com.project.bean.UserSessionBean;
import com.project.common.config.LoginConstant;
import com.project.entity.AuthUserEntity;
import com.project.entity.MemberUserEntity;
import com.project.entity.UserEntity;

import javax.servlet.http.HttpSession;
import java.util.UUID;

public class LoginUtils {

	/**
	 * 存储 seesion,seesion中存储了 UserSeesionBean 对象
	 * 
	 * @param session
	 * @param user
	 */
	public static void setLoginState(HttpSession session, AuthUserEntity user) {
		resetLoginState(session);

		UserSessionBean userBean = new UserSessionBean();
		userBean.setUser(user);

		int userType = -1;
		if (user instanceof MemberUserEntity) {
			UserEntity userEntity = ((MemberUserEntity) user).getUserEntity();
			String image = userEntity.getImage();
			if (image == null) {
				userEntity.setImage("avatar.png");
			}
			userType = LoginConstant.USER_TYPE_MEMBER;
		} {
			assert false : "bug";
		}
        try {
            session.setAttribute("tomcat_log_user_id", userType + "-" + user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
		userBean.setUserType(userType);
		session.setAttribute(LoginConstant.SESSION_KEY, userBean);
	}

	/**
	 * 删除 seesion
	 * 
	 * @param session
	 * @return
	 */
	public static UserSessionBean resetLoginState(HttpSession session) {
		UserSessionBean sessionBean = (UserSessionBean) session.getAttribute(LoginConstant.SESSION_KEY);

		session.removeAttribute(LoginConstant.SESSION_KEY);

		return sessionBean;
	}

	public static String generateSessionToken() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 在 session 中获取用户信息
	 * 
	 * @param session
	 * @return
	 */
	public static AuthUserEntity getCurrentUser(HttpSession session) {
		UserSessionBean userBean = (UserSessionBean) session.getAttribute(LoginConstant.SESSION_KEY);
		if (userBean == null) {
			return null;
		}
		return userBean.getUser();
	}
}
