package com.mednine.pillbuddy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.dto.UserUpdateDto;
import com.mednine.pillbuddy.domain.user.entity.Role;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import com.mednine.pillbuddy.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CaretakerRepository caretakerRepository;

    @Autowired
    private CaregiverRepository caregiverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Caretaker caretaker;

    private Caregiver caregiver;

    @BeforeEach
    void setup() {
        createCaretaker();
        createCaregiver();
        caretakerRepository.save(caretaker);
        caregiverRepository.save(caregiver);
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입을 할 수 있다.")
    void join() {
        // given
        JoinDto joinDto = new JoinDto(
                "newUser", "newLoginId", "newPassword",
                "new@gmail.com", "010-1112-2221", UserType.CARETAKER);

        // when
        UserDto userDto = userService.join(joinDto);

        // then
        assertThat(userDto.getUsername()).isEqualTo("newUser");
        assertThat(userDto.getPhoneNumber()).isEqualTo("010-1112-2221");
        assertThat(userDto.getEmail()).isEqualTo("new@gmail.com");
        assertThat(userDto.getUserType()).isEqualTo("caretaker");
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 이메일을 입력하면 예외가 발생한다.")
    void join_with_duplicated_email_exception() {
        String duplicatedEmail = "test-email";

        JoinDto joinDto = new JoinDto(
                "newUser", "newLoginId", "newPassword",
                duplicatedEmail, "010-1112-2222", UserType.CARETAKER);

        assertThatThrownBy(() -> userService.join(joinDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_ALREADY_REGISTERED_EMAIL.getMessage());
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 로그인 아이디를 입력하면 예외가 발생한다.")
    void join_with_duplicated_login_id_exception() {
        String duplicatedLoginId = "test-loginId";

        JoinDto joinDto = new JoinDto(
                "newUser", duplicatedLoginId, "newPassword",
                "new@gmail.com", "010-1112-2223", UserType.CARETAKER);

        assertThatThrownBy(() -> userService.join(joinDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_ALREADY_REGISTERED_LOGIN_ID.getMessage());
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 전화번호를 입력하면 예외가 발생한다.")
    void join_with_duplicated_phoneNumber_exception() {
        String duplicatedPhoneNumber = "test-phoneNumber";

        JoinDto joinDto = new JoinDto(
                "newUser", "newLoginId", "newPassword",
                "new@gmail.com", duplicatedPhoneNumber, UserType.CARETAKER);

        assertThatThrownBy(() -> userService.join(joinDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_ALREADY_REGISTERED_PHONE_NUMBER.getMessage());
    }

    @Test
    @DisplayName("LoginDto 를 통해 로그인을 할 수 있다.")
    void login() {
        // given
        String loginId = caretaker.getLoginId();
        String password = "test-password";
        LoginDto loginDto = new LoginDto(loginId, password);

        // when
        JwtToken jwtToken = userService.login(loginDto);
        Authentication authentication = jwtTokenProvider.getAuthenticationByToken(jwtToken.getAccessToken());

        // then
        assertThat(jwtToken.getGrantType()).isEqualTo("Bearer");
        assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        assertThat(authentication.getName()).isEqualTo(loginId);
    }

    @Test
    @DisplayName("LoginId 를 찾을 수 없다면, InternalAuthenticationServiceException 이 발생한다.")
    void login_with_invalid_loginId() {
        String invalidLoginId = "invalidLoginId";
        String password = "password3";
        LoginDto loginDto = new LoginDto(invalidLoginId, password);

        // when
        assertThatThrownBy(() -> userService.login(loginDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("password 가 저장된 비밀번호와 일치하지 않다면, BadCredentialsException 이 발생한다.")
    void login_with_invalid_password() {
        String loginId = caretaker.getLoginId();
        String invalidPassword = "invalidPassword";
        LoginDto loginDto = new LoginDto(loginId, invalidPassword);

        // when
        assertThatThrownBy(() -> userService.login(loginDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_MISMATCHED_ID_OR_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("사용자는 refreshToken 을 통해 토큰을 재발급 할 수 있다.")
    void reissueToken() {
        // given
        String loginId = caretaker.getLoginId();
        String password = "test-password";
        LoginDto loginDto = new LoginDto(loginId, password);

        JwtToken jwtToken = userService.login(loginDto);
        String refreshToken = "Bearer " + jwtToken.getRefreshToken();

        // when
        JwtToken reissuedJwtToken = userService.reissueToken(refreshToken);
        String reissuedAccessToken = reissuedJwtToken.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthenticationByToken(reissuedAccessToken);

        // then
        assertThat(authentication.getName()).isEqualTo(loginId);
        assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("유효하지 않은 refreshToken 이면, PillBuddyException 이 발생한다.")
    void reissueToken_with_expired_refreshToken() {
        // given
        String malformedRefreshToken = "Bearer malformed_jwt_token";

        assertThatThrownBy(() -> userService.reissueToken(malformedRefreshToken))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("유효하지 않은 JWT 토큰입니다.");
    }

    @Test
    @DisplayName("GrantType 이 없는 refreshToken 이면, PillBuddyException 이 발생한다.")
    void reissueToken_without_grantType() {
        // given
        String refreshToken = "simple_refresh_token";

        assertThatThrownBy(() -> userService.reissueToken(refreshToken))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("유효하지 않은 JWT 토큰입니다.");
    }

    @Test
    @DisplayName("사용자를 조회할 수 있다.")
    void findCaretaker() {
        // given
        Long userId = caretaker.getId();
        UserType userType = UserType.from(caretaker);

        // when
        UserDto userDto = userService.findUser(userId, userType);

        // then
        assertThat(userDto.getUsername()).isEqualTo(caretaker.getUsername());
        assertThat(userDto.getPhoneNumber()).isEqualTo(caretaker.getPhoneNumber());
        assertThat(userDto.getEmail()).isEqualTo(caretaker.getEmail());
        assertThat(userDto.getUserType()).isEqualTo(String.valueOf(userType).toLowerCase());
    }

    @Test
    @DisplayName("보호자를 조회할 수 있다.")
    void findCaregiver() {
        // given
        Long userId = caregiver.getId();
        UserType userType = UserType.from(caregiver);

        // when
        UserDto userDto = userService.findUser(userId, userType);

        // then
        assertThat(userDto.getUsername()).isEqualTo(caregiver.getUsername());
        assertThat(userDto.getPhoneNumber()).isEqualTo(caregiver.getPhoneNumber());
        assertThat(userDto.getEmail()).isEqualTo(caregiver.getEmail());
        assertThat(userDto.getUserType()).isEqualTo(String.valueOf(userType).toLowerCase());
    }

    @Test
    @DisplayName("등록되지 않은 사용자를 조회하면 PillBuddyCustomException 이 발생한다.")
    void findCaretaker_with_invalid_caretaker() {
        Long userId = 99999L;
        UserType userType = UserType.CARETAKER;

        assertThatThrownBy(() -> userService.findUser(userId, userType))
                .isExactlyInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("등록되지 않은 보호자를 조회하면 PillBuddyCustomException 이 발생한다.")
    void findCaregiver_with_invalid_caregiver() {
        Long userId = 99999L;
        UserType userType = UserType.CAREGIVER;

        assertThatThrownBy(() -> userService.findUser(userId, userType))
                .isExactlyInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("회원 정보를 수정할 수 있다.")
    void updateUserInfo() {
        // given
        Long userId = caretaker.getId();

        UserType userType = UserType.from(caretaker);
        String updateUsername = "new-username";
        String updateLoginId = "new-loginId";
        String updateEmail = "new-email";
        String updatePhoneNumber = "new-phoneNumber";

        UserUpdateDto userUpdateDto = new UserUpdateDto(updateUsername, updateLoginId, updateEmail, updatePhoneNumber, userType);

        // when
        UserDto userDto = userService.updateUserInfo(userId, userUpdateDto);

        // then
        assertThat(userDto.getUsername()).isEqualTo(updateUsername);
        assertThat(userDto.getLoginId()).isEqualTo(updateLoginId);
        assertThat(userDto.getEmail()).isEqualTo(updateEmail);
        assertThat(userDto.getPhoneNumber()).isEqualTo(updatePhoneNumber);
    }

    @Test
    @DisplayName("회원 정보를 삭제할 수 있다.")
    void deleteUserInfo() {
        // given
        Long userId = caretaker.getId();
        UserType userType = UserType.from(caretaker);

        // when
        userService.deleteUser(userId, userType);

        // then
        assertThatThrownBy(() -> userService.findUser(userId, userType))
                .isExactlyInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    private void createCaretaker() {
        caretaker = Caretaker.builder()
                .username("test-caretaker")
                .loginId("test-loginId")
                .password(passwordEncoder.encode("test-password"))
                .email("test-email")
                .phoneNumber("test-phoneNumber")
                .role(Role.USER)
                .build();
    }

    private void createCaregiver() {
        caregiver = Caregiver.builder()
                .username("test-caregiver")
                .loginId("test-loginId")
                .password(passwordEncoder.encode("test-password"))
                .email("test-email")
                .phoneNumber("test-phoneNumber")
                .role(Role.USER)
                .build();
    }
}