package com.sw.ecogrowbackend.config.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id; // 카카오 사용자 고유 ID
    private String nickname; // 카카오 사용자 닉네임
    private String email; // 카카오 사용자 이메일

    public KakaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}