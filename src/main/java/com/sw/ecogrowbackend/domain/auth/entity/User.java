package com.sw.ecogrowbackend.domain.auth.entity;

import com.sw.ecogrowbackend.common.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 추가: 관리자 승인 상태 필드
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    // 탈퇴 상태 확인 메서드
    // 탈퇴 여부 및 탈퇴 시점 저장
    @Column(nullable = true)
    private LocalDateTime resignedAt;

    public User(String username, String password, String name, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.resignedAt = null;
    }

    // 관리자로 승격하는 메서드
    public void approveAdmin() {
        this.role = UserRoleEnum.ADMIN;
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    // 승인 거부하는 메서드
    public void rejectAdmin() {
        this.approvalStatus = ApprovalStatus.REJECTED;
    }

    // 탈퇴 처리 메서드 (탈퇴 시점 기록)
    public void resign() {
        this.resignedAt = LocalDateTime.now();
    }

    // 탈퇴 여부 확인
    public boolean isResigned() {
        return this.resignedAt != null;
    }
}