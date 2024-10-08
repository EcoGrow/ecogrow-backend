package com.sw.ecogrowbackend.domain.profile.service;

import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.domain.profile.dto.ProfileDto;
import com.sw.ecogrowbackend.domain.profile.entity.Profile;
import com.sw.ecogrowbackend.domain.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    // 프로필 조회
    public ProfileDto getProfile(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new ProfileDto(
            profile.getId(),
            profile.getUser().getId(),
            profile.getBio(),
            profile.getProfileImageUrl()
        );
    }

    // 프로필 생성
    public ProfileDto createProfile(Long userId, ProfileDto profileDto) {
        // 유저 확인
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 이미 프로필이 있는지 확인
        if (profileRepository.findByUserId(userId).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        // 새 프로필 생성
        Profile profile = new Profile(user, profileDto.getBio(), profileDto.getProfileImageUrl());
        profile = profileRepository.save(profile);

        return new ProfileDto(
            profile.getId(),
            profile.getUser().getId(),
            profile.getBio(),
            profile.getProfileImageUrl()
        );
    }

    // 프로필 수정
    public ProfileDto updateProfile(Long userId, ProfileDto profileDto) {
        // 유저 확인
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기존 프로필 찾기
        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기존 프로필을 새로운 정보로 업데이트된 새로운 프로필 객체로 교체
        Profile updatedProfile = new Profile(
            profile.getUser(),
            profileDto.getBio(),
            profileDto.getProfileImageUrl()
        );
        updatedProfile = profileRepository.save(updatedProfile);

        return new ProfileDto(
            updatedProfile.getId(),
            updatedProfile.getUser().getId(),
            updatedProfile.getBio(),
            updatedProfile.getProfileImageUrl()
        );
    }

    // 프로필 삭제
    public void deleteProfile(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        profileRepository.delete(profile);
    }
}