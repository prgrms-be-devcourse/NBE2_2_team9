package com.mednine.pillbuddy.global.jwt;

import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = jwtTokenProvider.resolveToken(bearerToken);

            // token 유효성 검증
            if (token != null && jwtTokenProvider.validateToken(token) && jwtTokenProvider.isAccessToken(token)) {
                // token 의 loginId 를 통해 권한 정보를 조회하여 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthenticationByToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (PillBuddyCustomException e) {
            log.error("PillBuddyCustomException occurred: {}", e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
