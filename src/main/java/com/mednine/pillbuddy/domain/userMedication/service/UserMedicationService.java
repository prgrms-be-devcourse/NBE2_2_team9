package com.mednine.pillbuddy.domain.userMedication.service;

import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;

import java.util.List;

public interface UserMedicationService {

    UserMedicationDTO register(Long caretakerId, UserMedicationDTO userMedicationDTO);

    List<UserMedicationDTO> retrieve(Long caretakerId);

    UserMedicationDTO modify(Long caretakerId, Long userMedicationId, UserMedicationDTO userMedicationDTO);
}
