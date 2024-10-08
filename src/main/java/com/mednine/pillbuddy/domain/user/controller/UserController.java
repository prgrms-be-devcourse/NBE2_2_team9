package com.mednine.pillbuddy.domain.user.controller;

import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserPasswordUpdateDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.dto.UserUpdateDto;
import com.mednine.pillbuddy.domain.user.service.UserService;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "이용자 기능",description = "사이트 이용을 위한 회원가입, 회원정보 수정 등 다양한 기능을 제공한다.")
public class UserController {

    private final UserService userService;
    @Operation(description = "이용자는 회원가입을 할 수 있다.")
    @PostMapping("/join")
    public ResponseEntity<UserDto> join(@ParameterObject @Validated @RequestBody JoinDto joinDto) {
        UserDto userDto = userService.join(joinDto);

        return ResponseEntity.ok(userDto);
    }
    @Operation(description = "이용자는 로그인을 할 수 있다.")
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@ParameterObject @RequestBody LoginDto loginDto) {
        JwtToken jwtToken = userService.login(loginDto);

        return ResponseEntity.ok(jwtToken);
    }
    @Operation(description = "이용자는 로그아웃을 할 수 있다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();

        return ResponseEntity.noContent().build();
    }
    @Operation(description = "이용자는 ")
    @PostMapping("/reissue-token")
    public ResponseEntity<JwtToken> reissueToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        JwtToken jwtToken = userService.reissueToken(bearerToken);

        return ResponseEntity.ok(jwtToken);
    }
    @Operation(description = "이용자는 회원정보를 조회할 수 있다.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserInfo(@PathVariable Long userId,@ParameterObject UserType userType) {
        UserDto userDto = userService.findUser(userId, userType);

        return ResponseEntity.ok(userDto);
    }
    @Operation(description = "이용자는 회원정보를 수정할 수 있다.")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserInfo(
            @PathVariable Long userId,
            @ParameterObject @Validated @RequestBody UserUpdateDto userUpdateDto
    ) {
        UserDto userDto = userService.updateUserInfo(userId, userUpdateDto);

        return ResponseEntity.ok(userDto);
    }
    @Operation(description = "이용자는 비밀번호를 수정할 수 있다.")
    @PutMapping("/{userId}/password")
    public ResponseEntity<UserDto> updateUserPassword(
            @PathVariable Long userId,
            @ParameterObject @Validated @RequestBody UserPasswordUpdateDto userPasswordUpdateDto
            ) {
        UserDto userDto = userService.updateUserPassword(userId, userPasswordUpdateDto);

        return ResponseEntity.ok(userDto);
    }
    @Operation(description = "이용자는 회원 탈퇴할 수 있다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId,@ParameterObject UserType userType) {
        userService.deleteUser(userId, userType);

        return ResponseEntity.noContent().build();
    }
}
