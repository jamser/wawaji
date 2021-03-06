package com.toiletCat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toiletCat.bean.CommonResult;
import com.toiletCat.constants.BaseConstant;
import com.toiletCat.entity.UserAddress;
import com.toiletCat.entity.UserToy;
import com.toiletCat.enums.ChoiceType;
import com.toiletCat.service.UserToyService;
import com.toiletCat.utils.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/toiletCat/api/userToy")
@Controller
public class UserToyController {

    private static final Logger logger = LoggerFactory.getLogger(UserToyController.class);

    @Autowired
    private UserToyService userToyService;

    /**
     * 根据用户编号分页获得用户战利品
     * @param userNo 用户编号
     * @param startPage 开始页
     * @return
     */
    @RequestMapping(value = "/getUserToyListByUserNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserToyListByUserNo(String userNo, int startPage) {

        CommonResult<List<UserToy>> result = userToyService.getUserToyListByUserNo(userNo, startPage);

        return JSONUtil.getReturnBeanString(result);
    }

    /**
     * 根据用户编号获得用户战利品数量
     * @param userNo 用户编号
     * @return
     */
    @RequestMapping(value = "/countUserToyByUserNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String countUserToyByUserNo(String userNo) {

        CommonResult<Integer> result = userToyService.countUserToyByUserNo(userNo);

        return JSONUtil.getTotalCountAndPageSize(result, BaseConstant.DEFAULT_PAGE_SIZE);
    }

    /**
     * 添加用户战利品
     * @param paramStr 用户地址
     * @return
     */
    @RequestMapping(value = "/addUserToy", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addUserToy(String paramStr) {

        CommonResult result = userToyService.addUserToy(JSON.parseObject(paramStr, UserToy.class));

        return JSONUtil.getReturnStrString(result, BaseConstant.SUCCESS);
    }

    /**
     * 根据用户编号和玩具编号获得用户战利品记录
     * @param userNo 用户编号
     * @param toyNo 玩具编号
     * @return
     */
    @RequestMapping(value = "/getUserToyByUserNoAndToyNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserToyByUserNoAndToyNo(String userNo, String toyNo) {

        CommonResult<UserToy> result = userToyService.getUserToyByUserNoAndToyNo(userNo, toyNo);

        return JSONUtil.getReturnStrString(result, String.valueOf(result.getValue()));
    }

    /**
     * 根据用户编号获得用户所有未处理战利品
     * @param userNo 用户编号
     * @return
     */
    @RequestMapping(value = "/getAllUnHandleUserToyByUserNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getAllUnHandleUserToyByUserNo(String userNo) {

        CommonResult<List<UserToy>> result = userToyService.getAllUnHandleUserToyByUserNo(userNo);

        return JSONUtil.getReturnBeanString(result);
    }

    /**
     * 根据用户编号.id修改战利品选择类型
     * @param userToyStr 用户玩具str
     * @param userAddressStr 用户地址str
     * @return
     */
    @RequestMapping(value = "/updateChoiceTypeByIdAndUserNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateChoiceTypeByIdAndUserNo(String userToyStr, String userAddressStr) {

        UserAddress userAddress = null;

        JSONObject userToyJSON = JSONObject.parseObject(userToyStr);

        // 玩具名集合
        String toyNameArray = null;

        // 要更改的用户战利品id
        List<String> toyNoList = new ArrayList<>();

        Integer forCoinNum = 0;

        // 若为选择寄送类型
        if(ChoiceType.FOR_DELIVER.getStatus() == Integer.valueOf(userToyJSON.getString("choiceType"))) {

            userAddress = JSON.parseObject(userAddressStr, UserAddress.class);

            JSONArray toyNoArray = JSONArray.parseArray(userToyJSON.getString("toyNos"));

            JSONArray userToyNameArray = JSONArray.parseArray(userToyJSON.getString("userToyNames"));

            StringBuilder toyNameArrayBuilder = new StringBuilder("");

            for(Object obj : userToyNameArray) {
                toyNameArrayBuilder.append(JSONObject.parseObject(String.valueOf(obj)).getString("toyName"));
                toyNameArrayBuilder.append(",");
            }

            for(Object obj : toyNoArray) {
                toyNoList.add(JSONObject.parseObject(String.valueOf(obj)).getString("toyNo"));
            }

            toyNameArray = toyNameArrayBuilder.toString().
                    substring(0, toyNameArrayBuilder.toString().length() - 1);

            // 去除多余key
            userToyJSON.remove("toyNos");

            userToyJSON.remove("userToyNames");

            userToyStr = userToyJSON.toJSONString();

        } else {
            String forCoinNumString = userToyJSON.getString("forCoinNum");
            try {
                if(StringUtils.isNotBlank(forCoinNumString)) {
                    forCoinNum = Integer.valueOf(forCoinNumString);
                }
            } catch (Exception e) {
                logger.error("updateChoiceTypeByIdAndUserNo exchange error:" + e, e);
            }

        }

        UserToy userToy = JSON.parseObject(userToyStr, UserToy.class);

        CommonResult<String> result =  userToyService.updateChoiceTypeByIdAndUserNo(userToy, userAddress, toyNameArray,
                toyNoList, forCoinNum);

        return JSONUtil.getReturnBeanString(result);
    }

}
