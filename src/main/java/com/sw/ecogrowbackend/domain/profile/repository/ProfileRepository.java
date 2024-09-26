package com.sw.ecogrowbackend.domain.profile.repository;

import com.sw.ecogrowbackend.domain.profile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
}