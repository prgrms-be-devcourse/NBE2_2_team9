package com.mednine.pillbuddy.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
