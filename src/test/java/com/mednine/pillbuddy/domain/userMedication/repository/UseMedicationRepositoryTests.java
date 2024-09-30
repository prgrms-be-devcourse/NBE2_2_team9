package com.mednine.pillbuddy.domain.userMedication.repository;

import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
