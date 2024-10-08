package com.mednine.pillbuddy.domain.medicationApi.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MedicationForm {

    @NotEmpty
    @Parameter(description = "약이름(필수값)")
    private String itemName;
    @Parameter(description = "회사명")
    private String entpName;
    @Parameter(description = "품목기준코드")
    private String itemSeq;
}