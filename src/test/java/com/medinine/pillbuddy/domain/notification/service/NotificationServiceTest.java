package com.medinine.pillbuddy.domain.notification.service;

import com.medinine.pillbuddy.domain.notification.dto.NotificationDTO;
import com.medinine.pillbuddy.domain.notification.dto.UserNotificationDTO;
import com.medinine.pillbuddy.domain.notification.entity.Notification;
import com.medinine.pillbuddy.domain.notification.provider.SmsProvider;
import com.medinine.pillbuddy.domain.notification.repository.NotificationRepository;
import com.medinine.pillbuddy.domain.record.repository.RecordRepository;
import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.medinine.pillbuddy.domain.user.entity.Role;
import com.medinine.pillbuddy.domain.userMedication.entity.Frequency;
import com.medinine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.medinine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserMedicationRepository userMedicationRepository;

    @Mock
    private CaretakerCaregiverRepository caretakerCaregiverRepository;

    @Mock
    private CaretakerRepository caretakerRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private SmsProvider smsProvider;

    private UserMedication userMedication;
    private Notification notification;
    private CaretakerCaregiver caretakerCaregiver;
    private Caretaker caretaker;

    @BeforeEach
    public void setUp() {
        // Caretaker 객체 초기화
        caretaker = Caretaker.builder()
                .id(1L)
                .username("caretaker")
                .phoneNumber("01012345678")
                .role(Role.USER)
                .build();

        // UserMedication 객체 초기화
        userMedication = UserMedication.builder()
                .id(1L)
                .name("타이레놀")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .frequency(Frequency.TWICE_A_DAY)
                .caretaker(caretaker)
                .build();

        // Caregiver 객체 생성
        Caregiver caregiver = Caregiver.builder()
                .username("caregiver")
                .phoneNumber("01056781234")
                .role(Role.USER)
                .build();

        // CaretakerCaregiver 객체 생성
        caretakerCaregiver = CaretakerCaregiver.builder()
                .caretaker(caretaker)
                .caregiver(caregiver)
                .build();

        // Notification 객체 초기화
        notification = Notification.builder()
                .notificationTime(userMedication.getStartDate())
                .userMedication(userMedication)
                .caretaker(userMedication.getCaretaker())
                .build();
    }

    @Nested
    @DisplayName("알림 생성 관련 테스트")
    class CreateNotificationTests {

        @Test
        @DisplayName("정상적으로 알림이 생성되어야 한다")
        public void createNotifications() {
            // given
            when(userMedicationRepository.findById(1L)).thenReturn(Optional.of(userMedication));

            Notification notification2 = Notification.builder()
                    .notificationTime(userMedication.getStartDate().plusHours(12))
                    .userMedication(userMedication)
                    .caretaker(userMedication.getCaretaker())
                    .build();

            when(notificationRepository.saveAll(anyList())).thenReturn(List.of(notification, notification2));

            // when
            List<NotificationDTO> notificationDTOs = notificationService.createNotifications(1L);

            // then
            assertNotNull(notificationDTOs);
            assertEquals(2, notificationDTOs.size());
            assertEquals("타이레놀", notificationDTOs.get(0).getMedicationName());

            verify(userMedicationRepository).findById(1L);
            verify(notificationRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("사용자 약물이 존재하지 않을 경우 예외가 발생해야 한다")
        public void createNotifications_UserMedicationNotFound() {
            // given
            when(userMedicationRepository.findById(1L)).thenReturn(Optional.empty());

            // when
            assertThrows(PillBuddyCustomException.class, () -> notificationService.createNotifications(1L));

            // then
            verify(userMedicationRepository).findById(1L);
            verify(notificationRepository, never()).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("메세지 전송 관련 테스트")
    class SendNotificationTests {

        @Test
        @DisplayName("사용자와 보호자 모두에게 성공적으로 알림을 전송한다")
        public void sendNotifications_Success() {
            // given
            LocalDateTime now = LocalDateTime.of(2024, 10, 1, 9, 44, 0);
            LocalDateTime nowPlusOneMinute = now.plusMinutes(1);

            String ct_phoneNumber = caretakerCaregiver.getCaretaker().getPhoneNumber();
            String cg_phoneNumber = caretakerCaregiver.getCaregiver().getPhoneNumber();
            String medicationName = notification.getUserMedication().getName();
            String caretakerUsername = caretakerCaregiver.getCaretaker().getUsername();

            // Mocking LocalDateTime.now()
            try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
                mockedStatic.when(LocalDateTime::now).thenReturn(now);

                when(notificationRepository.findByNotificationTime(now, nowPlusOneMinute)).thenReturn(Arrays.asList(notification));
                when(caretakerCaregiverRepository.findByCaretaker(notification.getCaretaker())).thenReturn(Arrays.asList(caretakerCaregiver));

                // when
                notificationService.sendNotifications();

                // then
                verify(smsProvider, times(1)).sendNotification(ct_phoneNumber, medicationName, caretakerUsername);
                verify(smsProvider, times(1)).sendNotification(cg_phoneNumber, medicationName, caretakerUsername);
                verify(notificationRepository, times(1)).delete(notification);
            }
        }

        @Test
        @DisplayName("전송할 알림이 없을 경우 아무것도 전송하지 않는다")
        public void sendNotifications_NoNotifications() {
            // given
            LocalDateTime now = LocalDateTime.of(2024, 10, 1, 9, 44, 0);
            LocalDateTime nowPlusOneMinute = now.plusMinutes(1);

            // Mocking LocalDateTime.now()
            try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
                mockedStatic.when(LocalDateTime::now).thenReturn(now);

                when(notificationRepository.findByNotificationTime(now, nowPlusOneMinute)).thenReturn(Collections.emptyList());

                // when
                notificationService.sendNotifications();

                // then
                verify(smsProvider, never()).sendNotification(any(), any(), any());
                verify(notificationRepository, never()).delete(any());
            }
        }

        @Test
        @DisplayName("SMS 전송 실패 시 예외를 발생시킨다")
        public void sendNotifications_MessageSendFailed() {
            // given
            LocalDateTime now = LocalDateTime.of(2024, 10, 1, 9, 44, 0);
            LocalDateTime nowPlusOneMinute = now.plusMinutes(1);

            // Mocking LocalDateTime.now()
            try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
                mockedStatic.when(LocalDateTime::now).thenReturn(now);

                when(notificationRepository.findByNotificationTime(now, nowPlusOneMinute)).thenReturn(Arrays.asList(notification));

                doThrow(new PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED))
                        .when(smsProvider).sendNotification(any(), any(), any());

                // when & then
                PillBuddyCustomException exception = assertThrows(PillBuddyCustomException.class, () -> notificationService.sendNotifications());
                assertEquals(ErrorCode.MESSAGE_SEND_FAILED, exception.getErrorCode());
                verify(notificationRepository, never()).delete(notification);
            }
        }

        @Test
        @DisplayName("사용자가 약을 복용하지 않은 채 15분이 지난 경우, 보호자에게 약 복용 확인 메세지를 전송한다")
        void checkAndSendForMissedMedications_SendsNotification() {
            // given
            UserMedication testUserMedication = UserMedication.builder()
                    .id(5L)
                    .name("비타민 D")
                    .startDate(LocalDateTime.now().minusMinutes(15))
                    .endDate(LocalDateTime.now().plusDays(1))
                    .frequency(Frequency.TWICE_A_DAY)
                    .caretaker(caretaker)
                    .build();

            Notification testNotification = Notification.builder()
                    .notificationTime(LocalDateTime.now().minusMinutes(15))
                    .userMedication(null)
                    .caretaker(userMedication.getCaretaker())
                    .build();
            testNotification.changeUserMedication(testUserMedication);


            when(userMedicationRepository.findAll()).thenReturn(Collections.singletonList(testUserMedication));
            when(recordRepository.findByUserMedication(testUserMedication)).thenReturn(Collections.emptyList());
            when(caretakerCaregiverRepository.findByCaretaker(caretaker)).thenReturn(Collections.singletonList(caretakerCaregiver));

            // when
            notificationService.checkAndSendForMissedMedications();

            // then
            verify(smsProvider, times(1)).sendCheckNotification(
                    "01056781234",
                    "비타민 D",
                    "caretaker"
            );
        }

        @Test
        @DisplayName("약 복용 확인 메세지 전송 시, 보호자가 없는 경우 메세지가 전송되지 않는다.")
        void sendMissedMedicationNotification_NoCaregivers() {
            // Given
            when(caretakerCaregiverRepository.findByCaretaker(caretaker)).thenReturn(Collections.emptyList());

            // When
            notificationService.sendMissedMedicationNotification(userMedication);

            // Then
            verify(smsProvider, never()).sendCheckNotification(anyString(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("알림 조회 관련 테스트")
    class FindNotificationTests {
        @Test
        @DisplayName("Caretaker ID로 알림을 조회할 수 있어야 한다.")
        public void findNotification() {
            // given
            Long caretakerId = caretaker.getId();

            // Mocking
            when(caretakerRepository.findById(caretakerId)).thenReturn(Optional.of(caretaker));
            when(notificationRepository.findByCaretaker(caretaker)).thenReturn(List.of(new UserNotificationDTO(notification)));

            // when
            List<UserNotificationDTO> notifications = notificationService.findNotification(caretakerId);

            // then
            assertThat(notifications).isNotEmpty();
            assertThat(notifications).hasSize(1);
            assertThat(notifications.get(0).getNotificationTime()).isEqualTo(notification.getNotificationTime());
        }

        @Test
        @DisplayName("존재하지 않는 Caretaker ID로 알림을 조회할 경우 예외가 발생해야 한다.")
        public void findNotification_CaretakerNotFound() {
            // given
            Long caretakerId = 100L;

            // Mocking
            when(caretakerRepository.findById(caretakerId)).thenReturn(Optional.empty());

            // when & then
            PillBuddyCustomException exception = assertThrows(PillBuddyCustomException.class, () -> notificationService.findNotification(caretakerId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CARETAKER_NOT_FOUND);
        }

        @Test
        @DisplayName("Caretaker에 대한 알림이 없을 경우 예외가 발생해야 한다.")
        public void findNotification_NoNotificationsFound() {
            // given
            Long caretakerId = caretaker.getId();

            // Mocking
            when(caretakerRepository.findById(caretakerId)).thenReturn(Optional.of(caretaker));
            when(notificationRepository.findByCaretaker(caretaker)).thenReturn(new ArrayList<>());

            // when & then
            PillBuddyCustomException exception = assertThrows(PillBuddyCustomException.class, () -> notificationService.findNotification(caretakerId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("알림 시간 수정 관련 테스트")
    class ChangeNotificationTests {
        @Test
        @DisplayName("알림 시간을 수정할 수 있어야 한다.")
        void updateNotification_ValidId_UpdatesNotificationTime() {
            // given
            Long notificationId = 1L;
            LocalDateTime newNotificationTime = LocalDateTime.now().plusHours(1);

            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

            // when
            NotificationDTO result = notificationService.updateNotification(notificationId, newNotificationTime);

            // then
            assertNotNull(result);
            assertEquals(newNotificationTime, notification.getNotificationTime());
            verify(notificationRepository).findById(notificationId);
        }

        @Test
        @DisplayName("해당하는 알림이 없으면 예외가 발생해야 한다.")
        void updateNotification_InvalidId_ThrowsException() {
            // given
            Long notificationId = 1L;
            LocalDateTime newNotificationTime = LocalDateTime.now().plusHours(1);

            when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

            // when & then
            assertThrows(PillBuddyCustomException.class, () -> notificationService.updateNotification(notificationId, newNotificationTime));
            verify(notificationRepository).findById(notificationId);
        }
    }

    @Nested
    @DisplayName("알림 삭제 관련 테스트")
    class DeleteNotificationTests {
        @Test
        @DisplayName("알림을 삭제할 수 있어야 한다.")
        void deleteNotification_Success() {
            // given
            when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));

            // when
            notificationService.deleteNotification(notification.getId());

            // then
            verify(notificationRepository, times(1)).delete(notification);
        }

        @Test
        @DisplayName("해당하는 알림이 없으면 예외가 발생해야 한다.")
        void deleteNotification_NotificationNotFound() {
            // Given
            when(notificationRepository.findById(notification.getId())).thenReturn(Optional.empty());

            // When & Then
            PillBuddyCustomException exception = assertThrows(PillBuddyCustomException.class, () -> notificationService.deleteNotification(notification.getId()));

            assertEquals(ErrorCode.NOTIFICATION_NOT_FOUND, exception.getErrorCode());
            verify(notificationRepository, never()).delete(any());
        }
    }
}