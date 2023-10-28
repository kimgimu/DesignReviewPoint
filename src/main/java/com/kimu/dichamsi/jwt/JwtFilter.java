package com.kimu.dichamsi.jwt;

import com.kimu.dichamsi.service.MemberService;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;

    @Value("${jwt.token.secret}")
    private String key;

    public JwtFilter(MemberService memberService,String key) {
        this.memberService = memberService;
        this.key = key;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);

        //token 유무 확인
        if(authentication == null || !authentication.startsWith("Bearer")){
            System.out.println("토근확인"+authentication);
            filterChain.doFilter(request,response);
            return;
        }

        //token 꺼내기
        String token = authentication.split(" ")[1];

        //token 유효 기간 여부
        if(JwtTokenUtil.isExpired(token,key)){
            filterChain.doFilter(request,response);
        }

        //memberEmail token에서 꺼내기
        String userEmail = JwtTokenUtil.getUserEmail(token,key);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userEmail,null, List.of(new SimpleGrantedAuthority("USER")));

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);

    }
}
