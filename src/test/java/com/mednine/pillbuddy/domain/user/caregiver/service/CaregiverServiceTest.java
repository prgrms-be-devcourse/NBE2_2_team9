package com.mednine.pillbuddy.domain.user.caregiver.service;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class CaregiverServiceTest {
    @Autowired
    private CaregiverService caregiverService;

    @Test
    @Transactional
    public void caretakerServiceTests1() {
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        assertThatThrownBy(() -> caregiverService.register(caregiverId, caretakerId))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("이미 등록된 사용자 정보입니다");
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
