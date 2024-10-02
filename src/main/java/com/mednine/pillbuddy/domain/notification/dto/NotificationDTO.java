package com.mednine.pillbuddy.domain.notification.dto;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.userMedication.entity.Frequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class NotificationDTO {
    private Long notificationId;
    private String medicationName;
    private Frequency frequency;
    private LocalDateTime notificationTime;
    private Long caretakerId;

    public static List<NotificationDTO> convertToDTOs(List<Notification> notifications) {
        return notifications.stream()
                .map(notification -> NotificationDTO.builder()
                        .notificationId(notification.getId())
                        .notificationTime(notification.getNotificationTime())
                        .medicationName(notification.getUserMedication().getName())
                        .frequency(notification.getUserMedication().getFrequency())
                        .caretakerId(notification.getCaretaker().getId()).build())
                .collect(Collectors.toList());
    }
}
