package com.medinine.pillbuddy.domain.user.caregiver.repository;

import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Optional<Caregiver> findByLoginId(String loginId);
    Optional<Caregiver> findByEmail(String email);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = {"image"})
    Optional<Caregiver> findById(Long id);
}
