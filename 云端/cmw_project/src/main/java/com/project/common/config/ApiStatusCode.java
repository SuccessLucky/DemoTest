package com.project.common.config;

import java.util.HashMap;
import java.util.Map;

/**
 * app json 数据状态码
 * 
 */
public final class ApiStatusCode {
	public static Map<String, String> maps = new HashMap<String, String>();

	// ======全局状态码=====//
	public final static String COMMON_ERROR = "1000";
	public final static String NOT_LOGIN_USER = "1001";
	public final static String NOT_IN_THIS_GATEWAY = "1002";
	public final static String NOT_GNOUGH_PERMISSION = "1003";


	public final static String UNACTIVATED_USER = "4002";
	public final static String MEMBER_LOCKED = "4003";
	public final static String INVALID_USER_PASS = "4004";
	public final static String SERVER_EXCEPTION = "4005";
	public final static String NOT_NULL = "4006";
	public final static String PARAM_ERROR = "4007";

	public final static String PACKAGE_NOT_EXIST = "5001";
	public final static String PARTICIPANTS_NOT_ENOUGH = "5002";
	public final static String SCHEDULE_DELETED = "5003";
	public final static String OTHER_INFOS_NOT_EXIT = "5004";
	public final static String ORDER_NOT_EXIT = "5005";
	public final static String COUPON_INVALID = "5006";
	public final static String PROMOTION_CODE_INVALID = "5007";
	public final static String PRIMOTION_CODE_EXPIRED = "5008";
	public final static String PRIMOTION_CODE_NOT_APPLY = "5009";
	public final static String COUPON_USED = "5010";
	public final static String COUPON_EXPIRED = "5011";
	public final static String COUPON_NOT_APPLY = "5012";
	public final static String SHORT_INVENTORY = "5013";
	public final static String ITEM_EXPIRED = "5014";
	public final static String VOUCHER_NOT_EXIST = "5015";
	public final static String ORDER_PAID = "5016";
	public final static String PAY_FAILURE = "5017";
	public final static String PROMOTION_CODE_USED = "5018";
	public final static String PROMOTION_CODE_SUCCESS = "5019";
	public final static String PAYMENT_FAILURE = "5020";
	public final static String COUPON_NOT_FROM_APP = "5021";
	public final static String COUPON_NOT_FROM_WEB = "5023";
	public final static String PRIMOTION_NOT_FROM_APP = "5022";
	public final static String PRIMOTION_NOT_FROM_WEB = "5024";
	public final static String PRIMOTION_NEW_USERS = "5025";
    public final static String REVIEW_NOT_EXIST = "5026";
    public final static String CANNOT_REPLY = "5027";
    public final static String CONFIRM_FAILED = "5028";
    public final static String HAS_PROCESSED = "5029";
    public final static String MOBILE_ERROR = "5030";
    public final static String NUMBER_VERTIFIED = "5031";
    public final static String NUMBER_NOT_VERTIFIED = "5032";
    public final static String DATE_FORMAT_ERROR = "5033";

	public final static String ADDED_TO_WISH_LIST = "6001";
	public final static String ADD_WISH_LIST_FAILED = "6002";
	public final static String IMAGE_UPLOAD_ERROR = "6003";
	public final static String MSM_CODE_INVALId = "6004";
	public final static String ALREADY_EXISTS = "6005";
	public final static String FORMAT_ERROR = "6006";

}
