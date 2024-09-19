package com.sw.ecogrowbackend.common;

import lombok.Getter;

@Getter
public enum ResponseText {
    
    // admin
    ADMIN_SIGNUP_SUCCESS("관리자 회원가입 성공"),
    ADMIN_LOGIN_SUCCESS("관리자 로그인 성공"),
    ADMIN_USER_DELETE_SUCCESS("유저 회원탈퇴 처리 성공"),
    ADMIN_GET_ALL_PROFILE_SUCCESS("전체 프로필 조회 성공"),
    ADMIN_GET_ROLE_STATUS_SUCCESS("역할: %s, 상태: %s 상태의 프로필 조회 성공"),

    // auth
    AUTH_SIGNUP_SUCCESS("회원가입 성공"),
    AUTH_LOGIN_SUCCESS("로그인 성공"),
    AUTH_LOGOUT_SUCCESS("로그아웃 성공"),
    AUTH_RESIGN_SUCCESS("회원탈퇴 성공"),
    AUTH_TOKEN_REISSUE_SUCCESS("토큰 재발급 성공");

    private String msg;

    ResponseText(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String format(Object... args) {
        return String.format(msg, args);
    }
}