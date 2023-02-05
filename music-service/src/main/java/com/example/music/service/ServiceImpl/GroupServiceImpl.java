package com.example.music.service.ServiceImpl;

import com.example.music.entity.pojo.Entity.Group;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GroupServiceImpl implements GroupService {

    @Override
    public ResultObjectModel createGroup(Group group, MultipartFile file) {
        return null;
    }
}
