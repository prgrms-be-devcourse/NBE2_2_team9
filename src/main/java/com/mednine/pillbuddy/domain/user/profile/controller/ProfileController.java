package com.mednine.pillbuddy.domain.user.profile.controller;

import com.mednine.pillbuddy.domain.user.profile.dto.ProfileUploadDto;
import com.mednine.pillbuddy.domain.user.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
@Tag(name = "사진 등록 기능",description = "이용자는 프로필 사진을 등록할 수 있다.")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(value = "/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(
            @PathVariable Long userId,
            @ModelAttribute ProfileUploadDto profileUploadDto
            ) {
        profileService.uploadProfile(userId, profileUploadDto);

        return ResponseEntity.noContent().build();
    }
}
