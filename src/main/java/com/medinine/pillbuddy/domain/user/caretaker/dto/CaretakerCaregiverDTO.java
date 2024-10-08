package com.medinine.pillbuddy.domain.user.caretaker.dto;

import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerCaregiverDTO {
    private Long id;
    private Caretaker caretaker;
    private Caregiver caregiver;

    public static CaretakerCaregiverDTO entityToDTO(CaretakerCaregiver caretakerCaregiver) {
        return CaretakerCaregiverDTO
                .builder()
                .id(caretakerCaregiver.getId())
                .caretaker(caretakerCaregiver.getCaretaker())
                .caregiver(caretakerCaregiver.getCaregiver())
                .build();
    }
}
