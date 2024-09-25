package com.sw.ecogrowbackend.domain.admin.controller;

import com.sw.ecogrowbackend.common.ApiResponse;
import com.sw.ecogrowbackend.common.ResponseText;
import com.sw.ecogrowbackend.domain.admin.dto.AdminRequestDto;
import com.sw.ecogrowbackend.domain.admin.dto.AdminResponseDto;
import com.sw.ecogrowbackend.domain.admin.service.AdminService;
import com.sw.ecogrowbackend.domain.auth.dto.TokenResponseDto;
import com.sw.ecogrowbackend.domain.auth.dto.UserResponseDto;
import com.sw.ecogrowbackend.security.UserDetailsImpl;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 관리자 회원가입 API
     *
     * @param adminRequestDto 관리자 회원가입 요청 데이터 (username, password, name 포함)
     * @return 관리자 회원가입 응답 데이터 (AdminResponseDto)
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse> adminSignup(@RequestBody AdminRequestDto adminRequestDto)
    {
        AdminResponseDto adminResponseDto = adminService.adminSignup(adminRequestDto);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.ADMIN_SIGNUP_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(adminResponseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 관리자 로그인 API
     *
     * @param adminRequestDto 로그인 요청 데이터 (username, password 포함)
     * @return JWT 토큰 응답 데이터 (TokenResponseDto)
     */
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> adminLogin(@RequestBody AdminRequestDto adminRequestDto)
    {
        TokenResponseDto responseDto = adminService.adminLogin(adminRequestDto);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.ADMIN_LOGIN_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 관리자가 유저를 강제로 탈퇴시키는 API
     *
     * @param userId 탈퇴시킬 유저의 ID
     * @return 회원 탈퇴 성공 응답 데이터
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> adminDelete(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        adminService.adminDelete(userId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.ADMIN_USER_DELETE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 유저의 관리자 권한을 승인하는 API
     *
     * @param userId 승인할 유저의 ID
     * @return 관리자 승인 성공 응답 데이터
     */
    @PostMapping("/approve/{userId}")
    public ResponseEntity<ApiResponse> approveAdmin(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        adminService.approveAdmin(userId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.ADMIN_APPROVE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 유저의 관리자 권한 요청을 거부하는 API
     *
     * @param userId 거부할 유저의 ID
     * @return 관리자 권한 거부 성공 응답 데이터
     */
    @PostMapping("/reject/{userId}")
    public ResponseEntity<ApiResponse> rejectAdmin(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        adminService.rejectAdmin(userId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.ADMIN_REJECT_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 승인 대기 중인 유저 목록 조회 API
     *
     * @return 승인 대기 유저 리스트 응답 데이터
     */
    @GetMapping("/pending-approvals")
    public ResponseEntity<ApiResponse> dgetPendingApprovals()
    {
        List<UserResponseDto> pendingUsers = adminService.getPendingApprovalUsers();
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PENDING_APPROVAL_LIST_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(pendingUsers)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}