package com.mednine.pillbuddy.domain.user.caretaker.controller;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CaretakerControllerTest {
    @Autowired
    private CaretakerController caretakerController;

    @Test
    @Transactional
    public void addCaregiver() {
        Long caretakerId = 2L;
        Long caregiverId = 1L;

        ResponseEntity<CaretakerCaregiverDTO> caretakerCaregiver = caretakerController.addCaregiver(caretakerId, caregiverId);

        Assertions.assertThat(caretakerCaregiver).isNotNull();
        Assertions.assertThat(caretakerCaregiver.getBody()).isNotNull();
    }

    @Test
    public void caretakerServiceTests3() {
        Long caretakerId = 2L;
        Long caregiverId = 2L;

        caretakerController.deleteCaregiver(caretakerId, caregiverId);
    }
}