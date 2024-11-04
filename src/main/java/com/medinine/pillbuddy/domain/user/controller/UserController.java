package com.medinine.pillbuddy.domain.user.controller;

import com.medinine.pillbuddy.domain.user.dto.UserDto;
import com.medinine.pillbuddy.domain.user.dto.UserPasswordUpdateDto;
import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.dto.UserUpdateDto;
import com.medinine.pillbuddy.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "회원정보 관련 기능", description = "회원정보 수정, 조회 및 회원탈퇴 기능을 제공한다.")
public class UserController {

    private final UserService userService;

    @Operation(description = "사용자는 자신의 회원정보를 조회할 수 있다.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserInfo(@PathVariable Long userId, UserType userType) {
        UserDto userDto = userService.findUser(userId, userType);

        return ResponseEntity.ok(userDto);
    }

    @Operation(description = "사용자는 회원정보를 수정할 수 있다.")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserInfo(
            @PathVariable Long userId,
            @Validated @RequestBody UserUpdateDto userUpdateDto
    ) {
        UserDto userDto = userService.updateUserInfo(userId, userUpdateDto);

        return ResponseEntity.ok(userDto);
    }

    @Operation(description = "사용자는 비밀번호를 수정할 수 있다.")
    @PutMapping("/{userId}/password")
    public ResponseEntity<UserDto> updateUserPassword(
            @PathVariable Long userId,
            @Validated @RequestBody UserPasswordUpdateDto userPasswordUpdateDto
    ) {
        UserDto userDto = userService.updateUserPassword(userId, userPasswordUpdateDto);

        return ResponseEntity.ok(userDto);
    }

    @Operation(description = "사용자는 회원 탈퇴를 할 수 있다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, UserType userType) {
        userService.deleteUser(userId, userType);

        return ResponseEntity.noContent().build();
    }
}
