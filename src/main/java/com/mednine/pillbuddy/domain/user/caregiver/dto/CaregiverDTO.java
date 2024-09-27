package com.mednine.pillbuddy.domain.user.caregiver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CaregiverDTO {

    @NotNull
    private final Long caretakerId; // 등록할 caretaker의 ID
}