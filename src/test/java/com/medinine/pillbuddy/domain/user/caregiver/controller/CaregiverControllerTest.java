package com.medinine.pillbuddy.domain.user.caregiver.controller;

import com.medinine.pillbuddy.domain.user.caretaker.dto.CaretakerCaregiverDTO;
import com.medinine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class CaregiverControllerTest {
    @Autowired
    private CaregiverController caregiverController;

    @Test
    @Transactional
    public void getCaretakerMedications() {
        Long caregiverId = 1L;
        Long caretakerId = 1L;

        ResponseEntity<List<UserMedicationDTO>> userMedication = caregiverController.getCaretakerMedications(caregiverId, caretakerId);

        Assertions.assertThat(userMedication).isNotNull();
        Assertions.assertThat(userMedication.getBody()).isNotNull();
    }

    @Test
    @Transactional
    public void addCaretaker() {
        Long caregiverId = 2L;
        Long caretakerId = 1L;

        ResponseEntity<CaretakerCaregiverDTO> caretakerCaregiver = caregiverController.addCaretaker(caregiverId, caretakerId);

        Assertions.assertThat(caretakerCaregiver).isNotNull();
        Assertions.assertThat(caretakerCaregiver.getBody()).isNotNull();
    }

    @Test
    public void caregiverServiceTests3() {
        Long caregiverId = 2L;
        Long caretakerId = 2L;

        caregiverController.deleteCaretaker(caregiverId, caretakerId);
    }
}