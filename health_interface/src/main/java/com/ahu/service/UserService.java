package com.ahu.service;

import com.ahu.pojo.User;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/1
 * @description ：
 * @version: 1.0
 */
public interface UserService {
    public User findByUserName(String username);
}
