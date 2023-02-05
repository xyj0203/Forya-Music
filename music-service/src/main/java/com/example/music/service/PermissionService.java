package com.example.music.service;

import com.example.music.entity.pojo.Entity.Permission;
import com.example.music.entity.pojo.Entity.Role;
import com.example.music.entity.pojo.ResultObjectModel;

import java.util.List;

public interface PermissionService {

    /**
     * 添加权限
     * @param permission
     * @return
     */
    ResultObjectModel addPermission(Permission permission);

    /**
     * 删除权限
     * @param permissionIds
     * @return
     */
    ResultObjectModel deletePermission(List<Integer> permissionIds);

    /**
     * 修改权限
     * @param permission
     * @return
     */
    ResultObjectModel modifyPermission(Permission permission);

    /**
     * 获取所有权限
     * @return
     */
    ResultObjectModel getAllPermissions();

    /**
     * 加载权限信息至缓存
     */
    void loadPermissionCache();

    /**
     * 加载所有角色至缓存
     */
    void loadRoleCache();

    /**
     * 获取所有角色
     * @return
     */
    ResultObjectModel selectForAllRole();

    /**
     * 添加权限
     * @param role
     * @return
     */
    ResultObjectModel addRole(Role role);

    /**
     * 删除角色
     * @param roleIds
     * @return
     */
    ResultObjectModel deleteRole(List<Integer> roleIds);

    /**
     * 修改权限
     * @param role
     * @return
     */
    ResultObjectModel modifyRole(Role role);


    /**
     * 为角色添加权限
     * @return
     */
    ResultObjectModel addPermissionForRole(Integer roleId, List<Integer> permissionsId);

    /**
     * 获取指定角色的权限
     * @param id
     * @return
     */
    ResultObjectModel getRolePermissionById(Integer id);
}
