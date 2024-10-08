package com.medinine.pillbuddy.domain.user.caretaker.repository;

import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaretakerCaregiverRepository extends JpaRepository<CaretakerCaregiver, Long> {
    List<CaretakerCaregiver> findByCaretaker(Caretaker caretaker);
    Optional<CaretakerCaregiver> findByCaretakerIdAndCaregiverId(Long caretakerId, Long caregiverId);
}
