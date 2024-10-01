package com.mednine.pillbuddy.domain.user.profile.repository;

import com.mednine.pillbuddy.domain.user.profile.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
