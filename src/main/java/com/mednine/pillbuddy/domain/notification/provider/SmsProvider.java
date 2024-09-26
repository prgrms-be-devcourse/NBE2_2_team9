package com.mednine.pillbuddy.domain.notification.provider;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsProvider {

    private final DefaultMessageService messageService;
    public SmsProvider(@Value("${sms.api-key}") String API_KEY,
                       @Value("${sms.api-secret-key}") String API_SECRET_KEY,
                       @Value("${sms.domain}") String DOMAIN) {
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, DOMAIN);
    }

    @Value("${sms.from_number}") String FROM;

    public SingleMessageSentResponse sendSms(String to) {
        Message message = new Message();
        message.setFrom(FROM);
        message.setTo(to);
        message.setText("약을 복용할 시간입니다.");



        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        String statusCode = response.getStatusCode();
        return response;
    }
}
