package com.medinine.pillbuddy.domain.user.oauth.service;

import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_GRANT_TYPE;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CAREGIVER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CARETAKER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CLIENT_ID;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_OAUTH_QUERY_STRING;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_TOKEN_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_USER_INFO_URI;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.KakaoProfile;
import com.medinine.pillbuddy.domain.user.oauth.dto.KakaoTokenResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KakaoClient {

    public String getConnectionUrl(UserType userType) {
        return switch (userType) {
            case CAREGIVER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_OAUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CAREGIVER_REDIRECT_URI);
            case CARETAKER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_OAUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CARETAKER_REDIRECT_URI);
        };
    }

    public String getAccessToken(String code) {
        Optional<KakaoTokenResponse> kakaoTokenResponse = WebClient.create(KAKAO_TOKEN_URI).post()
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
                .bodyToMono(KakaoTokenResponse.class)
                .blockOptional();

        log.info("kakao token response => {}", kakaoTokenResponse);

        return kakaoTokenResponse.map(KakaoTokenResponse::getAccessToken).orElseThrow(
                () -> new IllegalArgumentException("카카오 엑세스 토큰을 찾을 수 없습니다.")
        );
    }

    public KakaoProfile getUserInfo(String accessToken) {
        Optional<KakaoProfile> profile = WebClient.create(KAKAO_USER_INFO_URI)
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
                .bodyToMono(KakaoProfile.class)
                .blockOptional();

        log.info("kakao user info => {}", profile);

        return profile.orElseThrow(() -> new IllegalArgumentException("카카오 사용자 정보를 찾을 수 없습니다."));
    }
}
