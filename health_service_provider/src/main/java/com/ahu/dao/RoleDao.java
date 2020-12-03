package com.ahu.dao;

import com.ahu.pojo.Role;

import java.util.Set;

/**
 * @author ：XXXX
 * @date ：Created in 2020/12/1
 * @description ：
 * @version: 1.0
 */
public interface RoleDao {
    Set<Role> findByUserId(Integer userId);
}
