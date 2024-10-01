package com.mednine.pillbuddy.global.jwt;

import com.mednine.pillbuddy.domain.user.service.CustomUserDetails;
import com.mednine.pillbuddy.domain.user.service.MyUserDetailsService;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String GRANT_TYPE = "Bearer";

    private final Key key;

    private final MyUserDetailsService myUserDetailsService;

    @Value("${jwt.token.access-expiration-time}")
    private Long accessExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private Long refreshExpirationTime;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.token.client-secret}") String secretKey,
                            MyUserDetailsService myUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * Access 토큰 생성
     */
    public String getAccessToken(Authentication authentication) {
        Date now = new Date();
        Date accessTokenExpireDate = new Date(now.getTime() + accessExpirationTime);

        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String getRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date refreshTokenExpireDate = new Date(now.getTime() + refreshExpirationTime);

        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰의 loginId 를 통해 권한을 가져오는 메서드
     */
    public Authentication getAuthenticationByToken(String token) {
        String loginId = getLoginId(token);
        CustomUserDetails userDetails = myUserDetailsService.loadUserByUsername(loginId);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getLoginId(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new PillBuddyCustomException(ErrorCode.JWT_TOKEN_EXPIRED);
        }
    }

    /**
     * 토큰 검증 메서드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new PillBuddyCustomException(ErrorCode.JWT_TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            throw new PillBuddyCustomException(ErrorCode.JWT_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new PillBuddyCustomException(ErrorCode.JWT_TOKEN_UNSUPPORTED);
        }
    }

    /**
     * HttpRequest 로부터 토큰 정보를 가져오는 메서드
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)
                && bearerToken.length() > GRANT_TYPE.length() + 1) {
            return bearerToken.substring(GRANT_TYPE.length() + 1);
        }
        return null;
    }
}
