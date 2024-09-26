package com.sw.ecogrowbackend.domain.profile.controller;

import com.sw.ecogrowbackend.common.ApiResponse;
import com.sw.ecogrowbackend.common.ResponseText;
import com.sw.ecogrowbackend.domain.profile.dto.ProfileDto;
import com.sw.ecogrowbackend.domain.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * 프로필 생성 API
     *
     * @param userId     사용자 ID
     * @param profileDto 프로필 생성 요청 데이터
     * @return 프로필 생성 응답 데이터
     */
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createProfile(@PathVariable Long userId, @Valid @RequestBody ProfileDto profileDto) {
        ProfileDto createdProfile = profileService.createProfile(userId, profileDto);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_CREATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(createdProfile)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 프로필 업데이트 API
     *
     * @param userId     사용자 ID
     * @param profileDto 프로필 업데이트 요청 데이터
     * @return 프로필 업데이트 응답 데이터
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long userId, @Valid @RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateProfile(userId, profileDto);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_UPDATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(updatedProfile)
            .build();
        return ResponseEntity.ok(response);
    }
}
