package com.sw.ecogrowbackend.common;

import lombok.Getter;

@Getter
public enum ResponseText {

    // admin
    ADMIN_SIGNUP_SUCCESS("관리자 회원가입 성공"),
    ADMIN_LOGIN_SUCCESS("관리자 로그인 성공"),
    ADMIN_USER_DELETE_SUCCESS("유저 회원탈퇴 처리 성공"),
    ADMIN_APPROVE_SUCCESS("관리자 승인 성공"),
    ADMIN_REJECT_SUCCESS("관리자 승인 거부 성공"),
    PENDING_APPROVAL_LIST_SUCCESS("승인 대기 중인 유저 목록 조회 성공"),

    // auth
    AUTH_SIGNUP_SUCCESS("회원가입 성공"),
    AUTH_LOGIN_SUCCESS("로그인 성공"),
    AUTH_LOGOUT_SUCCESS("로그아웃 성공"),
    AUTH_RESIGN_SUCCESS("회원탈퇴 성공"),
    AUTH_TOKEN_REISSUE_SUCCESS("토큰 재발급 성공"),
    KAKAO_LOGIN_SUCCESS( "카카오 로그인 성공"),
    GOOGLE_LOGIN_SUCCESS( "구글 로그인 성공"),

    // profile
    PROFILE_CREATE_SUCCESS("프로필 생성 성공"),
    PROFILE_UPDATE_SUCCESS("프로필 업데이트 성공"),
    PROFILE_DELETE_SUCCESS("프로필 삭제 성공"),
    PROFILE_FETCH_SUCCESS("프로필 조회 성공");

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