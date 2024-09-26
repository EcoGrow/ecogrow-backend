package com.sw.ecogrowbackend.domain.auth.dto;

import com.sw.ecogrowbackend.domain.auth.entity.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;          // 사용자 ID
    private String username;  // 사용자 이름
    private String name;      // 실제 이름
    private ApprovalStatus approvalStatus; // 승인 상태
}