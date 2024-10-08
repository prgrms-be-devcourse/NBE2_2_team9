package com.medinine.pillbuddy.domain.user.service;

import com.medinine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.medinine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final CaretakerRepository caretakerRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return caretakerRepository.findByLoginId(loginId)
                .map(CustomUserDetails::new)
                .or(() -> caregiverRepository.findByLoginId(loginId).map(CustomUserDetails::new))
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND));
    }
}
