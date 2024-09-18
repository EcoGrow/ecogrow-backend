package com.sw.ecogrowbackend.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자와 관련된 리프레시 토큰을 관리
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token_id", unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken(String token, User user){
        this.token = token;
        this.user = user;
    }

    public void updateToken(String newRefreshToken) {
        this.token = newRefreshToken;
    }
}