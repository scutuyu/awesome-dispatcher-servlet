package com.tuyu.web.controller;

import com.tuyu.web.annotation.*;
import com.tuyu.web.common.ReturnData;
import com.tuyu.web.util.ReturnUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.UUID;

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
     * <pre>
     *     request eg: http://localhost:8080/user/tuyu
     * </pre>
     *
     * @return
     */
    @RequestMapping("/tuyu")
    public ReturnData get() {
        User user = User.createUser();
        return ReturnUtils.ofSuccess(user);
    }

    /**
     * 查询用户信息
     * <pre>
     *     request eg: http://localhost:8080/user/21
     * </pre>
     * @return
     */
    @RequestMapping("/{id}")
    public ReturnData get(@PathVariable("id") Integer id) {
        User user = User.createUser();
        user.setId(id);
        return ReturnUtils.ofSuccess(user);
    }

    /**
     * 查询用户信息
     * <pre>
     *     request eg: http://localhost:8080/user/new?user_name=tuyu&user_age=11
     * </pre>
     * @return
     */
    @RequestMapping("/new")
    public ReturnData get(String userName, Integer userAge) {
        User user = User.createUser();
        user.setName(userName);
        user.setAge(userAge);
        return ReturnUtils.ofSuccess(user);
    }

    /**
     * 查询用户信息
     * <pre>
     *     request eg: http://localhost:8080/user/new2?user_name=tuyu&user_age=11
     * </pre>
     * @return
     */
    @RequestMapping("/new2")
    public ReturnData get2(User user) {
        return ReturnUtils.ofSuccess(user);
    }

    /**
     * 查询用户信息
     * <pre>
     *     request eg: http://localhost:8080/user/new3?user_name=tuyu&user_age=11
     * </pre>
     * @return
     */
    @PostMapping("/new3")
    public ReturnData get3(@RequestBody User user) {
        return ReturnUtils.ofSuccess(user);
    }

    /**
     * 用户实体
     */
    @Data
    public static class User{

        private final static Random RANDOM = new Random();
        /**
         * id
         */
        private Integer id;
        /**
         * 姓名
         */
        private String name;
        /**
         * 年龄
         */
        private Integer age;

        public static User createUser() {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setAge(RANDOM.nextInt(50));
            return user;
        }

        public static void main(String[] args) {
            Field[] fields = new User().getClass().getDeclaredFields();
            for (Field f : fields) {
                System.out.println(f.getName() + " " + Modifier.isStatic(f.getModifiers()));
            }
        }
    }
}
