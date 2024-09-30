package com.mednine.pillbuddy.domain.user.controller;

import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.service.UserService;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserDto> join(@Validated @RequestBody JoinDto joinDto) {
        log.info("-- UserController.join() called !!");
        UserDto userDto = userService.join(joinDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginDto loginDto) {
        log.info("-- UserController.login() called !!");
        JwtToken jwtToken = userService.login(loginDto);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();

        return ResponseEntity.noContent().build();
    }
}
