package com.mednine.pillbuddy.domain.user.profile.controller;

import com.mednine.pillbuddy.domain.user.profile.dto.ProfileUploadDto;
import com.mednine.pillbuddy.domain.user.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> upload(
            @PathVariable Long userId,
            @ModelAttribute ProfileUploadDto profileUploadDto
            ) {
        profileService.uploadProfile(userId, profileUploadDto);

        return ResponseEntity.noContent().build();
    }
}
