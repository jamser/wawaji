package com.toiletCat.service;

import com.toiletCat.bean.CommonResult;
import com.toiletCat.bean.WxUserInfo;
import com.toiletCat.entity.User;

public interface UserService {

    /**
     * 根据手机号添加用户
     * @param mobileNo 手机号
     */
    CommonResult<User> registerOrLoginUserByMobileNo(String mobileNo);

    /**
     * 根据微信用户信息注册或登录
     * @param wxUserInfo 微信用户信息
     */
    CommonResult<User> registerOrLoginUserByWxUserInfo(WxUserInfo wxUserInfo);

    /**
     * 校验短信验证码
     * @param ticket 短信验证码
     * @param mobileNo 手机号
     * @return
     */
    CommonResult<String> verifyCode(String ticket, String mobileNo);

    /**
     * 根据用户编号,娃娃机编号判断用户是否可以进行游戏若可以则直接扣除相应游戏币数
     * @param userNo 用户编号
     * @param machineNo 娃娃机编号
     * @return
     */
    CommonResult<String> userPlayMachine(String userNo, String machineNo);

    /**
     * 根据用户编号,游戏房间编号判断用户是否可以进行游戏若可以则直接扣除相应游戏币数
     * @param userNo 用户编号
     * @param gameRoomNo 游戏房间编号
     * @return
     */
    CommonResult<String> userPlayGame(String userNo, String gameRoomNo);

    /**
     * 用户登录或注册方法
     * @param mobileNo 手机号
     * @return
     */
    CommonResult<String> sendMobileVerificationCode(String mobileNo);

    /**
     * 根据用户编号获得用户信息
     * @param userNo 用户编号
     * @return
     */
    CommonResult<User> getUserByUserNo(String userNo);

    /**
     * 根据微信openId获得用户信息
     * @param openId 微信openId
     * @return
     */
    CommonResult<User> getUserByOpenId(String openId);

    /**
     * 根据用户编号修改用户名和用户头像
     * @param userNo 用户编号
     * @param userName 用户名
     * @param userImg 用户头像
     */
    CommonResult updateUserInfoByIdAndUserNo(String userNo, String userName, String userImg);

    /**
     * 修改用户信息
     * @param user 用户信息
     */
    CommonResult updateUserInfo(User user);

    /**
     * 根据用户编号和游戏房间号获得游戏抓取结果
     * @param userNo 用户编号
     * @param gameRoomNo 游戏房间号
     * @param catchId 抓取id
     * @param status 状态
     * @return
     */
    CommonResult<String> getGameCatchResultByUserNoAndGameRoomNo(String userNo, String gameRoomNo, String catchId,
                                                                 Integer status);
    /**
     * 用户邀请码
     * @param userNo 用户编号
     * @param inviteCode 邀请码
     * @return
     */
    CommonResult<String> userInvite(String userNo, String inviteCode);
}
