package com.mednine.pillbuddy.domain.user.caregiver.service;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
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

    private final CaregiverRepository caregiverRepository;
    private final CaretakerRepository caretakerRepository;
    private final CaretakerCaregiverRepository caretakerCaregiverRepository;

    @Transactional
    public void registerCaretaker(Long caregiverId, Long caretakerId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND));

        Caretaker caretaker = caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND));

        CaretakerCaregiver caretakerCaregiver = CaretakerCaregiver.builder()
                .caregiver(caregiver)
                .caretaker(caretaker)
                .build();

        caretakerCaregiverRepository.save(caretakerCaregiver);
    }

    @Transactional
    public void deleteCaretaker(Long caregiverId, Long caretakerId) {
        caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND));

        caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND));

        CaretakerCaregiver caretakerCaregiver = caretakerCaregiverRepository
                .findByCaretaker_IdAndCaregiver_Id(caretakerId, caregiverId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CARETAKER_CAREGIVER_NOT_FOUND));

        caretakerCaregiverRepository.delete(caretakerCaregiver);
    }
}