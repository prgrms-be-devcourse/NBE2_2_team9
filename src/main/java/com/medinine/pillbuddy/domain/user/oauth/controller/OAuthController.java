package com.medinine.pillbuddy.domain.user.oauth.controller;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.service.KakaoAuthService;
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

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/connection")
    public ResponseEntity<String> loginPage(@RequestParam UserType userType) {
        String location = kakaoAuthService.getConnectionUrl(userType);

        return ResponseEntity.ok(location);
    }

    @GetMapping("/login/{userType}")
    public ResponseEntity<JwtToken> login(@RequestParam String code, @PathVariable String userType) {
        JwtToken jwtToken = kakaoAuthService.login(code, UserType.from(userType));

        return ResponseEntity.ok(jwtToken);
    }
}
