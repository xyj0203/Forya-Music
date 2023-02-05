package com.example.music.service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.music.entity.pojo.Dto.UserDto;
import com.example.music.entity.pojo.Entity.User;
import com.example.music.entity.pojo.Vo.UserVo;
import com.example.music.mapper.UserMapper;
import com.example.music.service.UserService;
import com.example.music.utils.BeanUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuyujie
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private BeanUtils beanUtils;

    @Override
    public List<UserVo> queryForAll() {
        return queryByPage(1,10);
    }

    @Override
    public List<UserVo> queryByPage(int pageNow, int pageSize) {
        return queryByWord(null,pageNow,pageSize);
    }

    @Override
    public List<UserVo> queryByWord(String word, int pageNow, int pageSize) {
        PageHelper.startPage(pageNow,pageSize);
        List<User> users = word == null ?
                userMapper.selectList(null) :
                userMapper.selectList(new QueryWrapper<User>().eq("account",word));
        List<UserVo> userVos = new ArrayList<>();
        for (User user : users) {
            UserVo userVo = beanUtils.userToUserVo(user);
            userVos.add(userVo);
        }
        return userVos;
    }

    @Override
    public List<UserVo> queryByCondition(UserDto userDto, int pageNow, int pageSize) {
        return null;
    }
}
