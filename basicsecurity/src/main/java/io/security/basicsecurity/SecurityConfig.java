package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Configuration
@EnableWebSecurity // 웹 보안 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // 인가 설정
                .anyRequest().authenticated();
        http
                .formLogin()
//                .loginPage("/loginPage") // 제공해주고 싶은 화면 설정, 컨트롤러 설정
//                .defaultSuccessUrl("/") // 성공 시 이동 페이지
//                .failureUrl("/login") // 실패 시
//                .usernameParameter("userId") // form name
//                .passwordParameter("passwd") // form name
//                .loginProcessingUrl("login_proc") // form action url
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        System.out.println("authentication : " + authentication.getName());
//                        response.sendRedirect("/");
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                        System.out.println("exception : " + exception.getMessage());
//                        response.sendRedirect("/login");
//                    }
//                })
//                .permitAll()
        ;

        // 스프링 시큐리티는 로그아웃 방식을 POST를 받음. GET으로도 설정 가능
        http
                .rememberMe()
                .userDetailsService(userDetailsService)
        ;
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
//                .sessionFixation().changeSessionId() // 기본값, 세션값 바뀌게
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false)
                ;
    }
}

