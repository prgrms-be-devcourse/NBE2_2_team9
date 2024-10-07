package com.mednine.pillbuddy.domain.notification.controller;

import com.mednine.pillbuddy.domain.notification.dto.NotificationDTO;
import com.mednine.pillbuddy.domain.notification.dto.UserNotificationDTO;
import com.mednine.pillbuddy.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{userMedicationId}")
    public ResponseEntity<List<NotificationDTO>> createNotifications(@PathVariable Long userMedicationId) {
        List<NotificationDTO> notifications = notificationService.createNotifications(userMedicationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void checkAndSendNotifications() {
        notificationService.sendNotifications();
    }

    @GetMapping("/user/{caretakerId}")
    public ResponseEntity<List<UserNotificationDTO>> findNotifications(@PathVariable Long caretakerId) {
        return ResponseEntity.ok(notificationService.findNotification(caretakerId));
    }

    @PatchMapping("/{notificationId}/{notificationTime}")
    public ResponseEntity<NotificationDTO> updateNotificationTime(
            @PathVariable Long notificationId, @PathVariable LocalDateTime notificationTime) {
        return ResponseEntity.ok(notificationService.updateNotification(notificationId, notificationTime));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);

        return ResponseEntity.ok("삭제되었습니다. Id: " + notificationId);
    }
}
