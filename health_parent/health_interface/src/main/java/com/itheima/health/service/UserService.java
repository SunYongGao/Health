package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserService {
    /**
     * 通过用户名查询用户信息 包含角色与权限
     * @param username
     * @return
     */
    User findByUsername(String username);
}
