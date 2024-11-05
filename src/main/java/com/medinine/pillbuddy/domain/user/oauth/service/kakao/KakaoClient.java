package com.medinine.pillbuddy.domain.user.oauth.service.kakao;

import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_GRANT_TYPE;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CAREGIVER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CARETAKER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.KakaoProperty.KAKAO_CLIENT_ID;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.KakaoUserResponse;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthTokenResponse;
import com.medinine.pillbuddy.domain.user.oauth.service.OAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoClient implements OAuthClient {

    private static final String KAKAO_AUTHORIZATION_URI = "https://kauth.kakao.com/oauth/authorize";
    private static final String KAKAO_AUTH_QUERY_STRING = "?response_type=code&client_id=%s&redirect_uri=%s";

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoProfileClient kakaoProfileClient;

    public String getConnectionUrl(UserType userType) {
        return switch (userType) {
            case CAREGIVER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_AUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CAREGIVER_REDIRECT_URI);
            case CARETAKER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_AUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CARETAKER_REDIRECT_URI);
        };
    }

    @Override
    public OAuthProfile getUserInfo(String code) {
        OAuthTokenResponse tokenResponse = kakaoAuthClient.getAccessToken(KAKAO_AUTHORIZATION_GRANT_TYPE, KAKAO_CLIENT_ID, code);
        KakaoUserResponse userInfo = kakaoProfileClient.getUserInfo("Bearer " + tokenResponse.getAccessToken());

        return getOAuthProfile(userInfo);
    }

    private OAuthProfile getOAuthProfile(KakaoUserResponse userInfo) {
        return OAuthProfile.builder()
                .id(String.valueOf(userInfo.getId()))
                .nickname(userInfo.getProperties().get("nickname"))
                .email(userInfo.getEmail())
                .build();
    }
}
