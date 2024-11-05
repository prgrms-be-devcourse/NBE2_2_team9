package com.medinine.pillbuddy.domain.user.oauth.service.naver;

import com.medinine.pillbuddy.domain.user.oauth.dto.NaverUserResponse;
import com.medinine.pillbuddy.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "naver-profile-client",
        url = "https://openapi.naver.com",
        configuration = FeignConfig.class)
public interface NaverProfileClient {

    @GetMapping("/v1/nid/me")
    NaverUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}
