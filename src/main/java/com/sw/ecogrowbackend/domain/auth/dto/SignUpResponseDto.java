package com.sw.ecogrowbackend.domain.auth.dto;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.entity.UserRoleEnum;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponseDto {
    Long id;
    String username;
    String name;
    String email;
    UserRoleEnum role;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    @Builder
    public SignUpResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}