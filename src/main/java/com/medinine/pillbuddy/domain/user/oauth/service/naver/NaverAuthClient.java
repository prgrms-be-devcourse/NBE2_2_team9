package com.medinine.pillbuddy.domain.user.oauth.service.naver;

import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthTokenResponse;
import com.medinine.pillbuddy.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "naver-auth-client",
        url = "https://nid.naver.com",
        configuration = FeignConfig.class
)
public interface NaverAuthClient {

    @PostMapping("/oauth2.0/token")
    OAuthTokenResponse getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code
    );
}
