package com.mednine.pillbuddy.domain.user.service;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserPasswordUpdateDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.dto.UserUpdateDto;
import com.mednine.pillbuddy.domain.user.entity.User;
import com.mednine.pillbuddy.domain.user.profile.entity.Image;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import com.mednine.pillbuddy.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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
    private final MyUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto join(JoinDto joinDto) {
        validateUserInfo(joinDto.getLoginId(), joinDto.getEmail(), joinDto.getPhoneNumber());

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
        // 로그인 아이디를 통해 회원 조회
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getLoginId());

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            throw new PillBuddyCustomException(ErrorCode.USER_MISMATCHED_ID_OR_PASSWORD);
        }

        // 로그인 아이디를 통해 Jwt 토큰 생성 후 반환
        return jwtTokenProvider.generateToken(loginDto.getLoginId());
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
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new PillBuddyCustomException(ErrorCode.JWT_TOKEN_INVALID);
        }

        return jwtTokenProvider.reissueAccessToken(refreshToken);
    }

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId, UserType userType) {
        return new UserDto(findUserByIdAndUserType(userId, userType));
    }

    public UserDto updateUserInfo(Long userId, UserUpdateDto userUpdateDto) {
        User user = findUserByIdAndUserType(userId, userUpdateDto.getUserType());

        validateUserInfo(userUpdateDto.getLoginId(), userUpdateDto.getEmail(), userUpdateDto.getPhoneNumber());

        user.updateUsername(userUpdateDto.getUsername());
        user.updateLoginId(userUpdateDto.getLoginId());
        user.updateEmail(userUpdateDto.getEmail());
        user.updatePhoneNumber(userUpdateDto.getPhoneNumber());

        return new UserDto(user);
    }

    public UserDto updateUserPassword(Long userId, UserPasswordUpdateDto userPasswordUpdateDto) {
        User user = findUserByIdAndUserType(userId, userPasswordUpdateDto.getUserType());

        String encoded = passwordEncoder.encode(userPasswordUpdateDto.getPassword());
        userPasswordUpdateDto.changeEncodedPassword(encoded);

        user.updatePassword(userPasswordUpdateDto.getPassword());

        return new UserDto(user);
    }

    public void deleteUser(Long userId, UserType userType) {
        User user = findUserByIdAndUserType(userId, userType);

        Image image = user.getImage();

        if (user instanceof Caretaker) {
            caretakerRepository.delete((Caretaker) user);
        } else if (user instanceof Caregiver) {
            caregiverRepository.delete((Caregiver) user);
        }

        if (image != null) {
            image.deleteImageFile();
        }
    }

    private void validateUserInfo(String loginId, String email, String phoneNumber) {
        if (caregiverRepository.existsByLoginId(loginId) || caretakerRepository.existsByLoginId(loginId)) {
            throw new PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_LOGIN_ID);
        }
        if (caregiverRepository.existsByEmail(email) || caretakerRepository.existsByEmail(email)) {
            throw new PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_EMAIL);
        }
        if (caregiverRepository.existsByPhoneNumber(phoneNumber) || caretakerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_PHONE_NUMBER);
        }
    }

    private User findUserByIdAndUserType(Long userId, UserType userType) {
        return switch (userType) {
            case CAREGIVER -> caregiverRepository.findById(userId)
                    .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND));
            case CARETAKER -> caretakerRepository.findById(userId)
                    .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND));
        };
    }
}