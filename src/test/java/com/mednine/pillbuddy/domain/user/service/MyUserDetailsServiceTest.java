package com.mednine.pillbuddy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTest
class MyUserDetailsServiceTest {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Test
    @DisplayName("회원의 login Id 를 통해 회원 정보를 조회할 수 있다.")
    void loadUserByUsername() {
        // given
        String loginId = "caretaker1";

        // when
        CustomUserDetails details = myUserDetailsService.loadUserByUsername(loginId);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        // then
        assertThat(details.getUsername()).isEqualTo(loginId);
        assertThat(details.getPassword()).isEqualTo("password1");
        assertThat(authorities.size()).isEqualTo(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("USER");
    }

    @Test
    @DisplayName("저장되지 않은 login Id 를 입력하면 PillBuddyCustomException 이 발생한다.")
    void loadUserByUsername_exception() {
        String loginId = "AnonymousUser1";

        assertThatThrownBy(() -> myUserDetailsService.loadUserByUsername(loginId))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }
}