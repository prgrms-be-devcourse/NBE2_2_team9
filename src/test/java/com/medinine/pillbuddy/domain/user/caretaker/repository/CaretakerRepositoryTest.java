package com.medinine.pillbuddy.domain.user.caretaker.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.medinine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CaretakerRepositoryTest {

    @Autowired
    private CaretakerRepository caretakerRepository;

    @Autowired
    public CaretakerCaregiverRepository caretakerCaregiverRepository;

    @Test
    @DisplayName("loginID 를 통해 사용자를 조회할 수 있다.")
    void findByLoginId() {
        // given
        String loginId = "caretaker1";

        String phoneNumber = "010-3456-7890";
        String email = "caretaker1@example.com";
        String password = "password1";

        // when
        Caretaker caretaker = caretakerRepository.findByLoginId(loginId).get();

        // then
        assertThat(caretaker.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(caretaker.getEmail()).isEqualTo(email);
        assertThat(caretaker.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("사용자의 loginID 존재 여부를 확인할 수 있다.")
    void existsByLoginId() {
        // given
        String loginId = "caretaker1";

        // when
        boolean result = caretakerRepository.existsByLoginId(loginId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자의 이메일 존재 여부를 확인할 수 있다.")
    void existsByEmail() {
        // given
        String email = "caretaker1@example.com";

        // when
        boolean result = caretakerRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자의 전화번호 존재 여부를 확인할 수 있다.")
    void existsByPhoneNumber() {
        // given
        String phoneNumber = "010-3456-7890";

        // when
        boolean result = caretakerRepository.existsByPhoneNumber(phoneNumber);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void caretakerCaregiverRepositoryTest() {
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        CaretakerCaregiver caretakerCaregiver = caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId).orElse(null);

        assertThat(caretakerCaregiver.getId()).isEqualTo(1);
        assertThat(caretakerCaregiver.getCaretaker().getId()).isEqualTo(1);
        assertThat(caregiverId).isEqualTo(1);
    }
}