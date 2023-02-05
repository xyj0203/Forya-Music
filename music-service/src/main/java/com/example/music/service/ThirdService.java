package com.example.music.service;

import com.example.music.entity.pojo.Entity.ThirdUser;

public interface ThirdService {
    /**
     * 载入第三方信息
     * @param thirdUser
     * @return
     */
    ThirdUser loadThirdUser(ThirdUser thirdUser);
}
