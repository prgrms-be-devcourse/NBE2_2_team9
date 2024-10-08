package com.medinine.pillbuddy.domain.notification.dto;

import com.medinine.pillbuddy.domain.notification.entity.Notification;
import com.medinine.pillbuddy.domain.userMedication.entity.Frequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDTO {
    private Long notificationId;
    private Long caretakerId;
    private String ct_phoneNumber;
    private String medicationName;
    private Frequency frequency;
    private LocalDateTime notificationTime;

    public UserNotificationDTO(Notification notification) {
        this.notificationId = notification.getId();
        this.caretakerId = notification.getCaretaker().getId();
        this.ct_phoneNumber = notification.getCaretaker().getPhoneNumber();
        this.medicationName = notification.getUserMedication().getName();
        this.frequency = notification.getUserMedication().getFrequency();
        this.notificationTime = notification.getNotificationTime();
    }
}
