package com.mednine.pillbuddy.domain.userMedication.service;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMedicationServiceImpl implements UserMedicationService {

    private final UserMedicationRepository userMedicationRepository;
    private final CaretakerRepository caretakerRepository;

    @Override
    @Transactional
    public UserMedicationDTO register(Long caretakerId, UserMedicationDTO userMedicationDTO) {
        Caretaker caretaker = caretakerRepository.findById(caretakerId).orElseThrow(()
                        -> new PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND));

        UserMedication userMedication = userMedicationDTO.toEntity();
        userMedication.changeCaretaker(caretaker);
        UserMedication savedUserMedication = userMedicationRepository.save(userMedication);
        log.info("Saved userMedication: {}",savedUserMedication);

        return UserMedicationDTO.entityToDTO(savedUserMedication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMedicationDTO> retrieve(Long caretakerId) {
        List<UserMedication> medications = userMedicationRepository.findByCaretakerId(caretakerId);
        log.info("Retrieved medications: {}",medications);

        return medications.stream().map(UserMedicationDTO::entityToDTO).toList();
    }

    @Override
    @Transactional
    public UserMedicationDTO modify(Long caretakerId, Long userMedicationId, UserMedicationDTO userMedicationDTO) {
        UserMedication userMedication = userMedicationRepository.findById(userMedicationId).orElseThrow(
                () -> new PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND));

        if (!userMedication.getCaretaker().getId().equals(caretakerId)) {
            throw new PillBuddyCustomException(ErrorCode.MEDICATION_NOT_VALID);
        }

        try {
            userMedication.changeName(userMedicationDTO.getName());
            userMedication.changeDescription(userMedicationDTO.getDescription());
            userMedication.changeDosage(userMedicationDTO.getDosage());

            UserMedication updatedUserMedication = userMedicationRepository.save(userMedication);

            return UserMedicationDTO.entityToDTO(updatedUserMedication);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
