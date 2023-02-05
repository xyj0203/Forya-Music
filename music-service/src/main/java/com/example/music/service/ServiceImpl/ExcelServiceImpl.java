package com.example.music.service.ServiceImpl;

import com.alibaba.excel.EasyExcel;
import com.example.music.entity.pojo.Entity.User;
import com.example.music.entity.pojo.Vo.UserVo;
import com.example.music.mapper.UserMapper;
import com.example.music.service.ExcelService;
import com.example.music.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private BeanUtils beanUtils;

    @Override
    public void generateUserExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户信息表", "UTF-8");
        response.setHeader("Content-disposition",
                "attachment;filename=" + fileName + ".xlsx");
        List<User> listUser = userMapper.queryLinkedManList(3L);
        List<UserVo> list = new ArrayList<>();
        listUser.forEach(e -> list.add(beanUtils.userToUserVo(e)));
        EasyExcel.write(response
                .getOutputStream(), UserVo.class)
                .sheet("table1").doWrite(list);
    }
}
