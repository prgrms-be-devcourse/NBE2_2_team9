package com.mednine.pillbuddy.domain.userMedication.controller;

import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.mednine.pillbuddy.domain.userMedication.service.UserMedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caretakers/{caretakerId}/user-medications")
@RequiredArgsConstructor
public class UserMedicationController {

    private final UserMedicationService userMedicationService;

    @PostMapping
    public ResponseEntity<UserMedicationDTO> createUserMedication(@PathVariable Long caretakerId, @RequestBody UserMedicationDTO userMedicationDTO) {
        UserMedicationDTO savedUserMedication = userMedicationService.register(caretakerId, userMedicationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserMedication);
    }

    @GetMapping
    public ResponseEntity<List<UserMedicationDTO>> getUserMedications(@PathVariable Long caretakerId) {
        List<UserMedicationDTO> userMedicationDTOList = userMedicationService.retrieve(caretakerId);
        return ResponseEntity.ok(userMedicationDTOList);
    }

    @PutMapping("/{userMedicationId}")
    public ResponseEntity<UserMedicationDTO> updateUserMedication(@PathVariable Long caretakerId, @PathVariable Long userMedicationId, @RequestBody UserMedicationDTO userMedicationDTO) {
        UserMedicationDTO updateUserMedication = userMedicationService.modify(caretakerId, userMedicationId, userMedicationDTO);
        return ResponseEntity.ok(updateUserMedication);
    }
}
