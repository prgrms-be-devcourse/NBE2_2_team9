package com.medinine.pillbuddy.domain.user.oauth.service.naver;

import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_AUTHORIZATION_GRANT_TYPE;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CLIENT_ID;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CLIENT_SECRET;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_TOKEN_URI;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_USER_INFO_URI;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.NaverUserResponse;
import com.medinine.pillbuddy.domain.user.oauth.dto.NaverUserResponse.NaverUserDetail;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthTokenResponse;
import com.medinine.pillbuddy.domain.user.oauth.service.OAuthService;
import io.netty.handler.codec.http.HttpHeaderValues;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NaverOAuthService implements OAuthService {

    @Override
    public OAuthProfile getUserInfo(String code, UserType userType) {
        String accessToken = getAccessToken(code);

        return getUserInfo(accessToken);
    }

    @Override
    public String getAccessToken(String code) {
        Optional<OAuthTokenResponse> oAuthTokenResponse = WebClient.create(NAVER_TOKEN_URI).post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", NAVER_AUTHORIZATION_GRANT_TYPE)
                        .queryParam("client_id", NAVER_CLIENT_ID)
                        .queryParam("client_secret", NAVER_CLIENT_SECRET)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("- getAccessToken() => 4xx Client Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Invalid Parameter"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("- getAccessToken() => 5xx Server Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Internal Server Error"));
                })
                .bodyToMono(OAuthTokenResponse.class)
                .blockOptional();

        log.info("naver token response => {}", oAuthTokenResponse);

        return oAuthTokenResponse.map(OAuthTokenResponse::getAccessToken).orElseThrow(
                () -> new IllegalArgumentException("네이버 엑세스 토큰을 찾을 수 없습니다.")
        );
    }

    @Override
    public OAuthProfile getUserInfo(String accessToken) {
        Optional<NaverUserResponse> profile = WebClient.create(NAVER_USER_INFO_URI)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("- getUserInfo() => 4xx Client Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Invalid Parameter"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("- getUserInfo() => 5xx Server Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Internal Server Error"));
                })
                .bodyToMono(NaverUserResponse.class)
                .blockOptional();

        log.info("kakao user info => {}", profile);
        NaverUserResponse response = profile.orElseThrow(() -> new IllegalArgumentException("카카오 사용자 정보를 찾을 수 없습니다."));
        NaverUserDetail naverUserDetail = response.getNaverUserDetail();

        return OAuthProfile.builder()
                .id(naverUserDetail.getId().substring(0, Math.min(naverUserDetail.getId().length(), 10)))
                .nickname(naverUserDetail.getNickname())
                .email(naverUserDetail.getEmail())
                .phoneNumber(naverUserDetail.getPhoneNumber())
                .build();
    }
}
