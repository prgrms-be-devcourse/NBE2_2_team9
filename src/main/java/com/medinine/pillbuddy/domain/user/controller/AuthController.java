package com.medinine.pillbuddy.domain.user.controller;

import com.medinine.pillbuddy.domain.user.dto.JoinDto;
import com.medinine.pillbuddy.domain.user.dto.LoginDto;
import com.medinine.pillbuddy.domain.user.dto.UserDto;
import com.medinine.pillbuddy.domain.user.service.AuthService;
import com.medinine.pillbuddy.global.jwt.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "이용자 기능",description = "사이트 이용을 위한 회원가입, 로그인, 로그아웃 기능을 제공한다.")
public class AuthController {

    private final AuthService authService;
    @Operation(description = "회원가입 기능을 제공한다.")
    @PostMapping("/join")
    public ResponseEntity<UserDto> join(@ParameterObject @Validated @RequestBody JoinDto joinDto) {
        UserDto userDto = authService.join(joinDto);

        return ResponseEntity.ok(userDto);
    }
    @Operation(description = "로그인을 할 수 있다.")
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@ParameterObject @RequestBody LoginDto loginDto) {
        JwtToken jwtToken = authService.login(loginDto);

        return ResponseEntity.ok(jwtToken);
    }
    @Operation(description = "로그아웃을 할 수 있다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();

        return ResponseEntity.noContent().build();
    }
    @Operation(description = "만료된 토큰을 재발급한다.")
    @PostMapping("/reissue-token")
    public ResponseEntity<JwtToken> reissueToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        JwtToken jwtToken = authService.reissueToken(bearerToken);

        return ResponseEntity.ok(jwtToken);
    }
}
