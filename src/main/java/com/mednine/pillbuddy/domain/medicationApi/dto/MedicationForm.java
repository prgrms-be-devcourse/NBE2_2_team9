package com.mednine.pillbuddy.domain.medicationApi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MedicationForm {

    private String entpName;
    @NotEmpty
    private String itemName;

    private String itemSeq;
}
