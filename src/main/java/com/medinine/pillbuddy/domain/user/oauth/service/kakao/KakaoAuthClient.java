package com.medinine.pillbuddy.domain.user.oauth.service.kakao;

import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthTokenResponse;
import com.medinine.pillbuddy.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "kakao-auth-client",
        url = "https://kauth.kakao.com",
        configuration = FeignConfig.class
)
public interface KakaoAuthClient {

    @PostMapping("/oauth/token")
    OAuthTokenResponse getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("code") String code
    );
}
