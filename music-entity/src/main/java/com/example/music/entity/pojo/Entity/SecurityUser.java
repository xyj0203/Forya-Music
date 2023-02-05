package com.example.music.entity.pojo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class SecurityUser implements UserDetails {

    //用户id
    private Long userId;

    @ApiModelProperty(value = "用户权限列表")
    private List<String> roleList;
    private String account;
    private String password;
    private String nickName;

    public SecurityUser( String account, String password,List<String> roleList ) {
        this.roleList = roleList;
        this.account = account;
        this.password = password;
    }

    public SecurityUser(Long userId,List<String> roleList) {
        this.userId = userId;
        this.roleList = roleList;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> athorities = new ArrayList<>();
        for (String permissionValue :
                roleList) {
            if (StringUtils.hasLength(permissionValue)) {
                athorities.add(new SimpleGrantedAuthority("ROLE_" + permissionValue));
            }
        }
        return athorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
