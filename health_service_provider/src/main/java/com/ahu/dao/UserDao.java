package com.ahu.dao;

import com.ahu.pojo.User;

/**
 * 登录用户Dao
 * @author ：XXXX
 * @date ：Created in 2020/12/1
 * @description ：
 * @version: 1.0
 */
public interface UserDao {
    User findByUserName(String username);
}
