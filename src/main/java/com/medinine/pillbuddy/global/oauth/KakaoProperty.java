package com.medinine.pillbuddy.global.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoProperty {

    public static String KAKAO_CLIENT_ID;
    public static String KAKAO_CAREGIVER_REDIRECT_URI;
    public static String KAKAO_CARETAKER_REDIRECT_URI;
    public static String KAKAO_AUTHORIZATION_GRANT_TYPE;

    @Value("${oauth.kakao.client-id}")
    public void setClientId(String clientId) {
        KAKAO_CLIENT_ID = clientId;
    }

    @Value("${oauth.kakao.caregiver-redirect-uri}")
    public void setCaregiverRedirectUri(String redirectUri) {
        KAKAO_CAREGIVER_REDIRECT_URI = redirectUri;
    }

    @Value("${oauth.kakao.caretaker-redirect-uri}")
    public void setCaretakerRedirectUri(String redirectUri) {
        KAKAO_CARETAKER_REDIRECT_URI = redirectUri;
    }

    @Value("${oauth.kakao.authorization-grant-type}")
    public void setAuthorizationGrantType(String authorizationGrantType) {
        KAKAO_AUTHORIZATION_GRANT_TYPE = authorizationGrantType;
    }
}
