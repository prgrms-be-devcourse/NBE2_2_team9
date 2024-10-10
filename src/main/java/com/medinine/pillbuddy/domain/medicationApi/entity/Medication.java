package com.medinine.pillbuddy.domain.medicationApi.entity;

import com.medinine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.medinine.pillbuddy.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Medication extends BaseTimeEntity implements Persistable<Long> {
    @Id
    private Long itemSeq;

    @Column(length = 100)
    private String entpName;

    @Column(nullable = false,length = 100)
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
    @Column(columnDefinition = "text")
    private String itemImagePath;

    public static Medication createMedication(MedicationDTO medicationDTO) {
        return Medication.builder().itemName(medicationDTO.getItemName())
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


    @Override
    public Long getId() {
        return getItemSeq();
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
