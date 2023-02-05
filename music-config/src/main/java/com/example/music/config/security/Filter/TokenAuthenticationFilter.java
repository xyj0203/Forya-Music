package com.example.music.config.security.Filter;


import com.example.music.config.security.TokenManager;
import com.example.music.entity.pojo.Entity.SecurityUser;
import com.example.music.mapper.PermissionMapper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Xuyujie
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenManager tokenManager;
    private final PermissionMapper permissionMapper;
    public TokenAuthenticationFilter(TokenManager tokenManager, PermissionMapper permissionMapper) {
        this.tokenManager = tokenManager;
        this.permissionMapper = permissionMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AbstractAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private AbstractAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader("token");
        if (token != null && tokenManager.verfyToken(token)) {
            Map<String, String> map = tokenManager.getUserInfoFromToken(token);
            if (map == null) {
                return null;
            }
            Long id = Long.parseLong(map.get("userId"));
            SecurityUser securityUser = new SecurityUser(id,new ArrayList<>());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            permissionMapper.selectUserRole(id).forEach(name -> authorities.add(new SimpleGrantedAuthority(name)));
            return new UsernamePasswordAuthenticationToken(securityUser, token, authorities);
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("UNKNOWN"));
        return new AnonymousAuthenticationToken("UNKNOWN","UNKNOWN",authorities);
    }
}
