package com.mednine.pillbuddy.domain.user.profile.dto;

import com.mednine.pillbuddy.domain.user.dto.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ProfileUploadDto {

    @NotNull
    private MultipartFile file;

    @NotNull
    private UserType userType;
}
