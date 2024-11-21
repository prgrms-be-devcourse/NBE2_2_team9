package com.medinine.pillbuddy.domain.user.oauth.controller;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.service.SocialLoginService;
import com.medinine.pillbuddy.global.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users/oauth")
public class OAuthController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/connection/kakao")
    public ResponseEntity<String> getConnectionByKakao(@RequestParam UserType userType) {
        String location = socialLoginService.getConnectionUrl(userType, "kakao");

        return ResponseEntity.ok(location);
    }

    @GetMapping("/connection/naver")
    public ResponseEntity<String> getConnectionByNaver(@RequestParam UserType userType) {
        String location = socialLoginService.getConnectionUrl(userType, "naver");

        return ResponseEntity.ok(location);
    }

    @GetMapping("/login/{registrationId}/{userType}")
    public ResponseEntity<JwtToken> login(
            @RequestParam String code,
            @PathVariable String userType,
            @PathVariable String registrationId) {
        JwtToken jwtToken = socialLoginService.login(code, UserType.from(userType), registrationId);

        return ResponseEntity.ok(jwtToken);
    }
}
