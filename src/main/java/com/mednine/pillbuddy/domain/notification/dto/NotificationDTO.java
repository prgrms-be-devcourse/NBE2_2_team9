package com.mednine.pillbuddy.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationDTO {
    private Long notificationId;
    private LocalDateTime notificationTime;
    private String medicationName;
    private Long caretakerId;


    public NotificationDTO(Long notificationId, LocalDateTime notificationTime, String medicationName, Long caretakerId) {
        this.notificationId = notificationId;
        this.notificationTime = notificationTime;
        this.medicationName = medicationName;
        this.caretakerId = caretakerId;
    }
}
