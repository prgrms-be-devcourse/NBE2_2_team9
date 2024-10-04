package com.mednine.pillbuddy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.entity.Role;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MyUserDetailsServiceTest {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private CaretakerRepository caretakerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Caretaker caretaker;

    @BeforeEach
    void setup() {
        createCaretaker();
        caretakerRepository.save(caretaker);
    }


    @Test
    @DisplayName("회원의 login Id 를 통해 회원 정보를 조회할 수 있다.")
    void loadUserByUsername() {
        // given
        String loginId = caretaker.getLoginId();

        // when
        CustomUserDetails details = myUserDetailsService.loadUserByUsername(loginId);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        // then
        assertThat(details.getUsername()).isEqualTo(loginId);
        assertThat(details.getPassword()).isEqualTo(caretaker.getPassword());
        assertThat(authorities.size()).isEqualTo(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("저장되지 않은 login Id 를 입력하면 PillBuddyCustomException 이 발생한다.")
    void loadUserByUsername_exception() {
        String loginId = "AnonymousUser1";

        assertThatThrownBy(() -> myUserDetailsService.loadUserByUsername(loginId))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
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
}