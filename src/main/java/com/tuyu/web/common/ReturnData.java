package com.tuyu.web.common;

import lombok.Data;

/**
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
@Data
public class ReturnData<T> {

    private String code;
    private String msg;
    private T data;
}
