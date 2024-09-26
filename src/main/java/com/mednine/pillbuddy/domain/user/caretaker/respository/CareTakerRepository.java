package com.mednine.pillbuddy.domain.user.caretaker.respository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareTakerRepository extends JpaRepository<Caretaker, Long> {
}
