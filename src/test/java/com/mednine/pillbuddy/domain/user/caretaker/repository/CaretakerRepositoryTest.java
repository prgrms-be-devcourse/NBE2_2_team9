package com.mednine.pillbuddy.domain.user.caretaker.repository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaretakerRepositoryTest {
    @Autowired
    public CaretakerCaregiverRepository caretakerCaregiverRepository;

    @Test
    public void caretakerCaregiverRepositoryTest() {
        Long caretakerId = 1L;
        Long caregiverId = 1L;

        CaretakerCaregiver caretakerCaregiver = caretakerCaregiverRepository.findByCaretaker_IdAndCaregiver_Id(caretakerId, caregiverId).orElse(null);

        Assertions.assertThat(caretakerCaregiver.getId()).isEqualTo(1);
        Assertions.assertThat(caretakerCaregiver.getCaretaker().getId()).isEqualTo(1);
        Assertions.assertThat(caregiverId).isEqualTo(1);
    }
}