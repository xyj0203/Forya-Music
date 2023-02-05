package com.example.music.Controller.AdminController;

import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.constraints.NotNull;

/**
 * @author Xuyujie
 */
@Controller
@RequestMapping("/userAdmin")
@Api(tags = "用户管理")
@ResponseBody
@Validated
public class UserAdminController {

    @Autowired
    private UserService userService;

    @ApiOperation("查询用户信息")
    @GetMapping("/queryUserByPage")
    public ResultObjectModel queryUserByPage() {
         return ResultObjectModel.success(userService.queryForAll());
    }

    @ApiOperation("通过账户查询用户信息")
    @GetMapping("/queryUserByWord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账户",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageNow", value = "当前页",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页面条数",paramType = "query")
    })
    public ResultObjectModel queryUserByWord(@NotNull(message = "账户不能为空！") String account,
                                             @NotNull @Range(min = 1,message = "页数不符合") Integer pageNow,
                                             @NotNull @Range(min = 1, message = "条数不符合")  Integer pageSize) {
        return ResultObjectModel.success(userService.queryByWord(account, pageNow, pageSize));
    }
}
