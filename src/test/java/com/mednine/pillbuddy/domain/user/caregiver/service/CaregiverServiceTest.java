package com.mednine.pillbuddy.domain.user.caregiver.service;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CaregiverServiceTest {
    @Autowired
    private CaregiverService caregiverService;

    // 이미 저장된 정보 예외 테스트 : fail 되는 게 맞는 것입니다
    @Test
    @Transactional
    public void caregiverServiceTests1() {
        Long caregiverId = 1L;
        Long caretakerId = 1L;

        CaretakerCaregiverDTO register = caregiverService.register(caregiverId, caretakerId);

        Assertions.assertThat(register.getCaregiver().getId()).isEqualTo(1);
        Assertions.assertThat(register.getCaretaker().getId()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void caregiverServiceTests2() {
        Long caregiverId = 2L;
        Long caretakerId = 1L;

        CaretakerCaregiverDTO register = caregiverService.register(caregiverId, caretakerId);

        Assertions.assertThat(register.getCaregiver().getId()).isEqualTo(2);
        Assertions.assertThat(register.getCaretaker().getId()).isEqualTo(1);
    }

    @Test
    public void caregiverServiceTests3() {
        Long caregiverId = 2L;
        Long caretakerId = 2L;

        caregiverService.remove(caregiverId, caretakerId);
    }
}
