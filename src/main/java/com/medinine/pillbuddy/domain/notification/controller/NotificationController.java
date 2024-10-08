package com.medinine.pillbuddy.domain.notification.controller;

import com.medinine.pillbuddy.domain.notification.dto.NotificationDTO;
import com.medinine.pillbuddy.domain.notification.dto.UserNotificationDTO;
import com.medinine.pillbuddy.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@Tag(name = "알림 기능", description = "사용자는 알림 기능을 등록,조회,삭제,수정할 수 있다.")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(description = "사용자는 등록한 약 정보를 통해 알림을 생성할 수 있다.")
    @PostMapping("/{userMedicationId}")
    public ResponseEntity<List<NotificationDTO>> createNotifications(@PathVariable Long userMedicationId) {
        List<NotificationDTO> notifications = notificationService.createNotifications(userMedicationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void checkAndSendNotifications() {
        notificationService.sendNotifications();
    }

    @Operation(description = "사용자는 설정된 알림 정보를 조회할 수 있다.")
    @GetMapping("/user/{caretakerId}")
    public ResponseEntity<List<UserNotificationDTO>> findNotifications(@PathVariable Long caretakerId) {

        return ResponseEntity.ok(notificationService.findNotification(caretakerId));
    }

    @Operation(description = "사용자는 설정된 알림 시간을 수정할 수 있다.")
    @PatchMapping("/{notificationId}/{notificationTime}")
    public ResponseEntity<NotificationDTO> updateNotificationTime(
            @PathVariable Long notificationId, @PathVariable LocalDateTime notificationTime) {

        return ResponseEntity.ok(notificationService.updateNotification(notificationId, notificationTime));
    }

    @Operation(description = "사용자는 설정된 알림 정보를 삭제할 수 있다.")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);

        return ResponseEntity.ok("삭제되었습니다. Id: " + notificationId);
    }
}
