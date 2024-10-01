package com.mednine.pillbuddy.domain.user.profile.controller;

import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long userId,
            @RequestParam UserType userType
    ) {
        profileService.uploadProfile(file, userId, userType);
        return ResponseEntity.noContent().build();
    }
}
