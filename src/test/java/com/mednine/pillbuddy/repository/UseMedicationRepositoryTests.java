package com.mednine.pillbuddy.repository;

import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UseMedicationRepositoryTests {

    @Autowired
    public UserMedicationRepository userMedicationRepository;

    @Test
    public void userMedicationRetrieve() {
        Long caretakerId = 1L;
        List<UserMedication> byCaretakerId = userMedicationRepository.findByCaretakerId(caretakerId);
        Assertions.assertEquals(1, byCaretakerId.size());
    }
}
