package com.example.music.utils;

import com.example.music.entity.pojo.Entity.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @description: 获取登录的用户信息
 * @author: xuyujie
 * @date: 2023/02/05
 **/
public class UserUtils {
    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static SecurityUser getSecurityUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
