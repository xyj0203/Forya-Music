package com.example.music.utils;

import com.example.music.entity.pojo.Entity.User;
import com.example.music.entity.pojo.Vo.UserVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Xuyujie
 */
@Component
public class BeanUtils {
    @Resource
    private ComponentUtil compontUtil;
    @Resource
    private RedisUtils redisUtils;

    public UserVo userToUserVo(User user){
        UserVo userVo = new UserVo();
        org.springframework.beans.BeanUtils.copyProperties(user,userVo);
        Integer sex = user.getSex();
        if (sex == null){
            userVo.setSex("未知");
        }else if (sex == 0){
            userVo.setSex("男");
        }else if (sex == 1){
            userVo.setSex("女");
        }
        Date birthday = user.getBirthday();
        if (birthday == null){
            userVo.setBirthday("未知");
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(birthday);
            userVo.setBirthday(format);
            userVo.setAge(compontUtil.yearDateDiff(birthday.getTime(),System.currentTimeMillis()));
        }
        boolean bit = redisUtils.getBit(RedisKeyUtils.ONLINE_STATE, user.getUserId());
        if (bit){
            userVo.setOnline("在线");
        }else {
            userVo.setOnline("离线");
        }
        return userVo;
    }
}
