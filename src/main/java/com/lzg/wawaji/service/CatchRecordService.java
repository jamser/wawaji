package com.lzg.wawaji.service;

import com.lzg.wawaji.bean.CommonResult;
import com.lzg.wawaji.bean.UserCatchRecord;
import com.lzg.wawaji.entity.CatchRecord;

import java.util.List;

public interface CatchRecordService {

    /**
     * 根据玩具编号获得最近成功的抓取记录(10条)
     * @param toyNo 玩具编号
     * @return
     */
    CommonResult<List<UserCatchRecord>> getLatelyCatchSuccessRecordByToyNo(String toyNo);

    /**
     * 根据用户编号获得用户抓取记录数量
     * @param userNo 用户编号
     * @return
     */
    CommonResult<Integer> countCatchRecordByUserNo(String userNo);

    /**
     * 根据用户编号分页获得用户抓取记录集合
     * @param userNo 用户编号
     * @param startPage 开始页
     * @return
     */
    CommonResult<List<CatchRecord>> getCatchRecordListByUserNo(String userNo, int startPage);

    /**
     * 根据用户编号,id获得用户抓取记录
     * @param id id
     * @param userNo 用户编号
     * @return
     */
    CommonResult<CatchRecord> getCatchRecordByUserNo(Long id, String userNo);

    /**
     * 根据id,用户编号修改抓取记录状态
     * @param catchStatus 抓去记录状态
     * @param id id
     * @param userNo 用户编号
     */
    CommonResult updateCatchStatusByIdAndUserNo(Integer catchStatus, Long id, String userNo);
}
