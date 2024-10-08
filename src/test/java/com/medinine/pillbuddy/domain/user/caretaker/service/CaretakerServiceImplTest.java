package com.medinine.pillbuddy.domain.user.caretaker.service;

import static org.assertj.core.api.Assertions.*;

import com.medinine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CaretakerServiceImplTest {
    @Autowired
    private CaretakerServiceImpl caretakerService;

    @Test
    @Transactional
    public void caretakerServiceTests1() {
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        assertThatThrownBy(() -> caretakerService.register(caretakerId, caregiverId))
                .isInstanceOf(PillBuddyCustomException.class)
                        .hasMessage("이미 등록된 보호자 정보입니다");
    }

    @Test
    @Transactional
    public void caretakerServiceTests2() {
        Long caretakerId = 2L;
        Long caregiverId = 1L;

        CaretakerCaregiverDTO register = caretakerService.register(caretakerId, caregiverId);

        assertThat(register.getCaretaker().getId()).isEqualTo(2);
        assertThat(register.getCaregiver().getId()).isEqualTo(1);
    }

    @Test
    public void caretakerServiceTests3() {
        Long caretakerId = 2L;
        Long caregiverId = 2L;

        caretakerService.remove(caretakerId, caregiverId);
    }
}
