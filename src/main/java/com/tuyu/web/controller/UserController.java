package com.tuyu.web.controller;

import com.tuyu.web.annotation.RequestMapping;
import com.tuyu.web.annotation.RestController;
import com.tuyu.web.common.ReturnData;
import com.tuyu.web.util.ReturnUtils;
import lombok.Data;

/**
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/tuyu")
    public ReturnData get() {
        User user = new User();
        user.setName("tuyu");
        user.setAge(12);
        return ReturnUtils.ofSuccess(user);
    }

    @Data
    private static class User{
        private String name;
        private Integer age;
    }
}
