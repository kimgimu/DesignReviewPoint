package com.kimu.dichamsi.config;

import com.kimu.dichamsi.jwt.JwtFilter;
import com.kimu.dichamsi.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final MemberService memberService;

    @Value("${jwt.token.secret}")
    private String key;

    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                    //허용이 되는 엔드 포인트
                    .antMatchers("/user/join","/user/login").permitAll()
                    //나머지는 다 접근불가
                    .anyRequest().authenticated()
                .and()
                //허용이 안되는 url로 이동할시에 로그인페이지로 이동
                .formLogin()
                    .loginPage("/user/login")
                    //성공을 한다면 메인페이지로
                    .defaultSuccessUrl("/")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtFilter(memberService, key), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
