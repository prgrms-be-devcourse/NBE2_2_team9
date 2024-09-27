package com.mednine.pillbuddy.domain.user.caretaker.repository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaretakerRepository extends JpaRepository<Caretaker, Long> {
    Optional<Caretaker> findById(Long id);
}
