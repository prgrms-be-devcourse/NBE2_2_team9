package com.medinine.pillbuddy.domain.userMedication.dto;

import com.medinine.pillbuddy.domain.userMedication.entity.Frequency;
import com.medinine.pillbuddy.domain.userMedication.entity.MedicationType;
import com.medinine.pillbuddy.domain.userMedication.entity.UserMedication;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMedicationDTO {
    @Schema(hidden = true)
    private Long id;
    @Parameter(name = "약 이름")
    private String name;
    @Parameter(name = "기타 참고 사항")
    private String description;
    @Parameter(name = "복용량")
    private Integer dosage;
    @Parameter(name = "복용 빈도")
    private Frequency frequency;
    @Parameter(name = "약 종류")
    private MedicationType type;
    @Parameter(name = "약 재고량")
    private int stock;
    @Parameter(name = "약 만료일")
    private LocalDateTime expirationDate;
    @Parameter(name = "복용 시작일")
    private LocalDateTime startDate;
    @Parameter(name = "복용 종료일")
    private LocalDateTime endDate;

    public UserMedication toEntity() {
        UserMedication userMedication = UserMedication.builder()
                .name(this.name)
                .description(this.description)
                .dosage(this.dosage)
                .frequency(this.frequency)
                .type(this.type)
                .stock(this.stock)
                .expirationDate(this.expirationDate)
                .startDate(this.startDate)
                .endDate(this.endDate).build();

        return userMedication;
    }

    public static UserMedicationDTO entityToDTO(UserMedication userMedication) {
        UserMedicationDTO userMedicationDTO = UserMedicationDTO.builder()
                .id(userMedication.getId())
                .name(userMedication.getName())
                .description(userMedication.getDescription())
                .dosage(userMedication.getDosage())
                .frequency(userMedication.getFrequency())
                .type(userMedication.getType())
                .stock(userMedication.getStock())
                .expirationDate(userMedication.getExpirationDate())
                .startDate(userMedication.getStartDate())
                .endDate(userMedication.getEndDate())
                .build();

        return userMedicationDTO;
    }
}
