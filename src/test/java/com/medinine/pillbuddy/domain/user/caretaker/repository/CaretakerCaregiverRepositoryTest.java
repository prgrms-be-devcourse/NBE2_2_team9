package com.medinine.pillbuddy.domain.user.caretaker.repository;

import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.medinine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.medinine.pillbuddy.domain.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CaretakerCaregiverRepositoryTest {

    @Autowired
    private CaretakerCaregiverRepository caretakerCaregiverRepository;
    @Autowired
    private CaretakerRepository caretakerRepository;

    @Autowired
    private CaregiverRepository caregiverRepository;

    private Caretaker caretaker;
    private Caregiver caregiver;

    @BeforeEach
    public void setUp() {
        caretaker = Caretaker.builder()
                .username("caretaker1")
                .loginId("caretaker_login")
                .password("caretaker_password")
                .email("caretaker@example.com")
                .phoneNumber("01012345678")
                .role(Role.USER)
                .build();

        caregiver = Caregiver.builder()
                .username("caregiver1")
                .loginId("caregiver_login")
                .password("caregiver_password")
                .email("caregiver@example.com")
                .phoneNumber("01056781234")
                .role(Role.USER)
                .build();
        caretaker = caretakerRepository.save(caretaker);
        caregiver = caregiverRepository.save(caregiver);
    }

    @Test
    @DisplayName("Caretaker에 등록된 Caregiver를 찾을 수 있어야 한다")
    public void findByCaretaker() {
        //given
        CaretakerCaregiver caretakerCaregiver = CaretakerCaregiver.builder()
                .caretaker(caretaker)
                .caregiver(caregiver)
                .build();
        caretakerCaregiverRepository.save(caretakerCaregiver);

        //when
        List<CaretakerCaregiver> result = caretakerCaregiverRepository.findByCaretaker(caretaker);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCaretaker()).isEqualTo(caretaker);
        assertThat(result.get(0).getCaregiver()).isEqualTo(caregiver);
    }
}