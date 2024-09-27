package com.mednine.pillbuddy.domain.user.caretaker.repository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaretakerCaregiverRepository extends JpaRepository<CaretakerCaregiver, Long> {
    List<CaretakerCaregiver> findByCaretaker(Caretaker caretaker);

}
