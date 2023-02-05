package com.example.music.service;

import com.example.music.entity.pojo.Dto.UserDto;
import com.example.music.entity.pojo.Vo.UserVo;

import java.util.List;

/**
 * @author Xuyujie
 */
public interface UserService {

    /**
     * 查询所有用户
     * @return
     */
    List<UserVo> queryForAll();

    /**
     * 分页查询
     * @param pageNow
     * @param pageSize
     * @return
     */
    List<UserVo> queryByPage(int pageNow, int pageSize);

    /**
     * 分页搜索按照关键词
     * @param word
     * @param pageNow
     * @param pageSize
     * @return
     */
    List<UserVo> queryByWord(String word, int pageNow, int pageSize);

    /**
     * 按条件查询
     * @param userDto
     * @param pageNow
     * @param pageSize
     * @return
     */
    List<UserVo> queryByCondition(UserDto userDto, int pageNow, int pageSize);
}
