package com.medinine.pillbuddy.domain.user.caregiver.repository;

import com.medinine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaregiverRepositoryTest {
    @Autowired
    public CaretakerCaregiverRepository caretakerCaregiverRepository;

    @Test
    public void caretakerCaregiverRepositoryTest() {
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        CaretakerCaregiver caretakerCaregiver = caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId).orElse(null);

        Assertions.assertThat(caretakerCaregiver.getId()).isEqualTo(1);
        Assertions.assertThat(caretakerCaregiver.getCaregiver().getId()).isEqualTo(1);
        Assertions.assertThat(caretakerId).isEqualTo(1);
    }
}