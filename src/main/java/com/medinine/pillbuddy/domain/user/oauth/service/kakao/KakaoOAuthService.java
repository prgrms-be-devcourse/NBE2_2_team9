package com.medinine.pillbuddy.domain.user.oauth.service.kakao;

import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_GRANT_TYPE;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CLIENT_ID;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_TOKEN_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_USER_INFO_URI;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.KakaoUserResponse;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthTokenResponse;
import com.medinine.pillbuddy.domain.user.oauth.service.OAuthService;
import io.netty.handler.codec.http.HttpHeaderValues;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    @Override
    public OAuthProfile getUserInfo(String code, UserType userType) {
        // code 를 통해 카카오 서버로 Token 요청 (POST 요청)
        String accessToken = getAccessToken(code);

        // Token 을 통해 카카오 서버로 사용자 데이터 요청 (POST 요청)
        return getUserInfo(accessToken);
    }

    @Override
    public String getAccessToken(String code) {
        Optional<OAuthTokenResponse> kakaoTokenResponse = WebClient.create(KAKAO_TOKEN_URI).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .queryParam("grant_type", KAKAO_AUTHORIZATION_GRANT_TYPE)
                        .queryParam("client_id", KAKAO_CLIENT_ID)
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

        log.info("kakao token response => {}", kakaoTokenResponse);

        return kakaoTokenResponse.map(OAuthTokenResponse::getAccessToken).orElseThrow(
                () -> new IllegalArgumentException("카카오 엑세스 토큰을 찾을 수 없습니다.")
        );
    }

    @Override
    public OAuthProfile getUserInfo(String accessToken) {
        Optional<KakaoUserResponse> profile = WebClient.create(KAKAO_USER_INFO_URI)
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
                .bodyToMono(KakaoUserResponse.class)
                .blockOptional();

        log.info("kakao user info => {}", profile);
        KakaoUserResponse response = profile.orElseThrow(
                () -> new IllegalArgumentException("카카오 사용자 정보를 찾을 수 없습니다."));

        return OAuthProfile.builder()
                .id(String.valueOf(response.getId()))
                .nickname(response.getProperties().get("nickname"))
                .email(response.getEmail())
                .build();
    }
}
