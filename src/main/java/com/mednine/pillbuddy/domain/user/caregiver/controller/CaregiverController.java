package com.mednine.pillbuddy.domain.user.caregiver.controller;

import com.mednine.pillbuddy.domain.user.caregiver.dto.CaregiverDTO;
import com.mednine.pillbuddy.domain.user.caregiver.service.CaregiverService;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/caregivers/{id}/caretakers")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;

    @PostMapping
    public ResponseEntity<String> registerCaretaker(@PathVariable Long id, @RequestBody CaregiverDTO caregiverDTO) {
        try {
            caregiverService.registerCaretaker(id, caregiverDTO.getCaretakerId());
            return ResponseEntity.ok("Caretaker registered successfully");
        } catch (PillBuddyCustomException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{caretakerId}")
    public ResponseEntity<String> deleteCaretaker(@PathVariable Long id, @PathVariable Long caretakerId) {
        try {
            caregiverService.deleteCaretaker(id, caretakerId);
            return ResponseEntity.ok("Caretaker deleted successfully");
        } catch (PillBuddyCustomException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
        }
    }
}