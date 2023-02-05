package com.example.music.config.security.Provider;

import com.example.music.config.security.Token.EmailAuthenticationToken;
import com.example.music.entity.pojo.Entity.SecurityUser;
import com.example.music.entity.pojo.Entity.User;
import com.example.music.mapper.PermissionMapper;
import com.example.music.service.ServiceImpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAuthenticationToken token = (EmailAuthenticationToken) authentication;
        String email = (String) token.getPrincipal();
        User user = userDetailsService.loadUserByEmail(email);
//        List<String> list = permissionMapper.selectPermissionByLevel(user.getLevel());
        SecurityUser securityUser = new SecurityUser(user.getAccount(),user.getPassword(),new ArrayList<>());
        securityUser.setUserId(user.getUserId());
        return new EmailAuthenticationToken(securityUser, null,securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
