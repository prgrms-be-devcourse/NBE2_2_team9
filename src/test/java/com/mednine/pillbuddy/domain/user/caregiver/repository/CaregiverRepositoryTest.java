package com.mednine.pillbuddy.domain.user.caregiver.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CaregiverRepositoryTest {

    @Autowired
    private CaregiverRepository caregiverRepository;

    @Test
    @DisplayName("loginID 를 통해 보호자를 조회할 수 있다.")
    void findByLoginId() {
        // given
        String loginId = "caregiver1";

        String phoneNumber = "010-1234-5678";
        String email = "caregiver1@example.com";
        String password = "password1";

        // when
        Caregiver caregiver = caregiverRepository.findByLoginId(loginId).get();

        // then
        assertThat(caregiver.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(caregiver.getEmail()).isEqualTo(email);
        assertThat(caregiver.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("보호자의 loginID 존재 여부를 확인할 수 있다.")
    void existsByLoginId() {
        // given
        String loginId = "caregiver1";

        // when
        boolean result = caregiverRepository.existsByLoginId(loginId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("보호자의 이메일 존재 여부를 확인할 수 있다.")
    void existsByEmail() {
        // given
        String email = "caregiver1@example.com";

        // when
        boolean result = caregiverRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("보호자의 전화번호 존재 여부를 확인할 수 있다.")
    void existsByPhoneNumber() {
        // given
        String phoneNumber = "010-1234-5678";

        // when
        boolean result = caregiverRepository.existsByPhoneNumber(phoneNumber);

        // then
        assertThat(result).isTrue();
    }
}