package com.mednine.pillbuddy.domain.user.caretaker.service;

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
public class CaretakerServiceImpl implements CaretakerService {

    private final CaretakerCaregiverRepository caretakerCaregiverRepository;
    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    @Transactional
    public CaretakerCaregiverDTO register(Long caretakerId, Long caregiverId) {
        Caretaker caretaker = caretakerRepository.findById(caretakerId).orElseThrow(
                () -> new PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND)
        );
        Caregiver caregiver = caregiverRepository.findById(caregiverId).orElseThrow(
                () -> new PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND)
        );


        CaretakerCaregiver caretakerCaregiver = CaretakerCaregiver.builder()
                .caregiver(caregiver)
                .caretaker(caretaker)
                .build();

        if (caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId).isPresent()) {
            throw new PillBuddyCustomException(ErrorCode.CARETAKER_CAREGIVER_NOT_REGISTERED);
        }

        CaretakerCaregiver savedCaretakerCaregiver = caretakerCaregiverRepository.save(caretakerCaregiver);
        return CaretakerCaregiverDTO.entityToDTO(savedCaretakerCaregiver);
    }

    @Override
    @Transactional
    public void remove(Long caretakerId, Long caregiverId) {
        CaretakerCaregiver caretakerCaregiver = caretakerCaregiverRepository
                .findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CARETAKER_CAREGIVER_NOT_MATCHED));

        caretakerCaregiverRepository.delete(caretakerCaregiver);
    }
}
