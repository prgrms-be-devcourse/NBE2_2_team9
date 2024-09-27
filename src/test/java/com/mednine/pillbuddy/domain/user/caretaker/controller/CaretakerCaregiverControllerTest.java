package com.mednine.pillbuddy.domain.user.caretaker.controller;

import com.mednine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CaretakerCaregiverControllerTest {

    @Autowired
    private CaretakerCaregiverController caretakerCaregiverController;

    @Test
    @Transactional
    public void createCaretakerCaregiver() {
        Long caretakerId = 2L;
        Long caregiverId = 1L;

        ResponseEntity<CaretakerCaregiverDTO> caretakerCaregiver = caretakerCaregiverController.createCaretakerCaregiver(caretakerId, caregiverId);

        Assertions.assertThat(caretakerCaregiver).isNotNull();
        Assertions.assertThat(caretakerCaregiver.getBody()).isNotNull();
    }
}