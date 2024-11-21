package com.medinine.pillbuddy.global.config;

import com.medinine.pillbuddy.PillBuddyApplication;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import feign.FeignException;
import feign.Logger;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = PillBuddyApplication.class)
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public ErrorDecoder commonFeignErrorDecoder() {
        return (methodKey, response) -> {
            if (response.status() >= 400) {
                switch (response.status()) {
                    case 401:
                        throw new PillBuddyCustomException(ErrorCode.SOCIAL_UNAUTHORIZED);
                    case 403:
                        throw new PillBuddyCustomException(ErrorCode.SOCIAL_FORBIDDEN);
                    case 419:
                        throw new PillBuddyCustomException(ErrorCode.SOCIAL_EXPIRED_TOKEN);
                    default:
                        throw new PillBuddyCustomException(ErrorCode.SOCIAL_BAD_REQUEST);
                }
            }
            return FeignException.errorStatus(methodKey, response);
        };
    }

    @Bean
    public Encoder encoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }
}
