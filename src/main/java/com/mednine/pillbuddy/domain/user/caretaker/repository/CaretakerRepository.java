package com.mednine.pillbuddy.domain.user.caretaker.repository;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaretakerRepository extends JpaRepository<Caretaker, Long> {

    Optional<Caretaker> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
