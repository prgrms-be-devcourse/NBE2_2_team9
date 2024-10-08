package com.medinine.pillbuddy.global.config;

import com.medinine.pillbuddy.global.jwt.JwtAccessDeniedHandler;
import com.medinine.pillbuddy.global.jwt.JwtAuthenticationEntryPoint;
import com.medinine.pillbuddy.global.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint entryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // rest api 사용으로 basic auth 및 csrf 보안 X
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // 예외를 사용자에게 전달하기 위한 entryPoint, exceptionHandler 설정
                .exceptionHandling(ex -> {
                    ex.accessDeniedHandler(jwtAccessDeniedHandler);
                    ex.authenticationEntryPoint(entryPoint);
                })
                // jwt 토큰을 사용하기 때문에 세션 사용 X
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // api 테스트 시 편의를 위해 잠시 모든 요청 허용
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/users/join",
                                            "/api/users/login",
                                            "/api/users/reissue-token",
                                            "api/search").permitAll()
                            .anyRequest().hasRole("USER")
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
