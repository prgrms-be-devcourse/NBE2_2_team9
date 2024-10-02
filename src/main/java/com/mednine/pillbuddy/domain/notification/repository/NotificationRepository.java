package com.mednine.pillbuddy.domain.notification.repository;

import com.mednine.pillbuddy.domain.notification.dto.UserNotificationDTO;
import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.notificationTime >= :now AND n.notificationTime < :nowPlusOneMinute")
    List<Notification> findByNotificationTime(@Param("now") LocalDateTime now, @Param("nowPlusOneMinute") LocalDateTime nowPlusOneMinute);

    @Query("SELECT n FROM Notification n WHERE n.caretaker = :caretaker")
    List<UserNotificationDTO> findByCaretaker(@Param("caretaker") Caretaker caretaker);
}
