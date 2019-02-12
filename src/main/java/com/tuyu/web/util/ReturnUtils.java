package com.tuyu.web.util;

import com.tuyu.web.common.ReturnData;
import com.tuyu.web.common.ReturnStatus;

/**
 * web层相应请求工具类
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public class ReturnUtils {

    private static final ReturnStatus SUCCESS = ReturnStatus.SUCCESS;
    private static final ReturnStatus FAIL = ReturnStatus.FAIL;

    /**
     * 成功
     * @param <T>
     *
     * @return
     */
    public static <T> ReturnData<T> ofSuccess() {
        ReturnData<T> returnData = new ReturnData<T>();
        returnData.setCode(SUCCESS.getCode());
        returnData.setMsg(SUCCESS.getMsg());
        return null;
    }

    /**
     *  成功
     *
     * @param data 返回的数据
     * @param <T>
     *
     * @return
     */
    public static <T> ReturnData<T> ofSuccess(T data) {
        ReturnData<T> returnData = ofSuccess();
        returnData.setData(data);
        return returnData;
    }

    /**
     * 失败
     *
     * @param <T>
     *
     * @return
     */
    public static <T> ReturnData<T> ofFail() {
        return ofFail(FAIL, null);
    }

    /**
     * 失败
     *
     * @param returnStatus 返回状态枚举
     * @param msg 提示信息
     * @param <T>
     *
     * @return
     */
    public static <T> ReturnData<T> ofFail(ReturnStatus returnStatus, String msg) {
        ReturnData<T> returnData = new ReturnData<T>();
        returnData.setCode(returnStatus.getCode());
        returnData.setMsg(StringUtils.isEmpty(msg) ? returnStatus.getMsg() : msg);
        return returnData;
    }
}
