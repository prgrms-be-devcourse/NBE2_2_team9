package com.mednine.pillbuddy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.domain.user.dto.JoinDto;
import com.mednine.pillbuddy.domain.user.dto.LoginDto;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.jwt.JwtToken;
import com.mednine.pillbuddy.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
        assertThat(userDto.getUserType()).isEqualTo("Caretaker");
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 이메일을 입력하면 예외가 발생한다.")
    void join_with_duplicated_email_exception() {
        String duplicatedEmail = "caretaker1@example.com";

        JoinDto joinDto = new JoinDto(
                "newUser", "newLoginId", "newPassword",
                duplicatedEmail, "010-1112-2222", UserType.CARETAKER);

        assertThatThrownBy(() -> userService.join(joinDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("이미 등록된 이메일입니다.");
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 로그인 아이디를 입력하면 예외가 발생한다.")
    void join_with_duplicated_login_id_exception() {
        String duplicatedLoginId = "caregiver1";

        JoinDto joinDto = new JoinDto(
                "newUser", duplicatedLoginId, "newPassword",
                "new@gmail.com", "010-1112-2223", UserType.CARETAKER);

        assertThatThrownBy(() -> userService.join(joinDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("이미 등록된 아이디입니다.");
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 이메일을 입력하면 예외가 발생한다.")
    void join_with_duplicated_phoneNumber_exception() {
        String duplicatedPhoneNumber = "010-1234-5678";

        JoinDto joinDto = new JoinDto(
                "newUser", "newLoginId", "newPassword",
                "new@gmail.com", duplicatedPhoneNumber, UserType.CARETAKER);

        assertThatThrownBy(() -> userService.join(joinDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("이미 등록된 전화번호입니다.");
    }

    @Test
    @DisplayName("LoginDto 를 통해 로그인을 할 수 있다.")
    void login() {
        // given
        String loginId = "caregiver3";
        String password = "password3";
        LoginDto loginDto = new LoginDto(loginId, password);

        // when
        JwtToken jwtToken = userService.login(loginDto);
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken.getAccessToken());

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
        assertThatThrownBy(() -> {
            userService.login(loginDto);
        }).isInstanceOf(InternalAuthenticationServiceException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("password 가 저장된 비밀번호와 일치하지 않다면, BadCredentialsException 이 발생한다.")
    void login_with_invalid_password() {
        String invalidLoginId = "caregiver3";
        String password = "invalidPassword";
        LoginDto loginDto = new LoginDto(invalidLoginId, password);

        // when
        assertThatThrownBy(() -> {
            userService.login(loginDto);
        }).isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");
    }
}