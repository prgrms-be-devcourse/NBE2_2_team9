package com.medinine.pillbuddy.domain.userMedication.service;

import com.medinine.pillbuddy.domain.record.dto.RecordDTO;
import com.medinine.pillbuddy.domain.record.entity.Record;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.medinine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.medinine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.medinine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public List<RecordDTO> getUserMedicationRecordsByDate(Long caretakerId, LocalDateTime date) {
        List<UserMedication> userMedications = userMedicationRepository.findByCaretakerId(caretakerId);
        List<RecordDTO> recordDTOList = new ArrayList<>();

        for (UserMedication medication : userMedications) {
            List<Record> records = medication.getRecords().stream()
                    .filter(record -> record.getDate().toLocalDate().equals(date.toLocalDate()))
                    .toList();

            for (Record record : records) {
                recordDTOList.add(new RecordDTO(record));
            }
        }
        return recordDTOList;
    }
}
