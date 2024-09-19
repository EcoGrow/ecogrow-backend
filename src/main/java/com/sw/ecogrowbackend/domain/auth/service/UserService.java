package com.sw.ecogrowbackend.domain.auth.service;

import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.domain.auth.dto.LoginRequestDto;
import com.sw.ecogrowbackend.domain.auth.dto.SignUpRequestDto;
import com.sw.ecogrowbackend.domain.auth.dto.SignUpResponseDto;
import com.sw.ecogrowbackend.domain.auth.dto.TokenResponseDto;
import com.sw.ecogrowbackend.domain.auth.entity.UserRoleEnum;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "";

    /**
     * 회원가입 로직
     *
     * @param requestDto 회원가입 요청 데이터. 사용자 이름, 패스워드, 이름, 이메일, 관리자인지 여부를 포함함.
     * @return SignUpResponseDto 회원가입 처리 결과. 가입한 유저의 정보를 포함함.
     * @throws CustomException 사용자 이름이 중복된 경우
     * @throws IllegalArgumentException 이메일이 중복된 경우, 관리자 암호가 틀린 경우
     */
    @Transactional
    public SignUpResponseDto signup(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        // 이메일 중복 확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 저장
        User user = new User(username, password, requestDto.getName(), email, role);
        userRepository.save(user);

        return SignUpResponseDto.builder().user(user).build();
    }

    /**
     * 로그인 로직
     *
     * @param requestDto 로그인 요청 데이터. 사용자 이름, 패스워드, 역할을 포함함.
     * @return TokenResponseDto 로그인 성공 시, 액세스 토큰과 리프레시 토큰을 반환함.
     * @throws CustomException 사용자 이름이 존재하지 않는 경우, 탈퇴한 사용자, 비밀번호 불일치, 역할 불일치
     */
    public TokenResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.isResign()) {
            throw new CustomException(ErrorCode.RESIGNED_USER);
        } else if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole().toString());

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    /**
     * 로그아웃 로직
     *
     * @param user 로그아웃할 사용자. 사용자 ID를 기반으로 리프레시 토큰을 제거함.
     */
    public void logout(User user) {
        refreshTokenService.removeRefreshToken(user.getId());
    }

    /**
     * 회원탈퇴 로직
     *
     * @param user 탈퇴할 사용자. 사용자 ID를 기반으로 리프레시 토큰을 제거하고, 사용자 상태를 탈퇴로 변경함.
     */
    public void resign(User user) {
        refreshTokenService.removeRefreshToken(user.getId());

        user.resign();
        userRepository.save(user);
    }
}