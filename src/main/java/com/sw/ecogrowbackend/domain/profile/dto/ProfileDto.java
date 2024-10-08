package com.sw.ecogrowbackend.domain.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileDto {
    private final Long id;
    private final Long userId;

    @NotBlank(message = "Bio는 필수 입력 항목입니다.")
    @Size(max = 100, message = "Bio는 최대 100자까지 입력 가능합니다.")
    private final String bio;

    @Size(max = 100, message = "프로필 이미지 URL은 최대 100자까지 입력 가능합니다.")
    private final String profileImageUrl;

    @Builder
    public ProfileDto(Long id, Long userId, String bio, String profileImageUrl) {
        this.id = id;
        this.userId = userId;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }
}