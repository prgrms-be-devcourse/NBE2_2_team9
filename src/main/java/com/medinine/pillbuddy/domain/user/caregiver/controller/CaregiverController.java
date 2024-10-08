package com.medinine.pillbuddy.domain.user.caregiver.controller;

import com.medinine.pillbuddy.domain.user.caregiver.service.CaregiverService;
import com.medinine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.medinine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/caregivers/{caregiverId}/caretakers")
@RequiredArgsConstructor
@Tag(name = "보호자 기능",description = "보호자는 사용자를 등록,삭제할 수 있으며, 사용자의 약 정보를 확인할 수 있다.")
public class CaregiverController {

    private final CaregiverService caregiverService;
    @Operation(description = "보호자는 사용자의 약 정보를 확인할 수 있다.")
    @GetMapping("/{caretakerId}/caretaker-medications")
    public ResponseEntity<List<UserMedicationDTO>> getCaretakerMedications(@PathVariable Long caregiverId, @PathVariable Long caretakerId) {
        List<UserMedicationDTO> medications = caregiverService.getCaretakerMedications(caregiverId, caretakerId);
        return ResponseEntity.status(HttpStatus.OK).body(medications);
    }
    @Operation(description = "보호자는 사용자를 등록할 수 있다.")
    @PostMapping
    public ResponseEntity<CaretakerCaregiverDTO> addCaretaker(@PathVariable Long caregiverId, @RequestParam Long caretakerId) {
        CaretakerCaregiverDTO savedCaretakerCaregiverDTO = caregiverService.register(caregiverId, caretakerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO);
    }
    @Operation(description = "보호자는 사용자를 삭제할 수 있다.")
    @DeleteMapping("/{caretakerId}")
    public ResponseEntity<Map<String, String>> deleteCaretaker(@PathVariable Long caregiverId, @PathVariable Long caretakerId) {
        caregiverService.remove(caregiverId, caretakerId);
        Map<String, String> result = Map.of("Process", "Success");
        return ResponseEntity.ok(result);
    }
}