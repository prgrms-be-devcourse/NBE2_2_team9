package com.mednine.pillbuddy.domain.userMedication.repository;

import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMedicationRepository extends JpaRepository<UserMedication, Long> {
    @Query("SELECT um FROM UserMedication um WHERE um.caretaker.id = :caretakerId")
    List<UserMedication> findByCaretakerId(@Param("caretakerId") Long caretakerId);
}
