package com.toiletCat.controller;

import com.alibaba.fastjson.JSON;
import com.toiletCat.bean.CommonResult;
import com.toiletCat.constants.BaseConstant;
import com.toiletCat.entity.Toy;
import com.toiletCat.service.ToyService;
import com.toiletCat.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/toiletCat/api/toy")
@Controller
public class ToyController {

    @Autowired
    private ToyService toyService;

    /**
     * 分页获得所有玩具记录
     * @param startPage 开始页
     * @return
     */
    @RequestMapping(value = "/getAllToyByPage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getAllToyByPage(int startPage) {

        CommonResult<List<Toy>> result = toyService.getAllToyByPage(startPage);

        return JSONUtil.getReturnBeanString(result);
    }

    /**
     * 获得总记录数和每页数据数
     * @return
     */
    @RequestMapping(value = "/getTotalCountAndPageSize", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTotalCountAndPageSize() {

        CommonResult<Integer> result = toyService.countAllToy();

        return JSONUtil.getTotalCountAndPageSize(result, BaseConstant.DEFAULT_PAGE_SIZE);
    }

    /**
     * 添加玩具记录
     * @param paramStr 玩具记录
     * @return
     */
    @RequestMapping(value = "/addToy", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addToy(String paramStr) {

        CommonResult result = toyService.addToy(JSON.parseObject(paramStr, Toy.class));

        return JSONUtil.getReturnStrString(result, BaseConstant.SUCCESS);
    }

    /**
     * 根据id和玩具编号获得玩具信息
     * @param id id
     * @param dataNo 玩具编号
     * @return
     */
    @RequestMapping(value = "/getToyByIdAndToyNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getToyByIdAndToyNo(Long id, String dataNo) {

        CommonResult<Toy> result = toyService.getToyByIdAndToyNo(id, dataNo);

        return JSONUtil.getReturnStrString(result, String.valueOf(result.getValue()));
    }

    /**
     * 根据id和玩具编号修改玩具记录
     * @param paramStr 玩具记录
     * @return
     */
    @RequestMapping(value = "/updateToyByIdAndToyNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateToyByIdAndToyNo(String paramStr) {

        CommonResult result = toyService.updateToyByIdAndToyNo(JSON.parseObject(paramStr, Toy.class));

        return JSONUtil.getReturnStrString(result, BaseConstant.SUCCESS);
    }

    /**
     * 根据id和玩具编号删除记录
     * @param id id
     * @param toyNo 玩具编号
     * @return
     */
    @RequestMapping(value = "/deleteToyByIdAndToyNo", method = RequestMethod.POST)
    @ResponseBody
    public String deleteToyByIdAndToyNo(Long id, String toyNo) {

        CommonResult result = toyService.deleteToyByIdAndToyNo(id, toyNo);

        return JSONUtil.getReturnStrString(result, BaseConstant.SUCCESS);
    }

    /**
     * 获得所有可用玩具信息
     * @return
     */
    @RequestMapping(value = "/getAllAvailableToy", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getAllAvailableToy() {

        CommonResult<List<Toy>> result = toyService.getAllAvailableToy();

        return JSONUtil.getReturnBeanString(result);
    }
}
