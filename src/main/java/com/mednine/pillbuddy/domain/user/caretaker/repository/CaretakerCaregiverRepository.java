package com.mednine.pillbuddy.domain.user.caretaker.repository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaretakerCaregiverRepository extends JpaRepository<CaretakerCaregiver, Long> {
    Optional<CaretakerCaregiver> findByCaretaker_IdAndCaregiver_Id(Long caretakerId, Long caregiverId);
}
