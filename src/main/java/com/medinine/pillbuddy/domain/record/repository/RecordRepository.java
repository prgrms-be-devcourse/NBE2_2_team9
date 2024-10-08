package com.medinine.pillbuddy.domain.record.repository;

import com.medinine.pillbuddy.domain.record.entity.Record;
import com.medinine.pillbuddy.domain.userMedication.entity.UserMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.userMedication = :userMedication")
    List<Record> findByUserMedication(UserMedication userMedication);
}
