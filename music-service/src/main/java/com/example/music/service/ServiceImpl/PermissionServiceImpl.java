package com.example.music.service.ServiceImpl;


import com.example.music.entity.pojo.Entity.Permission;
import com.example.music.entity.pojo.Entity.Role;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.mapper.PermissionMapper;
import com.example.music.service.PermissionService;
import com.example.music.utils.RedisKeyUtils;
import com.example.music.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public ResultObjectModel addPermission(Permission permission) {
        int insert = permissionMapper.insert(permission);
        if (insert == 1){
            return ResultObjectModel.success("添加成功！");
        }
        return ResultObjectModel.fail("添加失败！");
    }

    @Override
    public ResultObjectModel deletePermission(List<Integer> permissionIds) {
        int i = permissionMapper.deleteBatchIds(permissionIds);
        if (i == permissionIds.size()){
            return ResultObjectModel.success("删除成功!");
        }
        return ResultObjectModel.success("删除失败！");
    }

    @Override
    public ResultObjectModel modifyPermission(Permission permission) {
        int i = permissionMapper.updateById(permission);
        if (i == 1){
            return ResultObjectModel.success("修改成功!");
        }
        return ResultObjectModel.success("修改失败！");
    }

    @Override
    public ResultObjectModel getAllPermissions() {
        List<Permission> all = permissionMapper.findAll();
        return ResultObjectModel.success(all);
    }

    @Override
    public void loadPermissionCache() {
        List<Role> roleList = (List<Role>)redisUtils.get(RedisKeyUtils.ROLE_LIST);
        for (Role role : roleList) {
            List<Permission> rolePermission = permissionMapper.selectPermissionById(role.getId());
            log.info("=======>{}:{}",role.getName(),rolePermission);
            redisUtils.set(RedisKeyUtils.ROLE_PERMISSION+role.getName(),rolePermission);
        }
    }

    @Override
    public void loadRoleCache() {
        List<Role> roleList = permissionMapper.findAllRole();
        log.info("=========>角色\n{}",roleList);
        redisUtils.set(RedisKeyUtils.ROLE_LIST,roleList);
    }

    @Override
    public ResultObjectModel selectForAllRole() {
        List<Role> roleList = (List<Role>)redisUtils.get(RedisKeyUtils.ROLE_LIST);
        return ResultObjectModel.success("请求成功",roleList);
    }

    @Override
    public ResultObjectModel addRole(Role role) {
        if (permissionMapper.addRole(role) == 1) {
            return ResultObjectModel.success();
        }
        return ResultObjectModel.fail();
    }

    @Override
    public ResultObjectModel deleteRole(List<Integer> roleIds) {
        if (permissionMapper.deleteRoles(roleIds) == roleIds.size()) {
            return ResultObjectModel.success();
        }
        return ResultObjectModel.fail();
    }

    @Override
    public ResultObjectModel modifyRole(Role role) {
        if (permissionMapper.updateRole(role) == 1) {
            return ResultObjectModel.success();
        }
        return ResultObjectModel.fail();
    }

    @Override
    public ResultObjectModel addPermissionForRole(Integer roleId, List<Integer> permissionsId) {
        if (permissionMapper.addPermissionForRole(roleId,permissionsId) == permissionsId.size()) {
            return ResultObjectModel.success();
        }
        return ResultObjectModel.fail();
    }

    @Override
    public ResultObjectModel getRolePermissionById(Integer id) {
        return null;
    }
}
