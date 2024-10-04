package com.mednine.pillbuddy.domain.record.service;

import com.mednine.pillbuddy.domain.record.dto.RecordDTO;

public interface RecordService {
    RecordDTO modifyTaken(Long userMedicationId, Long recordId);
}
