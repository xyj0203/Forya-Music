package com.example.music.service.ServiceImpl;

import com.example.music.service.CacheService;
import com.example.music.service.PermissionService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 切面编程，更新缓存
 */
@Service
@Aspect
public class CacheServiceImpl implements CacheService {

    @Autowired
    private PermissionService permissionService;

    @After(value = "execution(public * com.example."
            + "music.service.PermissionService.*Role(..))")
    public void reloadRole() {
        permissionService.loadRoleCache();
    }

    @After(value = "execution(public * com.example."
            + "music.service.PermissionService.*Permission(..))")
    public void reloadPermission() {
        permissionService.loadPermissionCache();
    }
}
