package com.medinine.pillbuddy.domain.medicationApi.entity;

import com.medinine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Medication{
    @Id
    private String itemSeq;

    private String entpName;

    @Column(nullable = false)
    private String itemName;

    @Column(columnDefinition = "text")
    private String efcyQesitm;
    @Column(columnDefinition = "text")
    private String useMethodQesitm;
    @Column(columnDefinition = "text")
    private String atpnWarnQesitm;
    @Column(columnDefinition = "text")
    private String atpnQesitm;
    @Column(columnDefinition = "text")
    private String intrcQesitm;
    @Column(columnDefinition = "text")
    private String seQesitm;
    @Column(columnDefinition = "text")
    private String depositMethodQesitm;

    private String itemImagePath;

    public static Medication createMedication(MedicationDTO medicationDTO) {
        Medication medication = Medication.builder().itemName(medicationDTO.getItemName())
                .entpName(medicationDTO.getEntpName())
                .itemSeq(medicationDTO.getItemSeq())
                .efcyQesitm(medicationDTO.getEfcyQesitm())
                .useMethodQesitm(medicationDTO.getUseMethodQesitm())
                .atpnWarnQesitm(medicationDTO.getAtpnWarnQesitm())
                .atpnQesitm(medicationDTO.getAtpnQesitm())
                .intrcQesitm(medicationDTO.getIntrcQesitm())
                .seQesitm(medicationDTO.getSeQesitm())
                .depositMethodQesitm(medicationDTO.getDepositMethodQesitm())
                .itemImagePath(medicationDTO.getItemImagePath()).build();
        return medication;
    }

    public static MedicationDTO changeDTO(Medication medication) {
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setItemName(medication.getItemName());
        medicationDTO.setEntpName(medication.getEntpName());
        medicationDTO.setItemSeq(medication.getItemSeq());
        medicationDTO.setEfcyQesitm(medication.getEfcyQesitm());
        medicationDTO.setAtpnQesitm(medication.getAtpnQesitm());
        medicationDTO.setIntrcQesitm(medication.getIntrcQesitm());
        medicationDTO.setSeQesitm(medication.getSeQesitm());
        medicationDTO.setAtpnWarnQesitm(medication.getAtpnWarnQesitm());
        medicationDTO.setItemImagePath(medication.getItemImagePath());
        medicationDTO.setUseMethodQesitm(medication.getUseMethodQesitm());
        medicationDTO.setDepositMethodQesitm(medication.getDepositMethodQesitm());

        return medicationDTO;
    }



}
