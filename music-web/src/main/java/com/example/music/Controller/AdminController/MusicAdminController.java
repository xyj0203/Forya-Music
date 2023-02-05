package com.example.music.Controller.AdminController;

import com.example.music.entity.pojo.Dto.Music;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.annotation.OptLog;
import com.example.music.service.MusicService;
import com.example.music.utils.QiNiuYunUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.example.music.entity.constant.OptTypeConst.*;

/**
 * @author Xuyujie
 */
@Api(tags = "管理员音乐管理")
@RestController
@RequestMapping("/adminMusic")
@Validated
public class MusicAdminController {

    @Autowired
    private MusicService musicService;
    @Autowired
    private QiNiuYunUtils qiNiuYunUtils;

    @ApiOperation("添加分类")
    @PostMapping("/addClassify")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "content", value = "分类名称", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "type", value = "类型 0语种 1风格 2场景 3情感 4主题", required = true, dataType = "String")
            }
    )
    public ResultObjectModel addClassify(@Valid @NotNull String content, @NotNull @Range(min = 0, max = 4) Integer type) {
        return musicService.addClassify(content, type);
    }

    @ApiOperation("删除分类")
    @PostMapping("/deleteClassify")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "type", value = "类型 0语种 1风格 2场景 3情感 4主题", required = true, dataType = "String")
            }
    )
    @OptLog(optType = REMOVE)
    public ResultObjectModel deleteClassify(@Valid @NotNull Integer id, @NotNull @Range(min = 0, max = 4) Integer type) {
        return musicService.deleteClassify(id,type);
    }

    @ApiOperation("添加歌曲")
    @PostMapping("/addMusic")
    public ResultObjectModel addMusic(@NotNull Music music,
                                      @NotNull MultipartFile musicFile,
                                      @NotNull MultipartFile lyricFile) {

        ResultObjectModel resultObjectModel = qiNiuYunUtils.upload(2,musicFile);
        ResultObjectModel resultObjectModel1 = qiNiuYunUtils.upload(3,lyricFile);
        return null;
    }
}
