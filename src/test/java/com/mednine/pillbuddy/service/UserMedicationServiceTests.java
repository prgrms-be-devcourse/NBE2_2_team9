package com.mednine.pillbuddy.service;

import com.mednine.pillbuddy.domain.userMedication.dto.UserMedicationDTO;
import com.mednine.pillbuddy.domain.userMedication.entity.Frequency;
import com.mednine.pillbuddy.domain.userMedication.entity.MedicationType;
import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.mednine.pillbuddy.domain.userMedication.service.UserMedicationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class UserMedicationServiceTests {

    @Autowired
    private UserMedicationRepository userMedicationRepository;

    @Autowired
    private UserMedicationService userMedicationService;

    @Test
    @Transactional
    public void userMedicationUpdate() {
        Long caretakerId = 1L;
        Long userMedicationId = 1L;
        List<UserMedication> byCaretakerId = userMedicationRepository.findByCaretakerId(caretakerId);

        if (byCaretakerId.get(0).getId().equals(userMedicationId)) {
            UserMedication userMedication = byCaretakerId.get(0);
            userMedication.changeName("aspirin");
            userMedication.changeDosage(100);
            userMedication.changeDescription("loose pain");
        }

        Assertions.assertThat(userMedicationRepository.findById(userMedicationId).get().getName()).isEqualTo("aspirin");
        Assertions.assertThat(userMedicationRepository.findById(userMedicationId).get().getDosage()).isEqualTo(100);
        Assertions.assertThat(userMedicationRepository.findById(userMedicationId).get().getDescription()).isEqualTo("loose pain");
    }

    @Test
    @Transactional
    public void userMedicationRetrieve() {
        Long caretakerId = 1L;

        List<UserMedication> byCaretakerId = userMedicationRepository.findByCaretakerId(caretakerId);

        if (byCaretakerId.isEmpty()) {
            return;
        }

        Assertions.assertThat(byCaretakerId.get(0).getName()).isEqualTo("Aspirin");
        Assertions.assertThat(byCaretakerId.get(0).getDosage()).isEqualTo(2);
        Assertions.assertThat(byCaretakerId.get(0).getDescription()).isEqualTo("Pain reliever");
        Assertions.assertThat(byCaretakerId.get(0).getCaretaker().getId()).isEqualTo(caretakerId);
    }

    @Test
    @Transactional
    public void userMedicationRegister() {
        Long caretakerId = 2L;
        UserMedicationDTO userMedicationDTO = UserMedicationDTO.builder()
                .name("텐텐")
                .description("맛있는 영양제")
                .dosage(3)
                .frequency(Frequency.TWICE_A_DAY)
                .type(MedicationType.SUPPLEMENT)
                .stock(10)
                .expirationDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now()).build();

        UserMedicationDTO register = userMedicationService.register(caretakerId, userMedicationDTO);
        Assertions.assertThat(register.getName()).isEqualTo("텐텐");
        Assertions.assertThat(register.getDescription()).isEqualTo("맛있는 영양제");
        Assertions.assertThat(register.getDosage()).isEqualTo(3);
        Assertions.assertThat(register.getFrequency()).isEqualTo(Frequency.TWICE_A_DAY);
        Assertions.assertThat(register.getType()).isEqualTo(MedicationType.SUPPLEMENT);
        Assertions.assertThat(register.getStock()).isEqualTo(10);
    }
}
