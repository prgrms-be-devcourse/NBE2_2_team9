package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.medinine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.medinine.pillbuddy.domain.user.entity.Role;
import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    @Value("${oauth.kakao.oauth2-password}")
    private String oauth2Password;

    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isNewUser(String email, UserType userType) {
        return switch (userType) {
            case CAREGIVER -> caregiverRepository.existsByEmail(email);
            case CARETAKER -> caretakerRepository.existsByEmail(email);
        };
    }

    public void registerUser(OAuthProfile profile, UserType userType, String registrationId) {
        String loginId = registrationId + "_" + profile.getId();
        String encodedPassword = passwordEncoder.encode(oauth2Password);
        String username = profile.getNickname();
        String phoneNumber = profile.getPhoneNumber().replaceAll("-", "");

        switch (userType) {
            case CARETAKER -> {
                Caretaker caretaker = Caretaker.builder()
                        .username(username)
                        .loginId(loginId)
                        .email(profile.getEmail())
                        .password(encodedPassword)
                        .phoneNumber(phoneNumber)
                        .role(Role.USER)
                        .build();
                caretakerRepository.save(caretaker);
            }
            case CAREGIVER -> {
                Caregiver caregiver = Caregiver.builder()
                        .username(username)
                        .loginId(loginId)
                        .email(profile.getEmail())
                        .password(encodedPassword)
                        .phoneNumber(phoneNumber)
                        .role(Role.USER)
                        .build();
                caregiverRepository.save(caregiver);
            }
        }
    }

    public String getUserLoginID(String email, UserType userType) {
        return switch (userType) {
            case CAREGIVER -> caregiverRepository.findByEmail(email)
                    .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND))
                    .getLoginId();
            case CARETAKER -> caretakerRepository.findByEmail(email)
                    .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND))
                    .getLoginId();
        };
    }
}
