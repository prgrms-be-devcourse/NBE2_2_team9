package com.mednine.pillbuddy.domain.notification.provider;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SmsProvider {

    private final DefaultMessageService messageService;

    public SmsProvider(@Value("${sms.api-key}") String API_KEY,
                       @Value("${sms.api-secret-key}") String API_SECRET_KEY,
                       @Value("${sms.domain}") String DOMAIN) {
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, DOMAIN);
    }

    @Value("${sms.from-number}") String FROM;

    public void sendNotification(String phoneNumber, String medicationName) {
        Message message = new Message();
        message.setFrom(FROM);
        message.setTo(phoneNumber);
        message.setText("약 복용 시간입니다: " + medicationName);

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
