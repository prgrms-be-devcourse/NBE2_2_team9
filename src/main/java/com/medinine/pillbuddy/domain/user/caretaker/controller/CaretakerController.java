package com.medinine.pillbuddy.domain.user.caretaker.controller;

import com.medinine.pillbuddy.domain.record.dto.RecordDTO;
import com.medinine.pillbuddy.domain.record.service.RecordService;
import com.medinine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.medinine.pillbuddy.domain.user.caretaker.service.CaretakerService;
import com.medinine.pillbuddy.domain.userMedication.service.UserMedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers")
@Tag(name = "사용자 기능",description = "사용자는 보호자를 등록,삭제할 수 있으며, 복용 기록여부를 등록,조회할 수 있다.")
public class CaretakerController {

    private final RecordService recordService;
    private final CaretakerService caretakerService;
    private final UserMedicationService userMedicationService;
    @Operation(description = "사용자는 보호자를 등록할 수 있다.")
    @PostMapping("/{caretakerId}/caregivers/{caregiverId}")
    public ResponseEntity<CaretakerCaregiverDTO> addCaregiver(@PathVariable Long caretakerId, @PathVariable Long caregiverId) {
        CaretakerCaregiverDTO savedCaretakerCaregiverDTO = caretakerService.register(caretakerId, caregiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO);
    }
    @Operation(description = "사용자는 보호자를 삭제할 수 있다.")
    @DeleteMapping("/{caretakerId}/caregivers/{caregiverId}")
    public ResponseEntity<Map<String, String>> deleteCaregiver(@PathVariable Long caretakerId, @PathVariable Long caregiverId) {
        caretakerService.remove(caretakerId, caregiverId);
        Map<String, String> result = Map.of("Process", "Success");
        return ResponseEntity.ok(result);
    }
    @Operation(description = "사용자는 지정일의 약 복용 기록을 조회할 수 있다.")
    @GetMapping("/{caretakerId}/user-medications/records")
    public ResponseEntity<List<RecordDTO>> getRecordsByDate(
            @PathVariable Long caretakerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<RecordDTO> records = userMedicationService.getUserMedicationRecordsByDate(caretakerId, date.atStartOfDay());
        return ResponseEntity.ok(records);
    }
    @Operation(description = "사용자는 새로운 약 복용 기록을 등록할 수 있다.")
    @PostMapping("/user-medications/{userMedicationId}/records")
    public ResponseEntity<RecordDTO> addRecord(@PathVariable Long userMedicationId) {
        RecordDTO savedRecordDTO = recordService.registerRecord(userMedicationId);
        return ResponseEntity.ok(savedRecordDTO);
    }
    @Operation(description = "사용자는 약을 복용했다 표시할 수 있다.")
    @PutMapping("/user-medications/{userMedicationId}/records/{recordId}")
    public ResponseEntity<RecordDTO> updateRecord(@PathVariable Long userMedicationId,
                                                  @PathVariable Long recordId) {
        RecordDTO savedRecordDTO = recordService.modifyTaken(userMedicationId, recordId);
        return ResponseEntity.ok(savedRecordDTO);
    }
}

