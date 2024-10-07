package com.mednine.pillbuddy.global.jwt;

import com.mednine.pillbuddy.domain.user.service.CustomUserDetails;
import com.mednine.pillbuddy.domain.user.service.MyUserDetailsService;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String GRANT_TYPE = "Bearer";
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

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

    public JwtToken generateToken(Authentication authentication) {
        Date now = new Date();
        String accessToken = getAccessToken(authentication, now);
        String refreshToken = getRefreshToken(authentication, now);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(GRANT_TYPE)
                .build();
    }

    public JwtToken reissueAccessToken(String refreshToken) {
        String loginId = getLoginId(refreshToken);
        CustomUserDetails userDetails = myUserDetailsService.loadUserByUsername(loginId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        return generateToken(authentication);
    }

    /**
     * Access 토큰 생성
     */
    public String getAccessToken(Authentication authentication, Date now) {
        Date accessTokenExpireDate = new Date(now.getTime() + accessExpirationTime);

        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        return Jwts.builder()
                .setHeader(setHeader(ACCESS_TOKEN_TYPE))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String getRefreshToken(Authentication authentication, Date now) {
        Date refreshTokenExpireDate = new Date(now.getTime() + refreshExpirationTime);

        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        return Jwts.builder()
                .setHeader(setHeader(REFRESH_TOKEN_TYPE))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> setHeader(String type) {
        Map<String, Object> header = new HashMap<>();
        header.put("type", "JWT");
        header.put("tokenType", type);
        header.put("alg", "HS256");
        return header;
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

    public boolean isAccessToken(String token) {
        JwsHeader header = getHeader(token);
        return ACCESS_TOKEN_TYPE.equals(header.get("tokenType"));
    }

    public boolean isRefreshToken(String token) {
        JwsHeader header = getHeader(token);
        return REFRESH_TOKEN_TYPE.equals(header.get("tokenType"));
    }

    private JwsHeader getHeader(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getHeader();
    }

    /**
     * 토큰 정보를 가져오는 메서드
     */
    public String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)
                && bearerToken.length() > GRANT_TYPE.length() + 1) {
            return bearerToken.substring(GRANT_TYPE.length() + 1);
        }
        return null;
    }
}
