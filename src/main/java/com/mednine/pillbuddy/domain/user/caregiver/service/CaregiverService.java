package com.mednine.pillbuddy.domain.user.caregiver.service;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CaregiverService {
    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;
    private final CaretakerCaregiverRepository caretakerCaregiverRepository;

    @Transactional
    public CaretakerCaregiverDTO register(Long caregiverId, Long caretakerId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId).orElseThrow(
                () -> new PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND)
        );
        Caretaker caretaker = caretakerRepository.findById(caretakerId).orElseThrow(
                () -> new PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND)
        );

        CaretakerCaregiver caretakerCaregiver = CaretakerCaregiver.builder()
                .caregiver(caregiver)
                .caretaker(caretaker)
                .build();

        if (caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId).isPresent()) {
            throw new PillBuddyCustomException(ErrorCode.CAREGIVER_CARETAKER_NOT_REGISTERED);
        }

        CaretakerCaregiver savedCaretakerCaregiver = caretakerCaregiverRepository.save(caretakerCaregiver);
        return CaretakerCaregiverDTO.entityToDTO(savedCaretakerCaregiver);
    }

    @Transactional
    public void remove(Long caregiverId, Long caretakerId) {
        CaretakerCaregiver caretakerCaregiver = caretakerCaregiverRepository
                .findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CAREGIVER_CARETAKER_NOT_MATCHED));

        caretakerCaregiverRepository.delete(caretakerCaregiver);
    }
}