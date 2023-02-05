package com.example.music.service.ServiceImpl;

import com.example.music.entity.pojo.Entity.ThirdUser;
import com.example.music.mapper.ThirdMapper;
import com.example.music.service.ThirdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ThirdServiceImpl implements ThirdService {

    @Resource
    private ThirdMapper thirdMapper;

    @Override
    public ThirdUser loadThirdUser(ThirdUser thirdUser) {
        thirdMapper.updateifexist(thirdUser);
        ThirdUser user = thirdMapper.selectByThirdId(thirdUser.getThirdAccountId());
        return user;
    }
}
