package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/2
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUserName")
    public Result getUserName(){
        try {
            //返回上下文对象
            SecurityContext securityContext = SecurityContextHolder.getContext();
            //获取认证信息
            Authentication authentication = securityContext.getAuthentication();
            //获取签名，是框架的User
            User user = (User) authentication.getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
