package com.mednine.pillbuddy.domain.user.caretaker.service;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CaretakerCaregiverServiceTest {

    @Autowired
    private CaretakerCaregiverService caretakerCaregiverService;

    // 이미 저장된 정보 예외 테스트 : fail 되는 게 맞는 것입니다
    @Test
    @Transactional
    public void caretakerCaregiverServiceTests1() {
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        CaretakerCaregiverDTO register = caretakerCaregiverService.register(caretakerId, caregiverId);

        Assertions.assertThat(register.getCaretaker().getId()).isEqualTo(1);
        Assertions.assertThat(register.getCaretaker().getId()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void caretakerCaregiverServiceTests2() {
        Long caretakerId = 2L;
        Long caregiverId = 1L;

        CaretakerCaregiverDTO register = caretakerCaregiverService.register(caretakerId, caregiverId);

        Assertions.assertThat(register.getCaretaker().getId()).isEqualTo(2);
        Assertions.assertThat(register.getCaregiver().getId()).isEqualTo(1);
    }
}
