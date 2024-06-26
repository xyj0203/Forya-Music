package com.example.music.config.security.Provider;

import com.example.music.config.security.Token.GithubAuthenticationToken;
import com.example.music.entity.pojo.Entity.SecurityUser;
import com.example.music.entity.pojo.Entity.ThirdUser;
import com.example.music.entity.enums.LoginType;
import com.example.music.entity.enums.UserType;
import com.example.music.mapper.PermissionMapper;
import com.example.music.service.ServiceImpl.GithubClientService;
import com.example.music.service.ThirdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GithubAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private ThirdService thirdService;
    @Autowired
    private GithubClientService githubClientService;
    @Resource
    private PermissionMapper permissionMapper;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        GithubAuthenticationToken token = (GithubAuthenticationToken) authentication;
        String accessToken = (String)token.getPrincipal();
        //根据accessToken 获取github用户信息
        Map<String, Object> userInfo = githubClientService.queryUser(accessToken);
        if (userInfo == null){
            log.info("未取到数据");
            return null;
        }
        //然后，根据github用户，查询对应系统用户信息
        ThirdUser thirdUser = getThirdUser(userInfo,LoginType.LOGIN_BY_GITHUB);
        thirdUser = thirdService.loadThirdUser(thirdUser);
        List<String> list = permissionMapper.selectPermissionByLevel(UserType.THIRD_USER.getLevel());
        SecurityUser securityUser = new SecurityUser(thirdUser.getThirdName(),thirdUser.getThirdAccount(),list);
        securityUser.setUserId(thirdUser.getThirdId());
        return new GithubAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GithubAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private ThirdUser getThirdUser(Map<String,Object> map,LoginType loginType){
        String thirdAccount = (String) map.get("login");
        Long id = Long.parseLong((String) map.get("id"));
        id = id * 10 + loginType.getType();
        int thirdType = LoginType.LOGIN_BY_GITHUB.getType();
        String thirdName = (String) map.get("name");
        String thirdAvtar = (String) map.get("avatar_url");
        String thirdCompany = (String) map.get("company");
        String thirdDecription = (String) map.get("bio");
        ThirdUser thirdUser = new ThirdUser(null,thirdAccount,thirdType,thirdName,thirdAvtar,thirdCompany,thirdDecription,id);
        return thirdUser;
    }
}
