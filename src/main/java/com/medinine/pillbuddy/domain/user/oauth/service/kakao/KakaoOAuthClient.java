package com.medinine.pillbuddy.domain.user.oauth.service.kakao;

import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CAREGIVER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CARETAKER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CLIENT_ID;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_OAUTH_QUERY_STRING;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.service.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KakaoOAuthClient implements OAuthClient {

    public String getConnectionUrl(UserType userType) {
        return switch (userType) {
            case CAREGIVER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_OAUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CAREGIVER_REDIRECT_URI);
            case CARETAKER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_OAUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CARETAKER_REDIRECT_URI);
        };
    }
}
