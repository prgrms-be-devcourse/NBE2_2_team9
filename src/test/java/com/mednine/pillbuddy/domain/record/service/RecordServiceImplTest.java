package com.mednine.pillbuddy.domain.record.service;

import com.mednine.pillbuddy.domain.record.dto.RecordDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class RecordServiceImplTest {
    @Autowired
    private RecordService recordService;

    @Test
    @Transactional
    @DisplayName("사용자 약 복용 여부 기록 서비스 레이어 테스트")
    public void updateMedicationTaken() {
        Long userMedicationId = 2L;
        Long recordId = 2L;

        RecordDTO modifyTaken = recordService.modifyTaken(userMedicationId, recordId);

        assertThat(modifyTaken).isNotNull();
        assertThat(modifyTaken.getRecordId()).isEqualTo(2);
        assertThat(modifyTaken.getTaken()).isEqualTo("TAKEN");
    }
}