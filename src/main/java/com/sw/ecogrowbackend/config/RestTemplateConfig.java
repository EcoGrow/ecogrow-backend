package com.sw.ecogrowbackend.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 외부 API와의 통신을 위해 사용되는 RestTemplate의 타임아웃 설정을 정의하고, 애플리케이션 내에서 사용할 수 있도록 빈으로 등록
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 빈을 생성하고 설정
     *
     * @param restTemplateBuilder RestTemplate을 빌드하기 위한 빌더 객체입니다.
     * @return RestTemplate 외부 API 호출을 위한 RestTemplate 객체입니다.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            // RestTemplate으로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 경우
            // 무한 대기 상태를 방지하기 위해 강제 종료 시간을 설정합니다.
            .setConnectTimeout(Duration.ofSeconds(5)) // 연결 타임아웃을 5초로 설정
            .setReadTimeout(Duration.ofSeconds(5)) // 읽기 타임아웃을 5초로 설정
            .build(); // 설정을 적용하여 RestTemplate 객체를 빌드
    }
}