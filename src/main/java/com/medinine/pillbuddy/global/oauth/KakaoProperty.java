package com.medinine.pillbuddy.global.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoProperty {

    public static String KAKAO_CLIENT_ID;
    public static String KAKAO_REDIRECT_URI;
    public static String KAKAO_AUTHORIZATION_GRANT_TYPE;
    public static String KAKAO_AUTHORIZATION_URI;
    public static String KAKAO_TOKEN_URI;
    public static String KAKAO_USER_INFO_URI;
    public static String KAKAO_OAUTH_QUERY_STRING = "?response_type=code&client_id=%s&redirect_uri=%s";

    @Value("${oauth.kakao.client-id}")
    public void setClientId(String clientId) {
        KAKAO_CLIENT_ID = clientId;
    }

    @Value("${oauth.kakao.redirect-uri}")
    public void setRedirectUri(String redirectUri) {
        KAKAO_REDIRECT_URI = redirectUri;
    }

    @Value("${oauth.kakao.authorization-grant-type}")
    public void setAuthorizationGrantType(String authorizationGrantType) {
        KAKAO_AUTHORIZATION_GRANT_TYPE = authorizationGrantType;
    }

    @Value("${oauth.kakao.authorization-uri}")
    public void setAuthorizationUri(String authorizationUri) {
        KAKAO_AUTHORIZATION_URI = authorizationUri;
    }

    @Value("${oauth.kakao.token-uri}")
    public void setTokenUri(String tokenUri) {
        KAKAO_TOKEN_URI = tokenUri;
    }

    @Value("${oauth.kakao.user-info-uri}")
    public void setUserInfoUri(String userInfoUri) {
        KAKAO_USER_INFO_URI = userInfoUri;
    }
}
