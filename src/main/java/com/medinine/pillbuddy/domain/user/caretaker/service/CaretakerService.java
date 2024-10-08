package com.medinine.pillbuddy.domain.user.caretaker.service;

import com.medinine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;

public interface CaretakerService {
    CaretakerCaregiverDTO register(Long caretakerId, Long caregiverId);
    void remove(Long caretakerId, Long caregiverId);
}
