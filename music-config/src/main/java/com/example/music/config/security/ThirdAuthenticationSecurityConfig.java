package com.example.music.config.security;

import com.example.music.config.security.Filter.ThirdAuthenticationFilter;
import com.example.music.config.security.Filter.TokenAuthenticationFilter;
import com.example.music.config.security.Provider.EmailAuthenticationProvider;
import com.example.music.config.security.Provider.GithubAuthenticationProvider;
import com.example.music.mapper.BasicMapper;
import com.example.music.mapper.PermissionMapper;
import com.example.music.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ThirdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private EmailAuthenticationProvider emailAuthenticationProvider;
    @Autowired
    private GithubAuthenticationProvider githubAuthenticationProvider;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private BasicMapper basicMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //添加第三方过滤器至过滤器链
        ThirdAuthenticationFilter thirdAuthenticationFilter = new ThirdAuthenticationFilter(redisUtils,tokenManager,basicMapper);
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenManager,permissionMapper);
        http.addFilterBefore(thirdAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        thirdAuthenticationFilter.setAuthenticationManager(authenticationManager);
        //添加第三方认证提供者
        http.authenticationProvider(emailAuthenticationProvider);
        http.authenticationProvider(githubAuthenticationProvider);
        super.configure(http);
    }
}
