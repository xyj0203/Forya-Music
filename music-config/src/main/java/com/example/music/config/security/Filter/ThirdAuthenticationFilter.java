package com.example.music.config.security.Filter;

import cn.hutool.core.lang.Validator;
import com.example.music.config.security.Token.EmailAuthenticationToken;
import com.example.music.config.security.Token.GithubAuthenticationToken;
import com.example.music.config.security.TokenManager;
import com.example.music.entity.pojo.Entity.SecurityUser;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.mapper.BasicMapper;
import com.example.music.utils.RedisUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ThirdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final RedisUtils redisUtils;
    private final TokenManager tokenManager;
    private final BasicMapper basicMapper;

    public ThirdAuthenticationFilter(RedisUtils redisUtils, TokenManager tokenManager,BasicMapper basicMapper) {
        super(new AntPathRequestMatcher("/Basic/login", "POST"));
        this.redisUtils = redisUtils;
        this.tokenManager = tokenManager;
        this.basicMapper = basicMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (!"POST".equals(request.getMethod())) {
            ResultObjectModel.returnvalue(request, response,ResultObjectModel.fail("Only POST requests are supported"));
            return null;
        }
        //获取第三方登录的类型
        String loginType = request.getParameter("loginType");
        if (loginType == null){
            ResultObjectModel.returnvalue(request, response,ResultObjectModel.fail("登录类型不能为空"));
            return null;
        }
        int type;
        try {
           type = Integer.parseInt(loginType);
        }catch (NumberFormatException e) {
            ResultObjectModel.returnvalue(request, response,ResultObjectModel.fail("登录类型不正确"));
            return null;
        }
        String principal = request.getParameter("principal");
        String credentials = request.getParameter("credentials");
        if (Validator.isEmpty(principal) || Validator.isEmpty(credentials)) {
            ResultObjectModel.returnvalue(request, response,ResultObjectModel.fail("参数信息不能为空"));
            return null;
        }
        AbstractAuthenticationToken authRequest;
        switch (type) {
            case 1:
                authRequest = new EmailAuthenticationToken(principal, credentials);
                String judge = judge(principal, credentials, type);
                if(judge != null){
                    ResultObjectModel.returnvalue(request, response,ResultObjectModel.fail(judge));
                    return null;
                }
                break;
            case 2:
                authRequest = new GithubAuthenticationToken(principal, credentials);
                break;
            default:
                authRequest = new UsernamePasswordAuthenticationToken(principal, credentials);
                break;
        }
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String judge(String principal, String credentials,Integer type){
        if (type == 1){
            if (Validator.isEmail(principal)){
                if(credentials.length() == 6){
                    String code = (String)redisUtils.get(principal);
                    if (code != null && code.equals(credentials) ){
                        if (basicMapper.selectUserByEmail(principal) == null){
                            return "用户不存在，请先注册";
                        }else{
                            return null;
                        }
                    }else{
                        return "验证码错误";
                    }
                }else{
                    return "验证码长度不正确";
                }
            }else{
                return "邮箱格式不正确";
            }
        }
        return null;
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        Object principal = authResult.getPrincipal();
        SecurityUser securityUser = (SecurityUser) principal;
        Map<String,String> map = new HashMap<>();
        map.put("id",securityUser.getUserId().toString());
        String token = tokenManager.createToken(map);
        ResultObjectModel.returnvalue(request, response,ResultObjectModel.success("登录成功！",token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ResultObjectModel.returnvalue(request, response,ResultObjectModel.fail("登录失败！"));
    }
}
