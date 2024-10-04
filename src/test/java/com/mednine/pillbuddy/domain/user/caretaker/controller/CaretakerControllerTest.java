package com.mednine.pillbuddy.domain.user.caretaker.controller;

import com.mednine.pillbuddy.domain.record.dto.RecordDTO;
import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CaretakerControllerTest {

    @Autowired
    private CaretakerController caretakerController;

    @Test
    @Transactional
    @DisplayName("사용자 기능 중 보호자 추가 기능 테스트")
    public void addCaregiver() {
        Long caretakerId = 2L;
        Long caregiverId = 1L;

        ResponseEntity<CaretakerCaregiverDTO> caretakerCaregiver = caretakerController.addCaregiver(caretakerId, caregiverId);

        assertThat(caretakerCaregiver).isNotNull();
        assertThat(caretakerCaregiver.getBody()).isNotNull();
    }

    @Test
    @DisplayName("사용자 기능 중 보호자 삭제 기능 테스트")
    public void caretakerServiceTests3() {
        Long caretakerId = 2L;
        Long caregiverId = 2L;

        caretakerController.deleteCaregiver(caretakerId, caregiverId);
    }

    @Test
    @Transactional
    @DisplayName("사용자의 날짜 별 약 복용 여부 조회 테스트")
    public void getRecordsByDate() {
        Long caretakerId = 1L;
        String sampleDate = "2024-09-25";

        LocalDate date = LocalDate.parse(sampleDate);

        ResponseEntity<List<RecordDTO>> recordsByDate = caretakerController.getRecordsByDate(caretakerId, date);

        assertThat(recordsByDate.getBody()).isNotNull();
        assertThat(recordsByDate.getStatusCode()).isEqualTo(HttpStatus.OK);

        RecordDTO recordDTO = recordsByDate.getBody().get(0);

        assertThat(recordDTO).isNotNull(); // 첫 번째 레코드가 null이 아닌지 확인
        assertThat(recordDTO.getRecordId()).isEqualTo(1); // recordId 검증
        assertThat(recordDTO.getDate().toString()).isEqualTo("2024-09-25T09:00"); // 날짜 검증
        assertThat(recordDTO.getMedicationName()).isEqualTo("Aspirin"); // 약 이름 검증
        assertThat(recordDTO.getTaken()).isEqualTo("TAKEN"); // 복용 여부 검증
    }

    @Test
    @Transactional
    @DisplayName("사용자의 약 복용 여부 수정 테스트")
    public void updateMedicationByTaken() {
        Long userMedicationId = 2L;
        Long recordId = 2L;

        ResponseEntity<RecordDTO> recordDTOResponseEntity = caretakerController.updateRecord(userMedicationId, recordId);

        assertThat(recordDTOResponseEntity).isNotNull();
        assertThat(recordDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordDTOResponseEntity.getBody().getTaken()).isEqualTo("TAKEN");
    }

    @Test
    @Transactional
    @DisplayName("사용자의 약 정보에 대한 기록 등록")
    public void addRecord() {
        Long userMedicationId = 2L;

        ResponseEntity<RecordDTO> recordDTOResponseEntity = caretakerController.addRecord(userMedicationId);
        assertThat(recordDTOResponseEntity).isNotNull();
        assertThat(recordDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordDTOResponseEntity.getBody().getTaken()).isEqualTo("UNTAKEN");
    }
}