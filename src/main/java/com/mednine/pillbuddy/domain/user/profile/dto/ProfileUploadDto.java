package com.mednine.pillbuddy.domain.user.profile.dto;

import com.mednine.pillbuddy.domain.user.dto.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ProfileUploadDto {

    private MultipartFile file;
    private UserType userType;
}
