package com.mednine.pillbuddy.domain.user.caretaker.dto;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
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
