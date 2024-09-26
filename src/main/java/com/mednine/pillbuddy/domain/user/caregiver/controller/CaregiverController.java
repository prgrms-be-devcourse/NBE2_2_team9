package com.mednine.pillbuddy.domain.user.caregiver.controller;

import com.mednine.pillbuddy.domain.user.caregiver.service.CaregiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/caregivers/{id}/caretakers")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;

    @PostMapping
    public ResponseEntity<String> registerCaretaker(@PathVariable Long id, @RequestParam Long caretakerId) {
        caregiverService.registerCaretaker(id, caretakerId);
        return ResponseEntity.ok("Caretaker registered successfully");
    }
}