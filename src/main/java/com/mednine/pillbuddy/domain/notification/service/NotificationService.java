package com.mednine.pillbuddy.domain.notification.service;

import com.mednine.pillbuddy.domain.notification.provider.SmsProvider;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SmsProvider smsProvider;

    public SingleMessageSentResponse sendSms(String to) {

        try {
            SingleMessageSentResponse response = smsProvider.sendSms(to);
            return response;
        } catch (Exception exception) {
            return null;
        }
    }
}
