package com.medinine.pillbuddy.domain.user.oauth.service.naver;

import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_AUTHORIZATION_URI;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CAREGIVER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CARETAKER_REDIRECT_URI;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_CLIENT_ID;
import static com.medinine.pillbuddy.global.oauth.NaverProperty.NAVER_OAUTH_QUERY_STRING;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.service.OAuthClient;
import org.springframework.stereotype.Component;

@Component
public class NaverOAuthClient implements OAuthClient {
    public String getConnectionUrl(UserType userType) {
        return switch (userType) {
            case CAREGIVER -> NAVER_AUTHORIZATION_URI + String.format(NAVER_OAUTH_QUERY_STRING, NAVER_CLIENT_ID, NAVER_CAREGIVER_REDIRECT_URI);
            case CARETAKER -> NAVER_AUTHORIZATION_URI + String.format(NAVER_OAUTH_QUERY_STRING, NAVER_CLIENT_ID, NAVER_CARETAKER_REDIRECT_URI);
        };
    }
}
