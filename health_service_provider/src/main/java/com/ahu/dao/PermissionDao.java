package com.ahu.dao;

import com.ahu.pojo.Permission;

import java.util.Set;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/1
 * @description ：
 * @version: 1.0
 */
public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
