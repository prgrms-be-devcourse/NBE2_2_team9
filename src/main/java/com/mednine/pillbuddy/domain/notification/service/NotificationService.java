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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserMedicationRepository userMedicationRepository;
    private final CaretakerCaregiverRepository caretakerCaregiverRepository;
    private final SmsProvider smsProvider;

    //알림 생성
    public List<NotificationDTO> createNotifications(Long userMedicationId) {
        UserMedication userMedication = userMedicationRepository.findById(userMedicationId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND));

        List<Notification> notifications = generateNotifications(userMedication);
        return NotificationDTO.convertToDTOs(notifications);
    }

    public List<Notification> generateNotifications(UserMedication userMedication) {
        LocalDateTime currentTime = userMedication.getStartDate();
        List<Notification> notifications = new ArrayList<>();
        while (currentTime.isBefore(userMedication.getEndDate())) {
            Notification notification = Notification.builder()
                    .notificationTime(currentTime)
                    .userMedication(userMedication)
                    .caretaker(userMedication.getCaretaker())
                    .build();
            notifications.add(notification);
            currentTime = incrementTime(currentTime, userMedication.getFrequency());
        }
        return notificationRepository.saveAll(notifications);
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

    //메세지 전송
    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findByNotificationTime(now);

        if (notifications != null && !notifications.isEmpty()) {
            for (Notification notification : notifications) {
                sendNotificationToCaretaker(notification);
                sendNotificationToCaregivers(notification);
                notificationRepository.delete(notification);
            }
        }
    }

    private void sendNotificationToCaretaker(Notification notification) {
        String phoneNumber = notification.getCaretaker().getPhoneNumber();
        String medicationName = notification.getUserMedication().getName();
        try {
            smsProvider.sendNotification(phoneNumber, medicationName);
            log.info("Caretaker에게 메세지 전송 성공");
        } catch (Exception e) {
            throw new PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED);
        }
    }

    private void sendNotificationToCaregivers(Notification notification) {
        List<CaretakerCaregiver> caretakerCaregivers = caretakerCaregiverRepository.findByCaretaker(notification.getCaretaker());
        String medicationName = notification.getUserMedication().getName();

        for (CaretakerCaregiver caretakerCaregiver : caretakerCaregivers) {
            Caregiver caregiver = caretakerCaregiver.getCaregiver();
            String caregiverPhoneNumber = caregiver.getPhoneNumber();
            try {
                smsProvider.sendNotification(caregiverPhoneNumber, medicationName);
                log.info("Caregiver에게 메세지 전송 성공");
            } catch (Exception e) {
                throw new PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED);
            }
        }
    }
}
