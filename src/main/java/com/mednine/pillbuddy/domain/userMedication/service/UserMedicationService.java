package com.mednine.pillbuddy.domain.userMedication.service;

import com.mednine.pillbuddy.domain.record.dto.RecordDTO;
import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserMedicationService {

    UserMedicationDTO register(Long caretakerId, UserMedicationDTO userMedicationDTO);

    List<UserMedicationDTO> retrieve(Long caretakerId);

    UserMedicationDTO modify(Long caretakerId, Long userMedicationId, UserMedicationDTO userMedicationDTO);

    List<RecordDTO> getUserMedicationRecordsByDate(Long caretakerId, LocalDateTime date);
}
