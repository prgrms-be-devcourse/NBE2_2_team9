package com.mednine.pillbuddy.domain.user.service;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto join(JoinDto joinDto) {
        // 회원가입 유효성 검증
        if (caregiverRepository.existsByLoginId(joinDto.getLoginId()) || caretakerRepository.existsByLoginId(
                joinDto.getLoginId())) {
            throw new IllegalArgumentException("로그인 아이디가 중복되었습니다.");
        }
        if (caregiverRepository.existsByEmail(joinDto.getEmail()) || caretakerRepository.existsByEmail(
                joinDto.getEmail())) {
            throw new IllegalArgumentException("이메일이 중복되었습니다.");
        }
        if (caregiverRepository.existsByPhoneNumber(joinDto.getPhoneNumber())
                || caretakerRepository.existsByPhoneNumber(joinDto.getPhoneNumber())) {
            throw new IllegalArgumentException("휴대폰 번호가 중복되었습니다.");
        }

        // 사용자 타입에 따라 회원 저장
        if (joinDto.getUserType() == UserType.CAREGIVER) {
            Caregiver save = caregiverRepository.save(joinDto.toCaregiverEntity());
            return new UserDto(save);
        } else if (joinDto.getUserType() == UserType.CARETAKER) {
            Caretaker save = caretakerRepository.save(joinDto.toCaretakerEntity());
            return new UserDto(save);
        } else {
            throw new IllegalArgumentException("잘못된 역할입니다.");
        }
    }

}
