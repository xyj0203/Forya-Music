package com.example.music.entity.pojo.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author Xuyujie
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@TableName(value = "user_permission")
public class Permission {
    @TableField("permission_id")
    @ApiModelProperty(value = "权限id",hidden = true)
    private int permissionId;
    @ApiModelProperty("权限形容")
    @NotNull
    @Length(min = 1,message = "权限形容不能为空")
    @TableField("description")
    private String description;
    @ApiModelProperty("权限路径")
    @NotNull
    @Length(min = 1,message = "权限路径不能为空")
    @TableField("path")
    private String path;
    @NotNull
    @Length(min = 1,message = "权限名称不能为空")
    @ApiModelProperty("权限名称")
    @TableField("name")
    private String name;


}
