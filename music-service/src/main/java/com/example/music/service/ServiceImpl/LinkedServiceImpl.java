package com.example.music.service.ServiceImpl;

import com.example.music.entity.pojo.Entity.Apply;
import com.example.music.entity.pojo.Entity.User;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.entity.pojo.Vo.ApplyVo;
import com.example.music.entity.pojo.Vo.UserVo;
import com.example.music.mapper.BasicMapper;
import com.example.music.mapper.UserMapper;
import com.example.music.service.LinkedService;
import com.example.music.utils.BeanUtils;
import com.example.music.utils.ComponentUtil;
import com.example.music.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkedServiceImpl implements LinkedService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private BasicMapper basicMapper;
    @Autowired
    private ComponentUtil compontUtil;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private BeanUtils beanUtils;


    @Override
    public boolean isLinked(Long userId, Long friendId) {
        if (userMapper.isLinked(userId, friendId) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public ResultObjectModel searchLinkedMan(String account) {
        User user = basicMapper.selectUserByAccount(account);
        if (user == null) {
            return ResultObjectModel.fail("没有该用户");
        }
        UserVo userVo = beanUtils.userToUserVo(user);
        return ResultObjectModel.success(userVo);
    }

    @Override
    public ResultObjectModel addLinkedMan(Long userId, Long friendId,String sendContent) {
        if (isLinked(userId, friendId)) {
            return ResultObjectModel.fail("已经是好友");
        }
        Apply apply = new Apply(null,userId,friendId,sendContent);
        int i = userMapper.addLinkedMan(apply);
        if (i == 1){
            return ResultObjectModel.success("发送申请成功");
        }
        return ResultObjectModel.success("发送申请失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultObjectModel acceptLinkedMan(Long userId, Long friendId, Integer status,Long applyId) {
        if (status == 0){
            return ResultObjectModel.fail("状态不能修改");
        }
        if (isLinked(userId, friendId)) {
            return ResultObjectModel.fail("已经是好友");
        }
        int i = userMapper.updateApplyStatus(applyId, status);
        if (i == 1){
            if(status == 1){
                int i1 = userMapper.addLinkedMantogether(userId, friendId);
                int i2 = userMapper.addLinkedMantogether(friendId, userId);
                if (i1 == 1 && i2 == 1){
                    return ResultObjectModel.success("添加好友成功");
                }
                return ResultObjectModel.success("添加好友失败");
            }
        }else{
            return ResultObjectModel.success("状态修改失败");
        }
        return ResultObjectModel.success("状态修改失败");
    }

    @Override
    public ResultObjectModel getApplyList(Long userId) {
        System.out.println("userId:"+userId);
        List<ApplyVo> applyVos = userMapper.queryApplyList(userId);
        System.out.println(applyVos.size());
        for (ApplyVo applyVo : applyVos) {
            switch (applyVo.getApplyState()){
                case 0:
                    applyVo.setApplyStateStr("待处理");
                    break;
                case 1:
                    applyVo.setApplyStateStr("已同意");
                    break;
                case 2:
                    applyVo.setApplyStateStr("已拒绝");
                    break;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(applyVo.getApplyTime());
            applyVo.setApplyTimeStr(format);
            applyVo.setApplyUserSex(applyVo.getApplyUserSexValue() == 0 ? "男" : "女");
            if (applyVo.getApplyUserBirthdayTime() == null){
                applyVo.setApplyUserAge(0);
            }else{
                applyVo.setApplyUserAge(compontUtil.yearDateDiff(applyVo.getApplyUserBirthdayTime().getTime(),System.currentTimeMillis()));
            }
        }
        return ResultObjectModel.success(applyVos);
    }

    @Override
    public ResultObjectModel getLinkedManList(Long userId) {
        List<User> users = userMapper.queryLinkedManList(userId);
        List<UserVo> userVos = new ArrayList<>();
        for (User user : users) {
            UserVo userVo = beanUtils.userToUserVo(user);
            userVos.add(userVo);
        }
        return ResultObjectModel.success(userVos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultObjectModel deleteLinkedMan(Long userId) {
        int i = userMapper.deleteLinkedMan(userId);
        if (i == 2){
            return ResultObjectModel.success("删除成功");
        }
        return ResultObjectModel.fail("删除失败");
    }
}
