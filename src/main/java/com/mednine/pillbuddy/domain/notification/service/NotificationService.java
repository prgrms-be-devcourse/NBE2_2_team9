package com.mednine.pillbuddy.domain.notification.service;

import com.mednine.pillbuddy.domain.notification.dto.NotificationDTO;
import com.mednine.pillbuddy.domain.notification.dto.UserNotificationDTO;
import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.notification.provider.SmsProvider;
import com.mednine.pillbuddy.domain.notification.repository.NotificationRepository;
import com.mednine.pillbuddy.domain.record.RecordRepository;
import com.mednine.pillbuddy.domain.record.entity.Record;
import com.mednine.pillbuddy.domain.record.entity.Taken;
import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
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
    private final CaretakerRepository caretakerRepository;
    private final RecordRepository recordRepository;

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

    //약 복용 알림 메세지 전송
    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusOneMinute = now.plusMinutes(1);

        List<Notification> notifications = notificationRepository.findByNotificationTime(now, nowPlusOneMinute);
        if (notifications != null && !notifications.isEmpty()) {
            for (Notification notification : notifications) {
                sendNotificationToCaretaker(notification);
                sendNotificationToCaregivers(notification);
                notificationRepository.delete(notification);
            }
        } else {
            log.info("현재 시간에 등록된 알림이 없습니다.");
        }

        //약 복용 확인 메세지 전송
        checkAndSendForMissedMedications();
    }

    private void sendNotificationToCaretaker(Notification notification) {
        String phoneNumber = notification.getCaretaker().getPhoneNumber();
        String medicationName = notification.getUserMedication().getName();
        String userName = notification.getCaretaker().getUsername();
        try {
            smsProvider.sendNotification(phoneNumber, medicationName, userName);
            log.info("Caretaker에게 메세지 전송 성공");
        } catch (Exception e) {
            throw new PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED);
        }
    }

    private void sendNotificationToCaregivers(Notification notification) {
        List<CaretakerCaregiver> caretakerCaregivers = caretakerCaregiverRepository.findByCaretaker(notification.getCaretaker());
        if (caretakerCaregivers != null && !caretakerCaregivers.isEmpty()) {
            String medicationName = notification.getUserMedication().getName();
            String userName = notification.getCaretaker().getUsername();

            for (CaretakerCaregiver caretakerCaregiver : caretakerCaregivers) {
                Caregiver caregiver = caretakerCaregiver.getCaregiver();
                String caregiverPhoneNumber = caregiver.getPhoneNumber();
                try {
                    smsProvider.sendNotification(caregiverPhoneNumber, medicationName, userName);
                    log.info("Caregiver에게 메세지 전송 성공");
                } catch (Exception e) {
                    throw new PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED);
                }
            }
        }
    }

    //약 복용 확인 메세지 전송
    public void checkAndSendForMissedMedications() {
        log.info("사용자가 약을 먹었는지 확인합니다.");

        LocalDateTime now = LocalDateTime.now();
        List<UserMedication> userMedications = userMedicationRepository.findAll();

        for (UserMedication userMedication : userMedications) {
            checkRecords(userMedication, now);
        }
    }

    private void checkRecords(UserMedication userMedication, LocalDateTime now) {
        List<Notification> notifications = userMedication.getNotificationList();
        List<Record> records = recordRepository.findByUserMedication(userMedication);

        for (Notification notification : notifications) {
            LocalDateTime notificationTime = notification.getNotificationTime();
            LocalDateTime fewMinutesAfter = notificationTime.plusMinutes(15);

            // 현재 시간이 알림 시간보다 이전인 경우는 체크하지 않음
            if (now.isBefore(notificationTime)) {
                continue;
            }

            boolean isTaken = records.stream()
                    .anyMatch(record -> record.getCreatedAt().isAfter(notificationTime) && record.getTaken() == Taken.TAKEN);

            if (!isTaken && now.isAfter(fewMinutesAfter)) {
                log.info("사용자가 약을 복용하지 않은 채 15분이 지났습니다.");
                sendMissedMedicationNotification(userMedication);
            }
        }
    }

    public void sendMissedMedicationNotification(UserMedication userMedication) {
        Caretaker caretaker = userMedication.getCaretaker();
        List<CaretakerCaregiver> caretakerCaregivers = caretakerCaregiverRepository.findByCaretaker(caretaker);

        if (caretakerCaregivers != null && !caretakerCaregivers.isEmpty()) {
            String medicationName = userMedication.getName();
            String userName = caretaker.getUsername();

            caretakerCaregivers.forEach(caretakerCaregiver -> {
                Caregiver caregiver = caretakerCaregiver.getCaregiver();
                String caregiverPhoneNumber = caregiver.getPhoneNumber();
                try {
                    smsProvider.sendCheckNotification(caregiverPhoneNumber, medicationName, userName);
                    log.info("Caregiver에게 메세지 전송 성공");
                } catch (Exception e) {
                    throw new PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED);
                }
            });
        }
    }

    //알림 조회
    public List<UserNotificationDTO> findNotification(Long caretakerId) {
        Caretaker caretaker = caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND));

        List<UserNotificationDTO> userNotificationDTOS = notificationRepository.findByCaretaker(caretaker);
        if (userNotificationDTOS.isEmpty()) {
            throw new PillBuddyCustomException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }

        return userNotificationDTOS;
    }
}

