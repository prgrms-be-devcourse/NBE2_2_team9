package com.mednine.pillbuddy.domain.user.caretaker.controller;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.domain.user.caretaker.service.CaretakerCaregiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers/{caretakerId}/caregivers/{caregiverId}")
public class CaretakerCaregiverController {

    private final CaretakerCaregiverService caretakerCaregiverService;

    @PostMapping
    public ResponseEntity<CaretakerCaregiverDTO> createCaretakerCaregiver(@PathVariable Long caretakerId, @PathVariable Long caregiverId) {
        CaretakerCaregiverDTO savedCaretakerCaregiverDTO = caretakerCaregiverService.register(caretakerId, caregiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO);
    }
}
