package com.medinine.pillbuddy.global.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NaverProperty {

    public static String NAVER_CLIENT_ID;
    public static String NAVER_CLIENT_SECRET;
    public static String NAVER_CAREGIVER_REDIRECT_URI;
    public static String NAVER_CARETAKER_REDIRECT_URI;
    public static String NAVER_AUTHORIZATION_URI;
    public static String NAVER_OAUTH_QUERY_STRING = "?response_type=code&client_id=%s&redirect_uri=%s";
    public static String NAVER_TOKEN_URI;
    public static String NAVER_AUTHORIZATION_GRANT_TYPE;
    public static String NAVER_USER_INFO_URI;

    @Value("${oauth.naver.client-id}")
    public void setNaverClientId(String naverClientId) {
        NAVER_CLIENT_ID = naverClientId;
    }

    @Value("${oauth.naver.client-secret}")
    public void setNaverClientSecret(String naverClientSecret) {
        NAVER_CLIENT_SECRET = naverClientSecret;
    }

    @Value("${oauth.naver.caregiver-redirect-uri}")
    public void setNaverCaregiverRedirectUri(String naverCaregiverRedirectUri) {
        NAVER_CAREGIVER_REDIRECT_URI = naverCaregiverRedirectUri;
    }

    @Value("${oauth.naver.caretaker-redirect-uri}")
    public void setNaverCaretakerRedirectUri(String naverCaretakerRedirectUri) {
        NAVER_CARETAKER_REDIRECT_URI = naverCaretakerRedirectUri;
    }

    @Value("${oauth.naver.authorization-uri}")
    public void setNaverAuthorizationUri(String naverAuthorizationUri) {
        NAVER_AUTHORIZATION_URI = naverAuthorizationUri;
    }

    @Value("${oauth.naver.token-uri}")
    public void setNaverTokenUri(String naverTokenUri) {
        NAVER_TOKEN_URI = naverTokenUri;
    }

    @Value("${oauth.naver.authorization-grant-type}")
    public void setNaverAuthorizationGrantType(String naverAuthorizationGrantType) {
        NAVER_AUTHORIZATION_GRANT_TYPE = naverAuthorizationGrantType;
    }

    @Value("${oauth.naver.user-info-uri}")
    public void setNaverUserInfoUri(String naverUserInfoUri) {
        NAVER_USER_INFO_URI = naverUserInfoUri;
    }
}
