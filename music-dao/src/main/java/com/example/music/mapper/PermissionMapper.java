package com.example.music.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.music.entity.pojo.Entity.Permission;
import com.example.music.entity.pojo.Entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

    List<Permission> findAllByUserId(Long roleId);

    /**
     * 通过等级查询权限
     * @param level
     * @return
     */
    List<String> selectPermissionByLevel(Integer level);

    /**
     * 通过等级查询所对应的操作权限列表
     * @param parseInt
     * @return
     */
    List<String> getNameByLevel(int parseInt);
    /**
     * 查找角色对应的权限
     * @param id
     * @return
     */
    List<Permission> selectPermissionById(Integer id);

    /**
     * 获取所有角色
     * @return
     */
    List<Role> findAllRole();

    /**
     * 添加角色
     * @param role
     * @return
     */
    int addRole(Role role);

    /**
     * 批量删除
     * @param roleIds
     * @return
     */
    int deleteRoles(List<Integer> roleIds);

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    int updateRole(Role role);

    /**
     * 批量添加关联信息
     * @param roleId
     * @param permissionsId
     * @return
     */
    int addPermissionForRole(@Param("roleId") Integer roleId, @Param("list") List<Integer> permissionsId);

    /**
     * 查找用户的角色
     * @param userId
     * @return
     */
    List<String> selectUserRole(Long userId);
}
