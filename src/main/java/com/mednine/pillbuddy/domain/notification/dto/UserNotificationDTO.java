package com.mednine.pillbuddy.domain.notification.dto;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
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
    private LocalDateTime notificationTime;

    public UserNotificationDTO(Notification notification) {
        this.notificationId = notification.getId();
        this.caretakerId = notification.getCaretaker().getId();
        this.ct_phoneNumber = notification.getCaretaker().getPhoneNumber();
        this.medicationName = notification.getUserMedication().getName();
        this.notificationTime = notification.getNotificationTime();
    }
}
