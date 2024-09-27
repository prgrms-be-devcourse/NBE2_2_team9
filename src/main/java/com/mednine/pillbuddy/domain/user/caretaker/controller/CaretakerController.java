package com.mednine.pillbuddy.domain.user.caretaker.controller;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.domain.user.caretaker.service.CaretakerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers/{caretakerId}/caregivers/{caregiverId}")
public class CaretakerController {

    private final CaretakerServiceImpl caretakerServiceImpl;

    @PostMapping
    public ResponseEntity<CaretakerCaregiverDTO> addCaregiver(@PathVariable Long caretakerId, @PathVariable Long caregiverId) {
        CaretakerCaregiverDTO savedCaretakerCaregiverDTO = caretakerServiceImpl.register(caretakerId, caregiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO);
    }
}
