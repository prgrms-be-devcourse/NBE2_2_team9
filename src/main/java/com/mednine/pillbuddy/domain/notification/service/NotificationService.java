package com.mednine.pillbuddy.domain.notification.service;

import com.mednine.pillbuddy.domain.notification.dto.NotificationDTO;
import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.notification.provider.SmsProvider;
import com.mednine.pillbuddy.domain.notification.repository.NotificationRepository;
import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.userMedication.entity.Frequency;
import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserMedicationRepository userMedicationRepository;
    private final CaretakerCaregiverRepository caretakerCaregiverRepository;
    private final SmsProvider smsProvider;

    public  List<NotificationDTO> createNotificationsForUserMedication(Long userMedicationId) {
        UserMedication userMedication = userMedicationRepository.findById(userMedicationId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND));
        LocalDateTime currentTime = userMedication.getStartDate();

        List<Notification> notifications = new ArrayList<>();
        while (currentTime.isBefore(userMedication.getEndDate())) {
            Notification notification = Notification.builder()
                    .notificationTime(currentTime)
                    .userMedication(userMedication)
                    .caretaker(userMedication.getCaretaker())
                    .build();
            Notification saved = notificationRepository.save(notification);
            notifications.add(saved);
            currentTime = incrementTime(currentTime, userMedication.getFrequency());
        }
        return notifications.stream().map(notification -> new NotificationDTO(
                notification.getId(),
                notification.getNotificationTime(),
                notification.getUserMedication().getName(),
                notification.getCaretaker().getId()
        )).collect(Collectors.toList());
    }

    private LocalDateTime incrementTime(LocalDateTime time, Frequency frequency) {
        return switch (frequency) {
            case ONCE_A_DAY -> time.plusDays(1);
            case TWICE_A_DAY -> time.plusHours(12);
            case THREE_TIMES_A_DAY -> time.plusHours(8);
            case WEEKLY -> time.plusWeeks(1);
            case BIWEEKLY -> time.plusWeeks(2);
            case MONTHLY -> time.plusMonths(1);
        };
    }

    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findByNotificationTime(now);

        for (Notification notification : notifications) {
            String phoneNumber = notification.getCaretaker().getPhoneNumber();
            String medicationName = notification.getUserMedication().getName();
            smsProvider.sendNotification(phoneNumber, medicationName);

            List<CaretakerCaregiver> caretakerCaregivers = caretakerCaregiverRepository.findByCaretaker(notification.getCaretaker());
            for (CaretakerCaregiver caretakerCaregiver : caretakerCaregivers) {
                Caregiver caregiver = caretakerCaregiver.getCaregiver();
                String caregiverPhoneNumber = caregiver.getPhoneNumber();
                smsProvider.sendNotification(caregiverPhoneNumber, medicationName);
            }
            notificationRepository.delete(notification);
        }
    }
}
