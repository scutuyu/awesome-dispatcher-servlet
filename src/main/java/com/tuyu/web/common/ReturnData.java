package com.tuyu.web.common;

import lombok.Data;

/**
 * 封装相应消息的类
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
@Data
public class ReturnData<T> {

    /**
     * 状态码
     */
    private String code;
    /**
     * 提示消息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
}
