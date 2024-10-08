package com.medinine.pillbuddy.domain.user.profile.repository;

import com.medinine.pillbuddy.domain.user.profile.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
