package com.example.music.service;

import com.example.music.entity.pojo.ResultObjectModel;

public interface BasicService {
    /**
     * 注册
     * @param email
     * @param code
     * @param password
     * @param account
     * @return
     */
    ResultObjectModel register(String email, String code,  String password,  String account);
}
