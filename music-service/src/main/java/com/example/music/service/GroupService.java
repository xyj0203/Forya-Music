package com.example.music.service;

import com.example.music.entity.pojo.Entity.Group;
import com.example.music.entity.pojo.ResultObjectModel;
import org.springframework.web.multipart.MultipartFile;

public interface GroupService {
    /**
     * 创建群组
     * @param group
     * @param file
     * @return
     */
    ResultObjectModel createGroup(Group group, MultipartFile file);
}
