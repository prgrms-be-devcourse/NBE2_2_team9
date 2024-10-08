package com.medinine.pillbuddy.domain.record.service;

import com.medinine.pillbuddy.domain.record.dto.RecordDTO;

public interface RecordService {
    RecordDTO modifyTaken(Long userMedicationId, Long recordId);
    RecordDTO registerRecord(Long userMedicationId);
}
