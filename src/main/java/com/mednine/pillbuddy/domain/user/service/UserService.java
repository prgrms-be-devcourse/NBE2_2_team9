package com.mednine.pillbuddy.domain.user.service;

import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import com.mednine.pillbuddy.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto join(JoinDto joinDto) {
        validateJoinInfo(joinDto);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(joinDto.getPassword());
        joinDto.changeEncodedPassword(encodedPassword);

        // 사용자 타입에 따라 회원 저장
        return switch (joinDto.getUserType()) {
            case CAREGIVER -> new UserDto(caregiverRepository.save(joinDto.toCaregiverEntity()));
            case CARETAKER -> new UserDto(caretakerRepository.save(joinDto.toCaretakerEntity()));
        };
    }

    @Transactional(readOnly = true)
    public JwtToken login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        // 사용자 유효성 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional(propagation = Propagation.NEVER)
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new PillBuddyCustomException(ErrorCode.USER_AUTHENTICATION_REQUIRED);
        }
        SecurityContextHolder.clearContext();
    }

    @Transactional(readOnly = true)
    public JwtToken reissueToken(String bearerToken) {
        // 토큰 가져오기
        String refreshToken = jwtTokenProvider.resolveToken(bearerToken);

        // 토큰 유효성 검사
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new PillBuddyCustomException(ErrorCode.JWT_TOKEN_INVALID);
        }

        return jwtTokenProvider.reissueAccessToken(refreshToken);
    }

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId, UserType userType) {
        return switch (userType) {
            case CAREGIVER -> new UserDto(caregiverRepository.findById(userId)
                    .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND)));
            case CARETAKER -> new UserDto(caretakerRepository.findById(userId)
                    .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND)));
        };
    }

    private void validateJoinInfo(JoinDto joinDto) {
        if (caregiverRepository.existsByLoginId(joinDto.getLoginId()) || caretakerRepository.existsByLoginId(joinDto.getLoginId())) {
            throw new PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_LOGIN_ID);
        }
        if (caregiverRepository.existsByEmail(joinDto.getEmail()) || caretakerRepository.existsByEmail(joinDto.getEmail())) {
            throw new PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_EMAIL);
        }
        if (caregiverRepository.existsByPhoneNumber(joinDto.getPhoneNumber()) || caretakerRepository.existsByPhoneNumber(joinDto.getPhoneNumber())) {
            throw new PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_PHONE_NUMBER);
        }
    }
}