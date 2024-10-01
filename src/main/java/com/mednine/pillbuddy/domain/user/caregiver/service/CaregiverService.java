package com.mednine.pillbuddy.domain.user.caregiver.service;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaregiverService {
    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;
    private final CaretakerCaregiverRepository caretakerCaregiverRepository;
    private final UserMedicationRepository userMedicationRepository;

    @Transactional(readOnly = true)
    public List<UserMedicationDTO> getCaretakerMedications(Long caregiverId, Long caretakerId) {
        caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CAREGIVER_CARETAKER_NOT_MATCHED));

        // 약 정보 조회
        List<UserMedicationDTO> medications = userMedicationRepository.findByCaretakerId(caretakerId)
                .stream()
                .map(UserMedicationDTO::entityToDTO)
                .collect(Collectors.toList());

        // 약 정보가 비어 있을 경우 예외 처리
        if (medications.isEmpty()) {
            throw new PillBuddyCustomException(ErrorCode.MEDICATION_IS_NULL);
        }

        return medications;
    }

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
            throw new PillBuddyCustomException(ErrorCode.CARETAKER_ALREADY_REGISTERED);
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