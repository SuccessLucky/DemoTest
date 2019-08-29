package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.ImageBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.ImageEntity;
import com.project.service.ImageEntityService;
import com.project.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyanhao on 16/10/9.
 */
@Controller
@RequestMapping("/rest")
public class ImageController extends BaseController {

    @Autowired
    private ImageEntityService imageEntityService;

    @RequestMapping(value = "/v1/image/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getImageByGateway(@RequestParam("category") String categoryStr) {

        if (Strings.isNullOrEmpty(categoryStr)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            String baseUrl = UrlUtils.getBasePath(request) + "staticFile/imgs/";

            int category = Integer.parseInt(categoryStr);
            List<ImageEntity> imageEntities = imageEntityService.getImagesByCategory(category);
            List<ImageBean> imageBeanList = new ArrayList<>();
            for (ImageEntity imageEntity : imageEntities) {
                ImageBean imageBean = new ImageBean();
                imageBean.setId(imageEntity.getId());
                imageBean.setBase_url(baseUrl + ImageEntity.imageCategoryMap.get(category));
                imageBean.setImage_type(imageEntity.getImageType());
                imageBean.setName(imageEntity.getName());
                imageBeanList.add(imageBean);
            }

            Map<String, Object> reValue = new HashMap<>();
            reValue.put("images", imageBeanList);
            return trueResult(reValue);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseResult;
    }

}
