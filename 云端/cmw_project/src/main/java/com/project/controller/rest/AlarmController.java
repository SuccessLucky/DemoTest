package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.AlarmBeanResp;
import com.project.bean.BaseResultBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.MemberGatewayEntity;
import com.project.service.AlarmEntityService;
import com.project.utils.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/28.
 */
@Controller
@RequestMapping("/rest")
public class AlarmController extends BaseController {

    private static Log logger = LogFactory.getLog(AlarmController.class);

    @Autowired
    private AlarmEntityService alarmEntityService;

    @RequestMapping(value = "/v1/alarm/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getFloorList() {
        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        String date = request.getParameter("date");
        if (Strings.isNullOrEmpty(date)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请选择日期");
        }

        try {

            String format = "yyyy-MM-dd HH:mm:ss";
            Date startDate = DateUtil.stringToDate(date + " 00:00:00", format);
            Date endDate = DateUtil.stringToDate(date + " 23:59:59", format);

            List<AlarmBeanResp> alarmBeanRespList = alarmEntityService.getAlarmByDate(startDate, endDate, memberGateway.getGateway());
            trueResult(alarmBeanRespList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }
        return responseResult;
    }

}
