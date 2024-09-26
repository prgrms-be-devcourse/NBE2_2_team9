package com.mednine.pillbuddy.domain.user.caregiver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CaregiverDTO {

    @NotBlank
    @Size(max = 30)
    private final String username;

    @NotBlank
    @Size(max = 20)
    private final String loginId;

    @NotBlank
    @Size(max = 30)
    private final String password;

    @NotBlank
    @Email
    @Size(max = 50)
    private final String email;
}