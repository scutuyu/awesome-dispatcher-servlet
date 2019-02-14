package com.tuyu.web.controller;

import com.tuyu.web.annotation.RequestMapping;
import com.tuyu.web.annotation.RestController;
import com.tuyu.web.common.ReturnData;
import com.tuyu.web.util.ReturnUtils;
import lombok.Data;

/**
 * User控制器
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 查询用户信息
     *
     * @return
     */
    @RequestMapping("/tuyu")
    public ReturnData get() {
        User user = new User();
        user.setName("tuyu");
        user.setAge(12);
        return ReturnUtils.ofSuccess(user);
    }

    /**
     * 用户实体
     */
    @Data
    private static class User{
        /**
         * 姓名
         */
        private String name;
        /**
         * 年龄
         */
        private Integer age;
    }
}
