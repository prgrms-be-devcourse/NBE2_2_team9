package com.medinine.pillbuddy.domain.user.oauth.service.naver;

import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_AUTHORIZATION_GRANT_TYPE;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CAREGIVER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CARETAKER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CLIENT_ID;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CLIENT_SECRET;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.NaverUserResponse;
import com.medinine.pillbuddy.domain.user.oauth.dto.NaverUserResponse.NaverUserDetail;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthTokenResponse;
import com.medinine.pillbuddy.domain.user.oauth.service.OAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverClient implements OAuthClient {

    private static final String NAVER_OAUTH_QUERY_STRING = "/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String NAVER_AUTHORIZATION_URI = "https://nid.naver.com";

    private final NaverAuthClient naverAuthClient;
    private final NaverProfileClient naverProfileClient;

    @Override
    public String getConnectionUrl(UserType userType) {
        return switch (userType) {
            case CAREGIVER -> NAVER_AUTHORIZATION_URI + String.format(NAVER_OAUTH_QUERY_STRING, NAVER_CLIENT_ID,
                    NAVER_CAREGIVER_REDIRECT_URI);
            case CARETAKER -> NAVER_AUTHORIZATION_URI + String.format(NAVER_OAUTH_QUERY_STRING, NAVER_CLIENT_ID,
                    NAVER_CARETAKER_REDIRECT_URI);
        };
    }

    @Override
    public OAuthProfile getUserInfo(String code) {
        OAuthTokenResponse tokenResponse = naverAuthClient.getAccessToken(NAVER_AUTHORIZATION_GRANT_TYPE,
                NAVER_CLIENT_ID, NAVER_CLIENT_SECRET, code);
        NaverUserResponse userInfo = naverProfileClient.getUserInfo("Bearer " + tokenResponse.getAccessToken());

        return getOAuthProfile(userInfo);
    }

    private OAuthProfile getOAuthProfile(NaverUserResponse userInfo) {
        NaverUserDetail naverUserDetail = userInfo.getNaverUserDetail();

        return OAuthProfile.builder()
                .id(naverUserDetail.getId().substring(0, Math.min(naverUserDetail.getId().length(), 10)))
                .nickname(naverUserDetail.getNickname())
                .email(naverUserDetail.getEmail())
                .phoneNumber(naverUserDetail.getPhoneNumber())
                .build();
    }
}
