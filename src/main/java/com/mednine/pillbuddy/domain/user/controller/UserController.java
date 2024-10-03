package com.mednine.pillbuddy.domain.user.controller;

import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.service.UserService;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserDto> join(@Validated @RequestBody JoinDto joinDto) {
        UserDto userDto = userService.join(joinDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginDto loginDto) {
        JwtToken jwtToken = userService.login(loginDto);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue-token")
    public ResponseEntity<JwtToken> reissueToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        JwtToken jwtToken = userService.reissueToken(bearerToken);

        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserInfo(@PathVariable Long userId, UserType userType) {
        UserDto userDto = userService.findUser(userId, userType);

        return ResponseEntity.ok(userDto);
    }

//    @PreAuthorize("hasRole('ROEL_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUserInfo() {
        List<UserDto> userDtoList = userService.findAllUser();

        return ResponseEntity.ok(userDtoList);
    }

//    @PreAuthorize("hasRole('ROEL_ADMIN')")
    @GetMapping("/caretakers")
    public ResponseEntity<List<UserDto>> findAllCaretakerInfo() {
        List<UserDto> userDtoList = userService.findAllCaretaker();

        return ResponseEntity.ok(userDtoList);
    }

//    @PreAuthorize("hasRole('ROEL_ADMIN')")
    @GetMapping("/caregivers")
    public ResponseEntity<List<UserDto>> findAllCaregiverInfo() {
        List<UserDto> userDtoList = userService.findAllCaregiver();

        return ResponseEntity.ok(userDtoList);
    }
}
