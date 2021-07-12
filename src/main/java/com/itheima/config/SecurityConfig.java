package com.itheima.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@EnableWebSecurity  // 启用Web安全功能
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Value("${COOKIE.VALIDITY}")
    private Integer COOKIE_VALIDITY;

    /**
     * 重写configure(HttpSecurity http)方法，进行用户授权管理
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1、自定义用户访问控制
        http.authorizeRequests()
                .antMatchers("/", "/page/**", "/article/**", "/login", "/User/**").permitAll()
                .antMatchers("/back/**", "/assets/**", "/user/**", "/article_img/**").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/**").hasAnyRole("admin","user","vip")
                // 其他的请求都需要认证
                .anyRequest().authenticated();
        // 2、登录
        http.formLogin()
                .loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
//                .successForwardUrl("/")
//                 用户登录成功处理
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse
                            httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        String url = httpServletRequest.getParameter("url");
                        // 获取被拦截的原始访问路径
                        RequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
                        if (savedRequest != null) {
                            // 如果存在原始拦截路径，登录成功后重定向到原始访问路径
                            httpServletResponse.sendRedirect(savedRequest.getRedirectUrl());
                        } else if (url != null && !url.equals("")) {
                            // 跳转到之前所在页面
                            URL fullURL = new URL(url);
                            httpServletResponse.sendRedirect(fullURL.getPath());
                        } else {
                            // 直接登录的用户，根据用户角色分别重定向到后台首页和前台首页
                            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                            boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_admin"));
                            if (isAdmin) {
                                httpServletResponse.sendRedirect("/admin");
                            } else {
                                httpServletResponse.sendRedirect("/");
                            }
                        }
                    }
                })
                // 用户登录失败处理-成功与失败都可以自定义
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        // 登录失败后,重定向回login并显示错误信息
                        httpServletResponse.sendRedirect("/login?error");
                    }
                });
        // 3、记住我-cookie
        http.rememberMe().alwaysRemember(true).tokenValiditySeconds(COOKIE_VALIDITY);
        // 4、注销
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
        // 5、针对访问无权限页面出现的403页面进行定制处理
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                // 如果是权限访问异常，则进行拦截到指定错误页面
                RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("/errorPage/comm/error_403");
                dispatcher.forward(httpServletRequest, httpServletResponse);
            }
        });
        // 6、单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
        //http.sessionManagement().maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy());
        //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
    }

    /**
     * 重写configure(AuthenticationManagerBuilder auth)方法，进行自定义用户认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //  使用JDBC进行身份认证
        String userSQL = "select username,password,valid from t_user where username = ?";
        String authoritySQL = "select u.username,a.authority " +
                              "from t_user u,t_authority a,t_user_authority ua " +
                              "where ua.user_id=u.id " +
                              "and ua.authority_id=a.id " +
                              "and u.username =?";
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery(userSQL)
                .authoritiesByUsernameQuery(authoritySQL);
    }
    /**
     * 密码加密
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

