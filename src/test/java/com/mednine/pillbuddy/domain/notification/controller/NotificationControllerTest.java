package com.mednine.pillbuddy.domain.notification.controller;

import com.mednine.pillbuddy.domain.notification.provider.SmsProvider;
import com.mednine.pillbuddy.domain.notification.repository.NotificationRepository;
import com.mednine.pillbuddy.domain.notification.service.NotificationService;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerCaregiverRepository;
import com.mednine.pillbuddy.domain.userMedication.repository.UserMedicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private UserMedicationRepository userMedicationRepository;

    @MockBean
    private CaretakerCaregiverRepository caretakerCaregiverRepository;

    @MockBean
    private SmsProvider smsProvider;

    private static final String BASE_URL = "/api/notification";

    @Test
    @DisplayName("알림 생성 테스트")
    void createNotifications_test() throws Exception {
        // given
        Long userMedicationId = 1L;

        // when & then
        mvc.perform(post(BASE_URL + "/" + userMedicationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}