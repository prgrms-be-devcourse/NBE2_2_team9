package com.medinine.pillbuddy.domain.userMedication.dto;

import com.medinine.pillbuddy.domain.userMedication.entity.Frequency;
import com.medinine.pillbuddy.domain.userMedication.entity.MedicationType;
import com.medinine.pillbuddy.domain.userMedication.entity.UserMedication;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMedicationDTO {

    private Long id;
    private String name;
    private String description;
    private Integer dosage;
    private Frequency frequency;
    private MedicationType type;
    private int stock;
    private LocalDateTime expirationDate;
    private LocalDateTime startDate;
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
