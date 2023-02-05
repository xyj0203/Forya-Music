package com.example.music.service.ServiceImpl;

import com.example.music.entity.pojo.Entity.SecurityUser;
import com.example.music.entity.pojo.Entity.User;
import com.example.music.mapper.BasicMapper;
import com.example.music.mapper.PermissionMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private BasicMapper basicMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = basicMapper.selectUserByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<String> roleList = permissionMapper.selectUserRole(user.getUserId());
        SecurityUser securityUser = new SecurityUser(user.getAccount(),user.getPassword(),roleList);
        securityUser.setUserId(user.getUserId());
        return securityUser;
    }

    public  User loadUserByEmail(String email){
        User user = basicMapper.selectUserByEmail(email);
        return user;
    }

}
