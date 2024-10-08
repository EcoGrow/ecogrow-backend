package com.sw.ecogrowbackend.domain.admin.service;

import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.domain.admin.dto.AdminRequestDto;
import com.sw.ecogrowbackend.domain.admin.dto.AdminResponseDto;
import com.sw.ecogrowbackend.domain.admin.mapper.AdminMapper;
import com.sw.ecogrowbackend.domain.auth.dto.TokenResponseDto;
import com.sw.ecogrowbackend.domain.auth.dto.UserResponseDto;
import com.sw.ecogrowbackend.domain.auth.entity.ApprovalStatus;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.entity.UserRoleEnum;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.domain.auth.service.RefreshTokenService;
import com.sw.ecogrowbackend.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AdminMapper adminMapper;
    private final UserRepository userRepository;

    /**
     * 관리자 회원가입
     *
     * @param adminRequestDto 관리자의 가입 정보를 담고 있는 DTO
     * @return 생성된 관리자의 정보를 담고 있는 AdminResponseDto
     * @throws CustomException 중복된 사용자 이름이 존재할 경우
     */
    @Transactional
    public AdminResponseDto adminSignup(AdminRequestDto adminRequestDto) {

        // 회원 중복 확인
        Optional<User> existingUser = userRepository.findByUsername(adminRequestDto.username());
        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        String encodedPassword = passwordEncoder.encode(adminRequestDto.password());
        UserRoleEnum role = UserRoleEnum.ADMIN;
        User user = new User(adminRequestDto.username(), encodedPassword,
            adminRequestDto.email(), role);

        userRepository.save(user);
        return adminMapper.toAdminResponseDto(user);
    }

    /**
     * 관리자 로그인
     *
     * @param adminRequestDto 관리자의 로그인 정보를 담고 있는 DTO
     * @return 로그인 후 발급된 토큰 정보를 담고 있는 TokenResponseDto
     * @throws CustomException 사용자 이름이 존재하지 않거나, 비밀번호가 틀리거나, 관리자가 아닌 경우
     */
    @Transactional
    public TokenResponseDto adminLogin(AdminRequestDto adminRequestDto) {
        User user = userRepository.findByUsername(adminRequestDto.username())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.isResigned()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(adminRequestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole().toString());

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    /**
     * 관리자가 강제로 유저 삭제 (탈퇴 처리)
     *
     * @param userId 유저의 ID
     * @param user 관리자 권한을 가진 사용자 객체
     * @throws CustomException 관리자 또는 유저가 존재하지 않거나, 유저가 이미 탈퇴한 경우
     */
    @Transactional
    public void adminDelete(Long userId, User user) {

        User userToResign = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_ADMIN);
        }

        if (user.isResigned()) {
            throw new CustomException(ErrorCode.RESIGN_USER);
        }

        userToResign.resign();
        userRepository.save(userToResign);
    }

    /**
     * 승인 대기 중인 사용자 목록을 조회
     *
     * @return 승인 대기 중인 사용자 정보를 담고 있는 UserResponseDto 목록
     */
    public List<UserResponseDto> getPendingApprovalUsers() {
        List<User> pendingUsers = userRepository.findByApprovalStatus(ApprovalStatus.PENDING);
        return pendingUsers.stream()
            .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getApprovalStatus()))
            .collect(Collectors.toList());
    }

    /**
     * 관리자를 승인
     *
     * @param userId 승인할 관리자의 ID
     * @param user 관리자 권한을 가진 사용자 객체
     * @throws CustomException 관리자가 존재하지 않거나 관리자 권한이 없는 경우
     */
    @Transactional
    public void approveAdmin(Long userId, User user) {

        User userToApprove = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_ADMIN);
        }

        userToApprove.approveAdmin();
        userRepository.save(userToApprove);
    }

    /**
     * 관리자 승인을 거부
     *
     * @param userId 거부할 관리자의 ID
     * @param user 관리자 권한을 가진 사용자 객체
     * @throws CustomException 관리자가 존재하지 않거나 관리자 권한이 없는 경우
     */
    @Transactional
    public void rejectAdmin(Long userId, User user) {

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_ADMIN);
        }

        User userToReject = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userToReject.rejectAdmin();
        userRepository.save(userToReject);
    }
}