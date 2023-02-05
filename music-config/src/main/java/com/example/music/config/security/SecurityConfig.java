package com.example.music.config.security;


import com.example.music.config.security.Interceptor.AccessSecurityInterceptor;
import com.example.music.config.security.Interceptor.CustomizedAccessDecisionManager;
import com.example.music.config.security.Interceptor.InvocationSecurityMetadataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ThirdAuthenticationSecurityConfig thirdAuthenticationSecurityConfig;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomizedAccessDecisionManager customizedAccessDecisionManager;
    @Autowired
    private InvocationSecurityMetadataSourceService invocationSecurityMetadataSourceService;
    @Autowired
    private AccessSecurityInterceptor accessSecurityInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/swagger-ui.html",
//                        "/swagger-resources/**",
//                        "/webjars/**",
//                        "/v2/**",
//                        "/api/**",
//                        "/doc.html/**",
//                        "/Basic/**",
//                        "/favicon.ico")
//                .permitAll();
        //配置动态权限认证
        http.authorizeRequests()
                        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                            @Override
                            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                                object.setSecurityMetadataSource(invocationSecurityMetadataSourceService);
                                object.setAccessDecisionManager(customizedAccessDecisionManager);
                                return object;
                            }
                        });
        http.exceptionHandling().authenticationEntryPoint(new UnauthEntryPoint())
                .accessDeniedHandler(new CustomizedAccessDeniedHandler());
        http.apply(thirdAuthenticationSecurityConfig);
        http.csrf().disable().authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(accessSecurityInterceptor,FilterSecurityInterceptor.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
