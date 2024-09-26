package com.mednine.pillbuddy.domain.user.caregiver.repository;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    Optional<Caregiver> findById(Long id);
}