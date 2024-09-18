package com.sw.ecogrowbackend.config;

import com.sw.ecogrowbackend.jwt.JwtUtil;
import com.sw.ecogrowbackend.security.JwtAuthorizationFilter;
import com.sw.ecogrowbackend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// JWT를 사용 => 인증 및 권한 부여 설정, 비밀번호 인코딩, 필터 체인 설정 등을 정의
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 방어 기능 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 세션을 사용하지 않고, JWT 방식을 사용하도록 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 리소스(static resources) 접근을 허용
                .requestMatchers("/**").permitAll()
                .requestMatchers("/api/user/signup").permitAll()
                .requestMatchers("/api/user/login").permitAll()
                .requestMatchers("/api/admin/**").permitAll()
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
        );

        // accessDeniedHandler 를 사용할 때, 새로 만든 빈 메서드를 호출하도록 변경
        // => AccessDeniedHandlerImpl 에 대한 빈이 제공 & 의존성 주입 문제 해결
        http.exceptionHandling(exceptionHandling ->
            exceptionHandling.accessDeniedHandler(accessDeniedHandler())
        );

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}