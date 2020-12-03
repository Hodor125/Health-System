package com.ahu.service;

import com.ahu.dao.PermissionDao;
import com.ahu.dao.RoleDao;
import com.ahu.dao.UserDao;
import com.ahu.pojo.Permission;
import com.ahu.pojo.Role;
import com.ahu.pojo.User;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/1
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public User findByUserName(String username) {
        User user = userDao.findByUserName(username);
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roleSet = roleDao.findByUserId(userId);
        //如果查询到的权限集合不为空
        if(roleSet != null && roleSet.size() > 0){
            //遍历查询permission
            for (Role role : roleSet) {
                Integer roleId = role.getId();
                Set<Permission> permissionSet =  permissionDao.findByRoleId(roleId);
                role.setPermissions(permissionSet);
            }
            user.setRoles(roleSet);
        }
        return user;
    }
}
