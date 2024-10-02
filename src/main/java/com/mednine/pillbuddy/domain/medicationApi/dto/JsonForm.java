package com.mednine.pillbuddy.domain.medicationApi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonForm {
    private int totalCount;
    private int nowPageNum;
    private int maxPageNum;
    private List<MedicationDTO> data;
}
