package com.sw.ecogrowbackend.domain.admin.dto;

import java.time.LocalDateTime;

public record AdminResponseDto(Long id, String username, String name, String email, LocalDateTime createdAt, LocalDateTime modifiedAt, String role) {}