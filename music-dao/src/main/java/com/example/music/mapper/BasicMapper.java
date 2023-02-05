package com.example.music.mapper;

import com.example.music.entity.pojo.Entity.User;
import org.apache.ibatis.annotations.Param;

public interface BasicMapper {
    /**
     * 注册用户
     * @param user
     * @return
     */
    Integer register(User user);

    /**
     * 通过邮箱查询用户
     * @param email
     * @return
     */
    User selectUserByEmail(String email);

    /**
     * 通过用户名查询用户
     * @param account
     * @return
     */
    User selectUserByAccount(String account);

    /**
     * 设置用户的角色
     * @param userId
     * @param role
     * @return
     */
    int setUserRole(@Param("userId") Long userId, @Param("role") int role);
}
