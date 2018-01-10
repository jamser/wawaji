package com.toiletCat.template;

import com.toiletCat.bean.Callback;
import com.toiletCat.bean.CommonResult;
import com.toiletCat.constants.BaseConstant;
import com.toiletCat.enums.CommonCodeMessage;
import org.slf4j.Logger;

public final class ExecTemplate {

    /**
     * 公共执行方法
     * @param callback 回调函数
     * @param logger 日志
     * @param method 方法名
     * @param params 参数
     * @return
     */
    public static CommonResult exec(Callback callback, Logger logger, String method, String params) {
        logger.info(method + " start params:" + params);

        CommonResult commonResult = new CommonResult();

        try {
            callback.setResult(commonResult);
            callback.exec();
        } catch (Throwable e) {
            logger.error(BaseConstant.LOG_ERR_MSG +" "+ method + " params:" + params + "error: " + e, e);
            callback.setCodeMessage(CommonCodeMessage.SYSTEM_ERROR);
        }

        logger.info(method + " end");

        return callback.getResult();
    }


}