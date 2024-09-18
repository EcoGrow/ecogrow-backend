package com.sw.ecogrowbackend.domain.auth.entity;

import com.sw.ecogrowbackend.common.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
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
    private UserType userType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 기본 userType을 NORMAL로 설정
    public User(String username, String password, String name, String email, UserRoleEnum role) {
        this(username, password, name, email, UserType.NORMAL, role);
    }

    // 모든 필드 설정
    public User(String username, String password, String name, String email, UserType userType, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.role = role;
    }

    // 사용자 상태를 LEAVE(탈퇴)로 변경
    public void resign() {
        this.userType = UserType.LEAVE;
    }

    // 사용자가 탈퇴 상태이면 true 반환
    public boolean isResign() {
        return this.userType == UserType.LEAVE;
    }
}
