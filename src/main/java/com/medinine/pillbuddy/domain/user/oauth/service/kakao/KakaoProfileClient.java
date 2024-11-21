package com.medinine.pillbuddy.domain.user.oauth.service.kakao;

import com.medinine.pillbuddy.domain.user.oauth.dto.KakaoUserResponse;
import com.medinine.pillbuddy.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "kakao-profile-client",
        url = "https://kapi.kakao.com",
        configuration = FeignConfig.class)
public interface KakaoProfileClient {

    @GetMapping("/v2/user/me")
    KakaoUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}
