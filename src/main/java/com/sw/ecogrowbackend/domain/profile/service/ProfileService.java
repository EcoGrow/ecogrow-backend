package com.sw.ecogrowbackend.domain.profile.service;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.domain.profile.dto.ProfileDto;
import com.sw.ecogrowbackend.domain.profile.entity.UserProfile;
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


    // 조회
    public ProfileDto getProfile(Long userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
            .orElseThrow(
                () -> new IllegalArgumentException("Profile not found for userId: " + userId));

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
            .orElseThrow(() -> new IllegalArgumentException("User not found for id: " + userId));

        // 이미 프로필이 있는지 확인
        if (profileRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("Profile already exists for this user.");
        }

        // 새 프로필 생성
        UserProfile profile = new UserProfile(user, profileDto.getBio(),
            profileDto.getProfileImageUrl());
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
            .orElseThrow(() -> new IllegalArgumentException("User not found for id: " + userId));

        // 기존 프로필 찾기
        UserProfile profile = profileRepository.findByUserId(userId)
            .orElseThrow(
                () -> new IllegalArgumentException("Profile not found for userId: " + userId));

        // 프로필 업데이트
        profile.setBio(profileDto.getBio());
        profile.setProfileImageUrl(profileDto.getProfileImageUrl());
        profile = profileRepository.save(profile);

        return new ProfileDto(
            profile.getId(),
            profile.getUser().getId(),
            profile.getBio(),
            profile.getProfileImageUrl()
        );
    }

    // 프로필 삭제 메소드
    public void deleteProfile(Long userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
            .orElseThrow(
                () -> new IllegalArgumentException("Profile not found for userId: " + userId));
        profileRepository.delete(profile);
    }
}