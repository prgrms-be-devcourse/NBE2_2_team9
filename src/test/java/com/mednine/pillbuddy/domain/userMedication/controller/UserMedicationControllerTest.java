package com.mednine.pillbuddy.domain.userMedication.controller;

import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.mednine.pillbuddy.domain.userMedication.entity.Frequency;
import com.mednine.pillbuddy.domain.userMedication.entity.MedicationType;
import com.mednine.pillbuddy.domain.userMedication.service.UserMedicationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class UserMedicationControllerTest {

    @Autowired
    private UserMedicationService userMedicationService;

    @Test
    @Transactional
    public void createUserMedication() {
        Long caretakerId = 2L;
        UserMedicationDTO userMedicationDTO = UserMedicationDTO.builder()
                .name("감기약")
                .description("잘 듣는 감기약")
                .dosage(10)
                .frequency(Frequency.TWICE_A_DAY)
                .type(MedicationType.SUPPLEMENT)
                .stock(5)
                .expirationDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now()).build();

        UserMedicationDTO register = userMedicationService.register(caretakerId, userMedicationDTO);
        Assertions.assertThat(register.getName()).isEqualTo("감기약");
        Assertions.assertThat(register.getDescription()).isEqualTo("잘 듣는 감기약");
        Assertions.assertThat(register.getDosage()).isEqualTo(10);
        Assertions.assertThat(register.getFrequency()).isEqualTo(Frequency.TWICE_A_DAY);
        Assertions.assertThat(register.getType()).isEqualTo(MedicationType.SUPPLEMENT);
        Assertions.assertThat(register.getStock()).isEqualTo(5);
    }

    @Test
    public void getUserMedication() {
        Long caretakerId = 2L;
        List<UserMedicationDTO> retrieve = userMedicationService.retrieve(caretakerId);
        Assertions.assertThat(retrieve.size()).isEqualTo(1);
        Assertions.assertThat(retrieve.get(0).getId()).isEqualTo(2L);
    }

    @Test
    @Transactional
    public void updateUserMedication() {
        Long caretakerId = 2L;
        Long medicationId = 2L;
        UserMedicationDTO userMedicationDTO = UserMedicationDTO.builder()
                .name("감기약")
                .description("잘 듣는 감기약")
                .dosage(10)
                .frequency(Frequency.TWICE_A_DAY)
                .type(MedicationType.SUPPLEMENT)
                .stock(5)
                .expirationDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now()).build();

        UserMedicationDTO modify = userMedicationService.modify(caretakerId, medicationId, userMedicationDTO);
        Assertions.assertThat(modify.getName()).isEqualTo("감기약");
        Assertions.assertThat(modify.getDescription()).isEqualTo("잘 듣는 감기약");
        Assertions.assertThat(modify.getDosage()).isEqualTo(10);
    }
}