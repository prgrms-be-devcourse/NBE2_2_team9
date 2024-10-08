package com.mednine.pillbuddy.domain.userMedication.controller;

import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.mednine.pillbuddy.domain.userMedication.service.UserMedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caretakers/{caretakerId}/user-medications")
@RequiredArgsConstructor
@Tag(name = "사용자 약 정보 기능",description = "사용자는 자신이 복용하는 약을 등록,조회,수정,삭제할 수 있다.")
public class UserMedicationController {

    private final UserMedicationService userMedicationService;
    @Operation(description = "사용자는 자신이 복용하는 약을 등록할 수 있다.")
    @PostMapping
    public ResponseEntity<UserMedicationDTO> createUserMedication(@PathVariable Long caretakerId, @ParameterObject @RequestBody UserMedicationDTO userMedicationDTO) {
        UserMedicationDTO savedUserMedication = userMedicationService.register(caretakerId, userMedicationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserMedication);
    }
    @Operation(description = "사용자는 자신이 복용하는 약을 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<List<UserMedicationDTO>> getUserMedications(@PathVariable Long caretakerId) {
        List<UserMedicationDTO> userMedicationDTOList = userMedicationService.retrieve(caretakerId);
        return ResponseEntity.ok(userMedicationDTOList);
    }
    @Operation(description = "사용자는 자신이 복용하는 약 정보를 수정할 수 있다.")
    @PutMapping("/{userMedicationId}")
    public ResponseEntity<UserMedicationDTO> updateUserMedication(@PathVariable Long caretakerId, @PathVariable Long userMedicationId, @ParameterObject @RequestBody UserMedicationDTO userMedicationDTO) {
        UserMedicationDTO updateUserMedication = userMedicationService.modify(caretakerId, userMedicationId, userMedicationDTO);
        return ResponseEntity.ok(updateUserMedication);
    }
}
