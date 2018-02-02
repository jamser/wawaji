package com.toiletCat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toiletCat.bean.Callback;
import com.toiletCat.bean.CommonResult;
import com.toiletCat.constants.BaseConstant;
import com.toiletCat.constants.ToiletCatConfigConstant;
import com.toiletCat.dao.*;
import com.toiletCat.entity.*;
import com.toiletCat.enums.*;
import com.toiletCat.service.ToiletCatConfigService;
import com.toiletCat.service.UserToyService;
import com.toiletCat.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Service("userToyService")
public class UserToyServiceImpl extends BaseServiceImpl implements UserToyService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserToyServiceImpl.class);

    @Autowired
    private UserToyDao userToyDao;

    @Autowired
    private DeliverDao deliverDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ToyDao toyDao;

    @Autowired
    private UserSpendRecordDao userSpendRecordDao;

    @Autowired
    private ToiletCatConfigService toiletCatConfigService;

    /**
     * 添加用户战利品记录
     * @param userToy 用户娃娃Bean
     */
    @Override
    public CommonResult addUserToy(final UserToy userToy) {

        return exec(new Callback() {
            @Override
            public void exec() {
                userToyDao.addUserToy(userToy);
            }
        }, "addUserToy", userToy);
    }

    /**
     * 根据用户编号获得用户玩具记录数
     * @param userNo 用户编号
     * @return
     */
    @Override
    public CommonResult<Integer> countUserToyByUserNo(final String userNo) {

        JSONObject json = new JSONObject();
        json.put("userNo",userNo);

        return exec(new Callback() {
            @Override
            public void exec() {
                got(userToyDao.countUserToyByUserNo(userNo));
            }
        },"countUserToyByUserNo", json);
    }

    /**
     * 根据用户编号分页获得所有用户娃娃记录
     * @param userNo 用户编号
     * @param startPage 开始页
     * @return
     */
    @Override
    public CommonResult<List<UserToy>> getUserToyListByUserNo(final String userNo, final int startPage) {

        JSONObject json = new JSONObject();
        json.put("userNo", userNo);
        json.put("startPage", startPage);
        json.put("pageSize", BaseConstant.DEFAULT_PAGE_SIZE);

        return exec(new Callback() {
            @Override
            public void exec() {
                got(userToyDao.getUserToyListByUserNo(userNo, startPage, BaseConstant.DEFAULT_PAGE_SIZE));
            }
        },"getUserToyByUserNo", json);
    }

    /**
     * 根据用户编号和id获得用户战利品记录
     * @param userNo 用户编号
     * @param id id
     * @return
     */
    @Override
    public CommonResult<UserToy> getUserToyByUserNoAndId(final String userNo, final Long id) {
        JSONObject json = new JSONObject();
        json.put("userNo", userNo);
        json.put("id", id);

        return exec(new Callback() {
            @Override
            public void exec() {
                // 邮寄娃娃所需游戏币数
                Integer deliverCoin = Integer.valueOf(toiletCatConfigService.getConfigByKey(
                        ToiletCatConfigConstant.USER_DELIVER_COIN));
                // 几个娃娃免费包邮
                Integer freeDeliverNum = Integer.valueOf(toiletCatConfigService.getConfigByKey(
                        ToiletCatConfigConstant.FREE_DELIVER_NUM));
                UserToy userToy = userToyDao.getUserToyByUserNoAndId(userNo, id);
                // 设置邮寄所需游戏币数
                userToy.setDeliverCoin(deliverCoin);
                // 设置几个娃娃免费包邮
                userToy.setFreeDeliverNum(freeDeliverNum);
                got(userToy);
            }
        },"getUserToyByUserNoAndId", json);
    }

    /**
     * 根据id,用户编号修改选择方式
     * @param userToy 用户玩具
     * @param userAddress 用户地址
     * @param toyNameArray 玩具名集合
     * @param toyNoList 玩具编号集合
     * @param forCoinNum 兑换成钱币的数量
     */
    @Override
    public CommonResult<String> updateChoiceTypeByIdAndUserNo(final UserToy userToy, final UserAddress userAddress,
                                                              final String toyNameArray, final List<String> toyNoList,
                                                              final Integer forCoinNum) {

        JSONObject json = new JSONObject();
        json.put("userToy", userToy);
        json.put("userAddress", userAddress);
        json.put("toyNameArray", toyNameArray);
        json.put("toyNoList", toyNoList);
        json.put("forCoinNum", forCoinNum);

        return exec(new Callback() {
            @Override
            public void exec() {

                // 选择类型
                Integer choiceType = userToy.getChoiceType();
                // 用户编号
                String userNo = userToy.getUserNo();

                // 若是选择快递
                if(ChoiceType.FOR_DELIVER.getStatus() == choiceType) {

                    Deliver deliver = new Deliver();
                    deliver.setToyNameArray(toyNameArray);
                    deliver.setUserNo(userNo);
                    deliver.setAddress(userAddress.getAddress());
                    deliver.setMobileNo(userAddress.getMobileNo());
                    deliver.setUserName(userAddress.getUserName());
                    deliver.setDeliverStatus(DeliverStatus.INIT.getStatus());

                    deliverDao.addDeliver(deliver);

                    // 几个娃娃免费包邮
                    Integer freeDeliverNum = Integer.valueOf(toiletCatConfigService.getConfigByKey(
                            ToiletCatConfigConstant.FREE_DELIVER_NUM));

                    // 若寄送娃娃少于包邮个数则扣除相应游戏币作为邮寄费
                    if(toyNoList.size() < freeDeliverNum) {
                        // 获取邮寄费
                        Integer deliverCoin = Integer.valueOf(toiletCatConfigService.getConfigByKey(
                                ToiletCatConfigConstant.USER_DELIVER_COIN));

                        Integer userCoin = userDao.getUserCoinByUserNo(userNo);

                        if(userCoin < deliverCoin) {
                            setOtherMsg();
                            got(BaseConstant.NOT_ENOUGH_COIN);
                            return;
                        }

                        userDao.updateUserCoinByUserNo(-deliverCoin, userNo);

                        logger.info("updateChoiceTypeByIdAndUserNo 扣除用户邮寄费游戏币成功");
                        UserSpendRecord userSpendRecord = new UserSpendRecord();

                        Date tradeTime = new Date();

                        userSpendRecord.setTradeStatus(TradeStatus.SUCCESS.getStatus());

                        userSpendRecord.setTradeType(TradeType.DELIVER_COIN.getType());

                        userSpendRecord.setTradeTime(tradeTime);

                        userSpendRecord.setTradeDate(DateUtil.getDate());

                        userSpendRecord.setUserNo(userNo);

                        // 邮寄游戏币数
                        userSpendRecord.setCoin(deliverCoin);

                        // 订单号
                        String deliverOrderNo = BaseConstant.TOILER_CAT + tradeTime.getTime() + userNo;

                        // 设置订单号
                        userSpendRecord.setOrderNo(deliverOrderNo);

                        userSpendRecordDao.addUserSpendRecord(userSpendRecord);

                        logger.info("updateChoiceTypeByIdAndUserNo 添加用户消费记录成功");
                    }

                    for(String toyNo : toyNoList) {

                        // 获得玩具信息
                        Toy toyInfo = toyDao.getToyInfoByToyNo(toyNo);

                        // 用户战利品处理bean
                        UserToyHandle userToyHandle = new UserToyHandle();

                        // 用户编号
                        userToyHandle.setUserNo(userNo);

                        // 选择类型
                        userToyHandle.setChoiceType(choiceType);

                        // 发货id
                        userToyHandle.setDeliverId(deliver.getId());

                        // 玩具编号
                        userToyHandle.setToyNo(toyNo);

                        userToyHandle.setToyName(toyInfo.getToyName());

                        userToyHandle.setToyImg(toyInfo.getToyImg());

                        userToyHandle.setForCoinNum(0);

                        userToyHandle.setToyForCoin(0);


                        // TODO: 2018/2/2 添加insert操作

                        // 获取兑换数量的战利品更改选择方式
                        List<Long> userToyIdList = userToyDao.getLimitUserToyIdListByUserNoAndToyNo(userNo,
                                toyNo, toyInfo.getDeliverNum());

                        for(Long id : userToyIdList) {

                            UserToy newUserToy = new UserToy();

                            newUserToy.setId(id);

                            newUserToy.setUserNo(userNo);

                            newUserToy.setChoiceType(choiceType);

                            userToyDao.updateChoiceTypeByIdAndUserNo(newUserToy);
                        }
                    }

                    // 若为兑换游戏币
                } else if(ChoiceType.FOR_COIN.getStatus() == choiceType) {

                    Date tradeTime = new Date();

                    // 获得玩具信息
                    Toy toyInfo = toyDao.getToyInfoByToyNo(userToy.getToyNo());

                    Integer coin = userToy.getToyForCoin() * forCoinNum;

                    // 添加相应的游戏币给用户
                    userDao.updateUserCoinByUserNo(coin, userNo);

                    // 用户战利品处理bean
                    UserToyHandle userToyHandle = new UserToyHandle();

                    userToyHandle.setUserNo(userNo);

                    userToyHandle.setChoiceType(choiceType);

                    userToyHandle.setDeliverId(0L);

                    userToyHandle.setToyNo(userToy.getToyNo());

                    userToyHandle.setToyName(toyInfo.getToyName());

                    userToyHandle.setToyImg(toyInfo.getToyImg());

                    userToyHandle.setForCoinNum(forCoinNum);

                    userToyHandle.setToyForCoin(coin);


                    // TODO: 2018/2/2 添加insert操作

                    UserSpendRecord userSpendRecord = new UserSpendRecord();
                    // 用户编号
                    userSpendRecord.setUserNo(userNo);
                    // 玩具兑换游戏币数
                    userSpendRecord.setCoin(coin);
                    // 交易日期
                    userSpendRecord.setTradeDate(DateUtil.getDate());
                    // 交易时间
                    userSpendRecord.setTradeTime(tradeTime);
                    // 交易状态 成功
                    userSpendRecord.setTradeStatus(TradeStatus.SUCCESS.getStatus());
                    // 交易类型 玩具兑换成游戏币
                    userSpendRecord.setTradeType(TradeType.TOY_FOR_COIN.getType());

                    // 订单号
                    String toyForCoinOrderNo = BaseConstant.TOILER_CAT + tradeTime.getTime() + userNo;

                    // 设置订单号
                    userSpendRecord.setOrderNo(toyForCoinOrderNo);


                    userSpendRecordDao.addUserSpendRecord(userSpendRecord);

                    userToyDao.updateChoiceTypeByIdAndUserNo(userToy);
                }

                Integer userCoin = userDao.getUserCoinByUserNo(userNo);

                JSONObject json = new JSONObject();

                json.put("userCoin",userCoin);

                got(json.toJSONString());

            }
        }, "updateChoiceTypeByIdAndUserNo", json);
    }

    /**
     * 根据用户编号获得用户所有未处理战利品
     * @param userNo 用户编号
     * @return
     */
    @Override
    public CommonResult<List<UserToy>> getAllUnHandleUserToyByUserNo(final String userNo) {
        JSONObject json = new JSONObject();
        json.put("userNo", userNo);

        return exec(new Callback() {
            @Override
            public void exec() {
                got(userToyDao.getAllUnHandleUserToyByUserNo(userNo));
            }
        }, "getAllUnHandleUserToyByUserNo", json);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
