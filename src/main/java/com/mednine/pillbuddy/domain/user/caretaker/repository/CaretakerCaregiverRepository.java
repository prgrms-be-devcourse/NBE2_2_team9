package com.mednine.pillbuddy.domain.user.caretaker.repository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaretakerCaregiverRepository extends JpaRepository<CaretakerCaregiver, Long> {
    Optional<CaretakerCaregiver> findByCaretaker_IdAndCaregiver_Id(Long caretakerId, Long caregiverId);
}
