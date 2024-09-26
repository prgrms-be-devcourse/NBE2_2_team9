package com.mednine.pillbuddy.domain.user.caregiver.service;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("Caregiver not found"));

        Caretaker caretaker = caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new IllegalArgumentException("Caretaker not found"));

        CaretakerCaregiver caretakerCaregiver = CaretakerCaregiver.builder()
                .caregiver(caregiver)
                .caretaker(caretaker)
                .build();

        caretakerCaregiverRepository.save(caretakerCaregiver);
    }
}