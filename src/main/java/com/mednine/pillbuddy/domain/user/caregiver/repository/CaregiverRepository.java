package com.mednine.pillbuddy.domain.user.caregiver.repository;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Optional<Caregiver> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
