package com.mednine.pillbuddy.domain.notification.repository;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.entity.Role;
import com.mednine.pillbuddy.domain.userMedication.entity.Frequency;
import com.mednine.pillbuddy.domain.userMedication.entity.UserMedication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private CaretakerRepository caretakerRepository;

    private UserMedication userMedication;
    private Notification notification1;
    private LocalDateTime currentTime;

    @BeforeEach
    public void setUp() {
        currentTime = LocalDateTime.now();

        // Caretaker 객체 생성
        Caretaker caretaker = Caretaker.builder()
                .username("caretaker")
                .phoneNumber("01012345678")
                .loginId("caretaker_login")
                .password("caretaker_password")
                .email("caretaker@gmail.com")
                .role(Role.USER)
                .build();
        caretakerRepository.save(caretaker);

        // UserMedication 객체 초기화
        userMedication = UserMedication.builder()
                .id(1L)
                .name("타이레놀")
                .startDate(currentTime.truncatedTo(ChronoUnit.SECONDS))
                .endDate(currentTime.plusDays(1))
                .frequency(Frequency.TWICE_A_DAY)
                .caretaker(caretaker)
                .build();

        // Notification 객체 초기화
        notification1 = Notification.builder()
                .notificationTime(currentTime.truncatedTo(ChronoUnit.SECONDS))
                .userMedication(userMedication)
                .caretaker(userMedication.getCaretaker())
                .build();
        notificationRepository.save(notification1);
    }

    @Test
    @DisplayName("알림을 모두 저장할 수 있어야 한다.")
    public void saveAll() {
        // given
        Notification notification2 = Notification.builder()
                .notificationTime(userMedication.getStartDate().plusHours(12))
                .userMedication(userMedication)
                .caretaker(userMedication.getCaretaker())
                .build();
        List<Notification> notifications = List.of(notification1, notification2);

        // when
        List<Notification> savedNotifications = notificationRepository.saveAll(notifications);

        // then
        assertThat(savedNotifications).hasSize(2);
        assertThat(savedNotifications.get(0).getNotificationTime()).isEqualTo(notification1.getNotificationTime());
        assertThat(savedNotifications.get(1).getNotificationTime()).isEqualTo(notification2.getNotificationTime());
    }

    @Test
    @DisplayName("알림을 삭제할 수 있어야 한다.")
    public void delete() {
        // when
        notificationRepository.delete(notification1);
        List<Notification> notifications = notificationRepository.findAll();

        // then
        assertThat(notifications).doesNotContain(notification1);
    }

    @Test
    @DisplayName("현재 시간에 존재하는 알림을 찾을 수 있어야 한다.")
    public void findByNotificationTime() {
        // when
        List<Notification> notifications = notificationRepository.findByNotificationTime(
                currentTime.truncatedTo(ChronoUnit.SECONDS),
                currentTime.truncatedTo(ChronoUnit.SECONDS).plusMinutes(1)
        );

        // then
        assertThat(notifications).hasSize(1);
    }
}