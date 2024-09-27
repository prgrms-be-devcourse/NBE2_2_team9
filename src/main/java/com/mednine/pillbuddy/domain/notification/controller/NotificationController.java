package com.mednine.pillbuddy.domain.notification.controller;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{userMedicationId}")
    public ResponseEntity<List<Notification>> createNotifications(@PathVariable Long userMedicationId) {
        List<Notification> notifications = notificationService.createNotificationsForUserMedication(userMedicationId);
        return ResponseEntity.ok(notifications);
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void checkAndSendNotifications() {
        notificationService.sendNotifications();
    }
}
