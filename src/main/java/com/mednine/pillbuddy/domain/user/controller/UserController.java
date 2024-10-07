package com.mednine.pillbuddy.domain.user.controller;

import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserPasswordUpdateDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.dto.UserUpdateDto;
import com.mednine.pillbuddy.domain.user.service.UserService;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
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
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserInfo(@PathVariable Long userId, UserType userType) {
        UserDto userDto = userService.findUser(userId, userType);

        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserInfo(
            @PathVariable Long userId,
            @Validated @RequestBody UserUpdateDto userUpdateDto
    ) {
        UserDto userDto = userService.updateUserInfo(userId, userUpdateDto);

        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<UserDto> updateUserPassword(
            @PathVariable Long userId,
            @Validated @RequestBody UserPasswordUpdateDto userPasswordUpdateDto
            ) {
        UserDto userDto = userService.updateUserPassword(userId, userPasswordUpdateDto);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, UserType userType) {
        userService.deleteUser(userId, userType);

        return ResponseEntity.noContent().build();
    }
}
