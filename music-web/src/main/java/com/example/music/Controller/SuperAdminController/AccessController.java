package com.example.music.Controller.SuperAdminController;


import com.example.music.entity.pojo.Entity.Permission;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author Xuyujie
 */
@RestController
@Api(tags = "超级管理员操作")
@RequestMapping("/superAdmin")
public class AccessController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("查询所有的角色")
    @GetMapping("/getAllRoles")
    public ResultObjectModel selectForAllRole(){
        return permissionService.selectForAllRole();
    }


    @ApiOperation("超级管理员添加权限")
    @PostMapping("/addPermission")
    public ResultObjectModel addPermission(@Valid Permission permission){
        return permissionService.addPermission(permission);
    }

    @ApiOperation("超级管理员删除权限")
    @DeleteMapping("/deleteById")
    public ResultObjectModel deletePermission(@NotNull @RequestBody List<Integer> permissionIds){
        return permissionService.deletePermission(permissionIds);
    }

    @ApiOperation("超级管理员修改权限")
    @PutMapping("/modifyPermission")
    @ApiImplicitParam(name = "permissionId", value = "id", required = true, dataType = "Long", paramType = "query")
    public ResultObjectModel modifyPermission(@NotNull Integer permissionId,@Valid Permission permission){
        permission.setPermissionId(permissionId);
        return permissionService.modifyPermission(permission);
    }

    @ApiOperation("超级管理员查询所有权限")
    @GetMapping("/getAllPermission")
    public ResultObjectModel getAllPermission(){
        return permissionService.getAllPermissions();
    }

    @ApiOperation("为指定角色添加权限")
    @PostMapping("/BatchAddPmFRole")
    @ApiImplicitParam(name = "roleId", value = "roleId", required = true, dataType = "Integer", paramType = "query")
    public ResultObjectModel addPmFRole(@NotNull Integer roleId,@RequestBody List<Integer> list){
        return permissionService.addPermissionForRole(roleId,list);
    }

    @ApiOperation("查询指定角色的权限列表")
    @GetMapping("/getRolePms")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int",allowMultiple = true, paramType = "query")
    public ResultObjectModel getRolePms(Integer id){
        return permissionService.getRolePermissionById(id);
    }

    @ApiOperation("删除角色的权限")
    @DeleteMapping("batchDeleteRolePms")
    public ResultObjectModel deleteRolePms(@NotNull @RequestBody List<Integer> ids){
        return null;
    }
}
