package com.mednine.pillbuddy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.dto.UserDto;
import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.dto.UserUpdateDto;
import com.mednine.pillbuddy.domain.user.entity.Role;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

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