package com.somefood.jwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        log.info("필터1");
        if (req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");

            if (headerAuth.equals("hi")) {
                log.info("headerAuth={}", headerAuth);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
