package com.ahu.security;

import com.ahu.pojo.Permission;
import com.ahu.pojo.Role;
import com.ahu.pojo.User;
import com.ahu.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/1
 * @description ：
 * @version: 1.0
 */
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    //根据页面输入的用户名查询用户的信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(username)){
            return null;
        }

        //查询数据库，如果不为空
        User user = userService.findByUserName(username);
        //如果查询为空则返回null
        if(user == null){
            return null;
        }

        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }
}
