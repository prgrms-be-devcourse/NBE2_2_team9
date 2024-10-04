package com.mednine.pillbuddy.domain.record.repository;

import com.mednine.pillbuddy.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
