package com.tuyu.web.common;

import lombok.Getter;

/**
 * 返回状态枚举
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
@Getter
public enum ReturnStatus {
    SUCCESS("0", "成功"),
    FAIL("1", "失败"),
    TIME_OUT("1001", "超时"),
    DATA_NOT_EXIST("1002", "数据不存在")
    ;


    private String code;
    private String msg;

    ReturnStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
