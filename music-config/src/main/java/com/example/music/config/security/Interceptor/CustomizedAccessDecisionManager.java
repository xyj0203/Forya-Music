package com.example.music.config.security.Interceptor;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class CustomizedAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException , InsufficientAuthenticationException {
        //当前用户具备的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //当前访问需要的角色
        for (ConfigAttribute configAttribute : configAttributes) {
            //未登录用户
            if ("UNKNOWN".equals(configAttribute.getAttribute())) {
                return;
            }
            //具备相应的权限
            for (GrantedAuthority authority : authorities) {
                if (configAttribute.getAttribute().equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
