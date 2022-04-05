package com.somefood.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 이게 동작함
// formLogin().disabled()해서 작동 안 함
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter 인증 시도");

        // username, password 받아서
        // 정상인지 로그인 시, authenticationManager로 로그인 시도를 하면
        // PrincipalDetailsService 호출 loadByUsername() 실행

        // PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
        // JWT 토큰 생성해서 응답
        return super.attemptAuthentication(request, response);
    }
}
