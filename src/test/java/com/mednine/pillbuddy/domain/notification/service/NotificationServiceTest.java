package com.mednine.pillbuddy.domain.notification.service;

import com.mednine.pillbuddy.domain.notification.dto.NotificationDTO;
import com.mednine.pillbuddy.domain.notification.dto.UserNotificationDTO;
import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.notification.provider.SmsProvider;
import com.mednine.pillbuddy.domain.notification.repository.NotificationRepository;
import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.entity.CaretakerCaregiver;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.entity.Role;
import com.mednine.pillbuddy.domain.userMedication.entity.Frequency;
import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
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
            assertThrows(PillBuddyCustomException.class, () -> {
                notificationService.createNotifications(1L);
            });

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

            // Mocking LocalDateTime.now()
            try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
                mockedStatic.when(LocalDateTime::now).thenReturn(now);

                when(notificationRepository.findByNotificationTime(now, nowPlusOneMinute)).thenReturn(Arrays.asList(notification));
                when(caretakerCaregiverRepository.findByCaretaker(notification.getCaretaker())).thenReturn(Arrays.asList(caretakerCaregiver));

                // when
                notificationService.sendNotifications();

                // then
                verify(smsProvider, times(1)).sendNotification("01012345678", "타이레놀", "사용자이름");
                verify(smsProvider, times(1)).sendNotification("01056781234", "타이레놀", "사용자이름");
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
            PillBuddyCustomException exception = assertThrows(PillBuddyCustomException.class, () -> {
                notificationService.findNotification(caretakerId);
            });
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
            PillBuddyCustomException exception = assertThrows(PillBuddyCustomException.class, () -> {
                notificationService.findNotification(caretakerId);
            });
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }
    }