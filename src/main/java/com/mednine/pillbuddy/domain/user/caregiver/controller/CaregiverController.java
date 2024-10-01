package com.mednine.pillbuddy.domain.user.caregiver.controller;

import com.mednine.pillbuddy.domain.user.caregiver.service.CaregiverService;
import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/caregivers/{caregiverId}/caretakers")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;

    @GetMapping("/{caretakerId}/caretaker-medications")
    public ResponseEntity<List<UserMedicationDTO>> getCaretakerMedications(@PathVariable Long caregiverId, @PathVariable Long caretakerId) {
        List<UserMedicationDTO> medications = caregiverService.getCaretakerMedications(caregiverId, caretakerId);
        return ResponseEntity.status(HttpStatus.OK).body(medications);
    }

    @PostMapping
    public ResponseEntity<CaretakerCaregiverDTO> addCaretaker(@PathVariable Long caregiverId, @RequestParam Long caretakerId) {
        CaretakerCaregiverDTO savedCaretakerCaregiverDTO = caregiverService.register(caregiverId, caretakerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO);
    }

    @DeleteMapping("/{caretakerId}")
    public ResponseEntity<Map<String, String>> deleteCaretaker(@PathVariable Long caregiverId, @PathVariable Long caretakerId) {
        caregiverService.remove(caregiverId, caretakerId);
        Map<String, String> result = Map.of("Process", "Success");
        return ResponseEntity.ok(result);
    }
}