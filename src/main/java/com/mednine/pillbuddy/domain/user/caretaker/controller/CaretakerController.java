package com.mednine.pillbuddy.domain.user.caretaker.controller;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.domain.user.caretaker.service.CaretakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers")
public class CaretakerController {

    private final CaretakerService caretakerService;

    @PostMapping("/{caretakerId}/caregivers/{caregiverId}")
    public ResponseEntity<CaretakerCaregiverDTO> addCaregiver(@PathVariable Long caretakerId, @PathVariable Long caregiverId) {
        CaretakerCaregiverDTO savedCaretakerCaregiverDTO = caretakerService.register(caretakerId, caregiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO);
    }

    @DeleteMapping("/{caretakerId}/caregivers/{caregiverId}")
    public ResponseEntity<Map<String, String>> deleteCaregiver(@PathVariable Long caretakerId, @PathVariable Long caregiverId) {
        caretakerService.remove(caretakerId, caregiverId);
        Map<String, String> result = Map.of("Process", "Success");
        return ResponseEntity.ok(result);
    }
}