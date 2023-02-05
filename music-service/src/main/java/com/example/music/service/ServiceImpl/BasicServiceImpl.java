package com.example.music.service.ServiceImpl;

import com.example.music.entity.pojo.Entity.User;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.mapper.BasicMapper;
import com.example.music.service.BasicService;
import com.example.music.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class BasicServiceImpl implements BasicService {

    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private BasicMapper basicMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultObjectModel register(String email, String codeRe,
                                      String password, String account) {
        String code = (String) redisUtils.get(email);
        if (code == null) {
            return ResultObjectModel.fail("验证码已过期");
        }
        if (basicMapper.selectUserByEmail(email) != null) {
            return ResultObjectModel.fail("邮箱已被注册");
        }
        if (basicMapper.selectUserByAccount(account) != null) {
            return ResultObjectModel.fail("账号已被注册");
        }
        if (!code.equals(codeRe)) {
            return ResultObjectModel.fail("验证码不正确");
        }
        User user = new User();
        user.setAccount(account);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (basicMapper.register(user) == 1 &&
                basicMapper.setUserRole(user.getUserId(),2) == 1) {
            return ResultObjectModel.success("注册成功");
        }
        return ResultObjectModel.fail("注册失败");
    }
}
