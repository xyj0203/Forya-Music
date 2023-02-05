package com.example.music.entity.pojo.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**用户的视图筛选对象
 * @author Xuyujie
 */
@Data
public class UserDto {
    @ApiModelProperty(value = "性别")
    private Character sex;
}
