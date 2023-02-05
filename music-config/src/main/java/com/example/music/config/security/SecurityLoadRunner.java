package com.example.music.config.security;

import com.example.music.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityLoadRunner implements ApplicationRunner {

    @Autowired
    PermissionService permissionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=================================>>>开始载入所有角色");
        permissionService.loadRoleCache();
        log.info("=================================>>>角色载入缓存完成");
        log.info("=================================>>>开始载入角色权限对应信息至缓存");
        permissionService.loadPermissionCache();
        log.info("=================================>>>角色权限载入至缓存完毕");
    }
}
