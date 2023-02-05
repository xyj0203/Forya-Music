package com.example.music.config.security.Interceptor;

import com.example.music.entity.pojo.Entity.Permission;
import com.example.music.entity.pojo.Entity.Role;
import com.example.music.utils.RedisKeyUtils;
import com.example.music.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import java.util.*;

@Service
public class InvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    AntPathMatcher antPathMatcher;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //强制类型转换
        FilterInvocation filterInvocation = ((FilterInvocation) object);
        //获取用户请求的地址
        String requestUrl = filterInvocation.getRequestUrl();
        List<String> roleRequires = new ArrayList<>();
        List<Role> roleList = (List<Role>)redisUtils.get(RedisKeyUtils.ROLE_LIST);

        for (Role role:roleList) {
            List<Permission> rolePermission = (List<Permission>)redisUtils.get(RedisKeyUtils.ROLE_PERMISSION+role.getName());
            for (Permission permission: rolePermission) {
                if (antPathMatcher.match(permission.getPath(),requestUrl)) {
                    roleRequires.add(role.getName());
                    break;
                }
            }
        }
        if (roleRequires.size() > 0) {
            String []roles = new String[roleRequires.size()];
            roleRequires.toArray(roles);
            return SecurityConfig.createList(roles);
        }
        return SecurityConfig.createList("UNKNOWN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
