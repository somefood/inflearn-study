package com.somefood.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somefood.jwt.config.auth.PrincipalDetails;
import com.somefood.jwt.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            log.info("{}", user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService 의 loadByUsername() 실행, 정상 실행되면 authentication 반환
            // DB에 있는 username과 password 일치
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            // 로그인 정상 처리 됨
            log.info("username: {} password: {}", principalDetails.getUser().getUsername(), principalDetails.getUser().getPassword());

            // authentication 객체가 session에 저장됨 -> 로그인 되었단 뜻
            // 리턴의 이유는 권한 관리를 Seucrity에서 해주기 때문. for 편리
            // JWT 사용하면 세션 필요가 없지만, 관리 차원이 필요하다!
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 정상인지 로그인 시, authenticationManager로 로그인 시도를 하면
        // PrincipalDetailsService 호출 loadByUsername() 실행

        // PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
        // JWT 토큰 생성해서 응답
        return null;
    }

    // 인증이 정상적으로 되었으면 아래 함수 실행
    // 여기서 jwt 토큰 만들어서 사용자에게 토큰값 반환
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 로그인 완료");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader("Authorization", JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
