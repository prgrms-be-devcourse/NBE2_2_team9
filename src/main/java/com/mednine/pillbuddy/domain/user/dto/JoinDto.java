package com.mednine.pillbuddy.domain.user.dto;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class JoinDto {

    @NotBlank
    @Size(min = 2, max = 30)
    private String username;

    @NotBlank
    @Size(min = 5, max = 20)
    private String loginId;

    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 10, max = 12)
    @Pattern(regexp = "^[0-9]+$")
    private String phoneNumber;

    @NotNull
    private UserType userType;

    public Caregiver toCaregiverEntity() {
        return Caregiver.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
    }

    public Caretaker toCaretakerEntity() {
        return Caretaker.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
    }
}