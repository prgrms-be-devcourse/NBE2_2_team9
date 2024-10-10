package com.medinine.pillbuddy.domain.medicationApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationDTO {
    private String itemName;

    private String entpName;

    private Long itemSeq;

    private String efcyQesitm;

    private String useMethodQesitm;

    private String atpnWarnQesitm;

    private String atpnQesitm;

    private String intrcQesitm;

    private String seQesitm;

    private String depositMethodQesitm;

    @JsonProperty("itemImage")
    private String itemImagePath;
}
