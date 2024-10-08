package com.medinine.pillbuddy.domain.user.caregiver.service;

import com.medinine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.medinine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class CaregiverServiceTest {

    @Autowired
    private CaregiverService caregiverService;

    @Test
    @Transactional
    public void getCaretakerMedicationsTests1() {   //정상적으로 불러지는지 확인
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        List<UserMedicationDTO> medications = caregiverService.getCaretakerMedications(caregiverId, caretakerId);

        Assertions.assertThat(medications.get(0).getName()).isEqualTo("Aspirin");
    }

    @Test
    @Transactional
    public void getCaretakerMedicationsTests2() {   //예외 - 등록되지 않은 사용자 검색했을 때
        Long caregiverId = 1L;
        Long caretakerId = 99L;

        assertThatThrownBy(() -> caregiverService.getCaretakerMedications(caregiverId, caretakerId))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("사용자 정보가 일치하지 않습니다");
    }

    @Test
    @Transactional
    public void caregiverServiceTests1() {
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
