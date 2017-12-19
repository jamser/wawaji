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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/toiletCat/userToy")
@Controller
public class UserToyController {

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
     * 根据用户编号和id获得用户娃娃记录
     * @param userNo 用户编号
     * @param id id
     * @return
     */
    @RequestMapping(value = "/getUserToyByUserNoAndId", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserToyByUserNoAndId(String userNo, Long id) {

        CommonResult<UserToy> result = userToyService.getUserToyByUserNoAndId(userNo, id);

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
        StringBuilder toyNameArrayBuilder = new StringBuilder("");

        // 要更改的用户战利品id
        List<Long> userToyIdList = new ArrayList<>();

        // 若为选择寄送类型
        if(ChoiceType.FOR_DELIVER.getStatus() == Integer.valueOf(userToyJSON.getString("choiceType"))) {

            userAddress = JSON.parseObject(userAddressStr, UserAddress.class);

            JSONArray userToyIdArray = JSONArray.parseArray(userToyJSON.getString("userToyIds"));

            JSONArray userToyNameArray = JSONArray.parseArray(userToyJSON.getString("userToyNames"));

            for(Object obj : userToyNameArray) {
                toyNameArrayBuilder.append(JSONObject.parseObject(String.valueOf(obj)).getString("toyName"));
                toyNameArrayBuilder.append(",");
            }

            for(Object obj : userToyIdArray) {
                userToyIdList.add(Long.valueOf(JSONObject.parseObject(String.valueOf(obj)).getString("toyName")));
            }

            String toyNameArray = toyNameArrayBuilder.toString().
                    substring(0, toyNameArrayBuilder.toString().length() - 1);

            // 去除多余key
            userToyJSON.remove("userToyIds");

            userToyJSON.remove("userToyNames");

            userToyStr = userToyJSON.toJSONString();

        }

        UserToy userToy = JSON.parseObject(userToyStr, UserToy.class);

        CommonResult result =  userToyService.updateChoiceTypeByIdAndUserNo(userToy, userAddress);

        return JSONUtil.getReturnStrString(result, BaseConstant.SUCCESS);
    }

    /**
     * 根据用户编号.id修改战利品处理状态
     * @param handleStatus 处理状态
     * @param id id
     * @param userNo 用户编号
     * @return
     */
    @RequestMapping(value = "/updateHandleStatusByIdAndUserNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateHandleStatusByIdAndUserNo(Integer handleStatus, Long id, String userNo) {

        CommonResult result =  userToyService.updateHandleStatusByIdAndUserNo(handleStatus, id, userNo);

        return JSONUtil.getReturnStrString(result, BaseConstant.SUCCESS);
    }

    public static void main(String[] args) {

        String ss = "1,2,3,4,5,";
        ss = ss.substring(0,ss.length() - 1);

        System.out.println(ss);
    }

}
