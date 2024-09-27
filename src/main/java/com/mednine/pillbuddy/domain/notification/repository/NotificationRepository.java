package com.mednine.pillbuddy.domain.notification.repository;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE DATE_FORMAT(n.notificationTime, '%Y-%m-%d %H:%i') = DATE_FORMAT(:now, '%Y-%m-%d %H:%i')")
    List<Notification> findByNotificationTime(LocalDateTime now);
}
