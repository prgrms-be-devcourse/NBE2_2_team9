package com.mednine.pillbuddy.domain.notification.controller;

import com.mednine.pillbuddy.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;


    /**
     * 단일 메시지 발송 예제
     */
    @PostMapping("/send-sms/{to}")
    public SingleMessageSentResponse sendOne(@PathVariable("to") String to) {

        SingleMessageSentResponse response = notificationService.sendSms(to);
        System.out.println(response);

        return response;
    }
}
