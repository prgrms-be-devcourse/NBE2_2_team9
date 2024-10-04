package com.mednine.pillbuddy.domain.record.service;

import com.mednine.pillbuddy.domain.record.dto.RecordDTO;
import com.mednine.pillbuddy.domain.record.entity.Record;
import com.mednine.pillbuddy.domain.record.entity.Taken;
import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final UserMedicationRepository userMedicationRepository;

    @Override
    @Transactional
    public RecordDTO modifyTaken(Long userMedicationId, Long recordId) {
        UserMedication userMedication = userMedicationRepository.findById(userMedicationId).orElse(null);

        Record record = userMedication.getRecords().stream()
                .filter(x -> x.getId().equals(recordId))
                .findFirst().orElseThrow(() -> new PillBuddyCustomException(ErrorCode.RECORD_NOT_FOUND));

        if (record.getTaken().equals(Taken.UNTAKEN)) {
            record.takeMedication(Taken.TAKEN);
        } else if (record.getTaken().equals(Taken.TAKEN)) {
            throw new PillBuddyCustomException(ErrorCode.RECORD_NOT_VALID);
        }

        return new RecordDTO(record);
    }
}
