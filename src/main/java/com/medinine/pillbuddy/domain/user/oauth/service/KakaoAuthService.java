package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.medinine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.medinine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.entity.Role;
import com.medinine.pillbuddy.domain.user.oauth.dto.KakaoProfile;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import com.medinine.pillbuddy.global.jwt.JwtToken;
import com.medinine.pillbuddy.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService implements OAuthService {

    @Value("${oauth.kakao.oauth2-password}")
    private String oauth2Password;

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoClient kakaoClient;
    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String getConnectionUrl(UserType userType) {
        return kakaoClient.getConnectionUrl(userType);
    }

    @Override
    public JwtToken login(String code, UserType userType) {
        // code 를 통해 카카오 서버로 Token 요청 (POST 요청)
        String accessToken = kakaoClient.getAccessToken(code);

        // Token 을 통해 카카오 서버로 사용자 데이터 요청 (POST 요청)
        KakaoProfile profile = kakaoClient.getUserInfo(accessToken);
        String email = profile.getEmail();

        // 존재하지 않는 회원일 경우 회원 가입
        if (!isEmailExists(email)) {
            userAppend(profile, userType);
        }

        // caregiver or caretaker 조회 후, Jwt 토큰 반환
        if (userType == UserType.CAREGIVER) {
            Caregiver caregiver = caregiverRepository.findByEmail(email).orElseThrow(() -> new PillBuddyCustomException(
                    ErrorCode.USER_NOT_FOUND));
            return jwtTokenProvider.generateToken(caregiver.getLoginId());
        } else {
            Caretaker caretaker = caretakerRepository.findByEmail(email).orElseThrow(() -> new PillBuddyCustomException(
                    ErrorCode.USER_NOT_FOUND));
            return jwtTokenProvider.generateToken(caretaker.getLoginId());
        }
    }

    private void userAppend(KakaoProfile profile, UserType userType) {
        String loginId = "kakao_" + profile.getId();
        String encodedPassword = passwordEncoder.encode(oauth2Password);
        String username = profile.getProperties().get("nickname");

        switch (userType) {
            case CARETAKER -> {
                Caretaker caretaker = Caretaker.builder()
                        .username(username)
                        .loginId(loginId)
                        .email(profile.getEmail())
                        .password(encodedPassword)
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
                        .role(Role.USER)
                        .build();
                caregiverRepository.save(caregiver);
            }
        }
    }

    private boolean isEmailExists(String email) {
        return caretakerRepository.existsByEmail(email) || caregiverRepository.existsByEmail(email);
    }
}
