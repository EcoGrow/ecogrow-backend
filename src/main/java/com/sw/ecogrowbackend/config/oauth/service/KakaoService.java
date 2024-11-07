package com.sw.ecogrowbackend.config.oauth.service;

import com.sw.ecogrowbackend.domain.auth.entity.RefreshToken;
import com.sw.ecogrowbackend.domain.auth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.ecogrowbackend.config.oauth.dto.KakaoUserInfoDto;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.entity.UserRoleEnum;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.jwt.JwtUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect_uri}")
    private String redirectURI;

    /**
     * 카카오 로그인을 처리하는 메서드.
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return 생성된 JWT 토큰을 반환
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외
     */
    public String kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getToken(code);
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken); // 액세스 토큰으로 카카오 사용자 정보 조회
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo); // 사용자 등록 필요 시 등록

        String createToken = jwtUtil.createAccessToken(kakaoUser.getUsername(),
            kakaoUser.getRole().toString()); // JWT 생성
        String refreshToken = jwtUtil.createRefreshToken(kakaoUser.getUsername(),
            kakaoUser.getRole().toString());

        refreshTokenRepository.save(new RefreshToken(refreshToken, kakaoUser));
        return createToken;
    }

    /**
     * 인가 코드를 사용하여 카카오 서버로부터 액세스 토큰을 얻는 메서드.
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return 액세스 토큰
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외
     */
    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 : " + code);

        // 카카오 토큰 요청 URL 생성
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com") // 카카오 인증 서버 주소
            .path("/oauth/token") // 토큰 요청 경로
            .encode()
            .build()
            .toUri();

        // HTTP 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectURI);
        body.add("code", code);

        // 토큰 요청
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // JSON 응답에서 액세스 토큰 추출
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    /**
     * 액세스 토큰을 사용하여 카카오 사용자 정보를 조회하는 메서드.
     *
     * @param accessToken 카카오 서버로부터 받은 액세스 토큰
     * @return KakaoUserInfoDto 카카오 사용자 정보 DTO
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외
     */
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken : " + accessToken);

        // 카카오 사용자 정보 요청 URL 생성
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com") // 카카오 API 서버 주소
            .path("/v2/user/me") // 사용자 정보 요청 경로
            .encode()
            .build()
            .toUri();

        // HTTP 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 사용자 정보 요청
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // JSON 응답에서 사용자 정보 추출
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
            .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);

        return new KakaoUserInfoDto(id, nickname, email);
    }

    /**
     * 카카오 사용자를 저장하거나 이미 존재하는지 확인하는 메서드.
     *
     * @param kakaoUserInfo 카카오 사용자 정보 DTO
     * @return 등록된 사용자 정보(User)
     */
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {

        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        // 사용자가 없으면 새로 등록
        if (kakaoUser == null) {
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);

            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser.kakaoIdUpdate(kakaoId);
            } else {
                // 새로운 사용자 생성
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // 이 부분에서 name 필드를 nickname으로 설정
                kakaoUser = new User(
                    kakaoUserInfo.getNickname(),
                    encodedPassword,
                    kakaoEmail,
                    UserRoleEnum.USER,
                    kakaoId,
                    null
                );
            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}