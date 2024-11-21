package com.medinine.pillbuddy.global.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NaverProperty {

    public static String NAVER_CLIENT_ID;
    public static String NAVER_CLIENT_SECRET;
    public static String NAVER_CAREGIVER_REDIRECT_URI;
    public static String NAVER_CARETAKER_REDIRECT_URI;
    public static String NAVER_AUTHORIZATION_GRANT_TYPE;

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

    @Value("${oauth.naver.authorization-grant-type}")
    public void setNaverAuthorizationGrantType(String naverAuthorizationGrantType) {
        NAVER_AUTHORIZATION_GRANT_TYPE = naverAuthorizationGrantType;
    }
}
